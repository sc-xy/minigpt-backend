package com.xy.springboot.controller;

import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xy.springboot.common.BaseResponse;
import com.xy.springboot.common.ErrorCode;
import com.xy.springboot.common.ResultUtils;
import com.xy.springboot.exception.BusinessException;
import com.xy.springboot.model.dto.conversion.ConversationAskRequest;
import com.xy.springboot.model.dto.conversion.ConversationDeleteRequest;
import com.xy.springboot.model.dto.conversion.ConversationQueryRequest;
import com.xy.springboot.model.entity.Conversation;
import com.xy.springboot.model.entity.Message;
import com.xy.springboot.model.entity.Response;
import com.xy.springboot.model.entity.User;
import com.xy.springboot.model.vo.ConversationInfoVO;
import com.xy.springboot.model.vo.ConversationVO;
import com.xy.springboot.service.ConversationService;
import com.xy.springboot.service.MessageService;
import com.xy.springboot.service.ResponseService;
import com.xy.springboot.service.UserService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 对话控制器
 */

@RestController
@RequestMapping("/conversation")
@Slf4j
public class ConversationController {
    @Resource
    private ConversationService conversationService;

    @Resource
    private UserService userService;

    @Resource
    private MessageService messageService;

    @Resource
    private ResponseService responseService;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 对话列表
     *
     * @return
     * @Param request
     */
    @GetMapping("/list")
    public BaseResponse<List<ConversationVO>> listConversation(HttpServletRequest request) {
        QueryWrapper<Conversation> queryWrapper = new QueryWrapper<>();
        User user = userService.getLoginUser(request);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        queryWrapper.eq("userId", user.getId());
        List<Conversation> conversationList = conversationService.list(queryWrapper);
        return ResultUtils.success(conversationService.convertConversationVOList(conversationList));
    }

    /**
     * 获取对话详细信息
     *
     * @return
     * @Param conversationQueryRequest
     */
    @GetMapping("/detail")
    public BaseResponse<ConversationInfoVO> getConversationDetail(ConversationQueryRequest conversationQueryRequest) {
        Long conversationId = conversationQueryRequest.getConversationId();
        log.info("conversationId: {}", conversationId);
        log.info("messageService: {}", messageService == null);
        List<Message> messages = messageService.getMessageByConversationId(conversationId);
        log.info("messages: {}", messages.size());
        List<Response> responses = responseService.getResponseByMessageIds(
                messages.stream().map(Message::getId).collect(Collectors.toList()));
        ConversationInfoVO conversationInfoVO = conversationService.buildConversationInfoVO(conversationId, messages, responses);
        return ResultUtils.success(conversationInfoVO);
    }

    /**
     * 对话
     *
     * @param request
     * @param conversationAskRequest
     * @return
     */
    @GetMapping("/ask")
    public SseEmitter askConversation(HttpServletRequest request,
                                      ConversationAskRequest conversationAskRequest) {
        User user = userService.getLoginUser(request);
        SseEmitter sseEmitter = new SseEmitter();
        sseEmitter.onCompletion(() -> {
            log.info("对话结束");
        });
        sseEmitter.onTimeout(() -> {
            log.info("对话超时");
        });
        threadPoolExecutor.execute(new SseClient(sseEmitter, conversationAskRequest, user, messageService, responseService, conversationService));
        return sseEmitter;
    }

    public class SseClient implements Runnable {
        private final SseEmitter sseEmitter;
        private final ConversationAskRequest conversationAskRequest;
        private final User user;
        private final MessageService messageService;
        private final ResponseService responseService;
        private final ConversationService conversationService;
        private final List<String> answers = new ArrayList<>();

        public SseClient(SseEmitter sseEmitter, ConversationAskRequest conversationAskRequest, User user,
                         MessageService messageService, ResponseService responseService, ConversationService conversationService) {
            this.sseEmitter = sseEmitter;
            this.conversationAskRequest = conversationAskRequest;
            this.user = user;
            this.messageService = messageService;
            this.responseService = responseService;
            this.conversationService = conversationService;
        }

        @Override
        public void run() {
            Request request = new Request.Builder()
                    .url("http://localhost:5000/sse?conversationId=" + conversationAskRequest.getConversationId() + "&ask=" + conversationAskRequest.getAsk())
                    .get()
                    .build();
            // OkHttp客户端
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .build();

            // 事件源工厂
            EventSource.Factory factory = EventSources.createFactory(client);
            CountDownLatch countDownLatch = new CountDownLatch(1);
            EventSourceListener listener = new SseListener(sseEmitter, countDownLatch, answers);

            EventSource eventSource = factory.newEventSource(request, listener);
            // 等待事件结束
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                log.error("SseEmitter error", e);
            }
            Long conversationId = conversationAskRequest.getConversationId();
            String ask = conversationAskRequest.getAsk();
            // 保存提问
            Message message = new Message();
            message.setConversationId(conversationId);
            message.setContent(ask);
            messageService.save(message);

            // 更新标题
            conversationService.updateTitle(conversationId, ask);

            // 保存回答
            Response response = new Response();
            response.setMessageId(message.getId());
            response.setContent(answers.get(answers.size() - 1));
            responseService.save(response);
            log.info(conversationId + ": " + ask + " " +
                    response.getContent());
            sseEmitter.complete();
        }
    }

    public class SseListener extends EventSourceListener {
        private final CountDownLatch countDownLatch;
        private final SseEmitter sseEmitter;
        private final List<String> answers;

        public SseListener(SseEmitter sseEmitter, CountDownLatch countDownLatch, List<String> answers) {
            this.sseEmitter = sseEmitter;
            this.countDownLatch = countDownLatch;
            this.answers = answers;
        }

        @Override
        public void onOpen(@NotNull EventSource eventSource, @NotNull okhttp3.Response response) {
        }

        @Override
        public void onEvent(@NotNull EventSource eventSource, String id, String type, String data) {
            try {
                sseEmitter.send(data);
                answers.add(data);
            } catch (Exception e) {
                log.error("SseEmitter send error", e);
            }
        }

        @Override
        public void onClosed(@NotNull EventSource eventSource) {
//            log.info("关闭sse连接...");
            countDownLatch.countDown();
        }

        @Override
        public void onFailure(@NotNull EventSource eventSource, Throwable t, okhttp3.Response response) {
            log.error("SseEmitter error", t);
            countDownLatch.countDown();
        }
    }

    /**
     * 删除对话
     *
     * @return
     * @Param request
     * @Param conversationDeleteRequest
     */
    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleteConversation(HttpServletRequest request,
                                                    @RequestBody ConversationDeleteRequest conversationDeleteRequest) {
        User user = userService.getLoginUser(request);
        conversationService.deleteConversation(conversationDeleteRequest.getConversationId(), user.getId());
        return ResultUtils.success(true);
    }

    /**
     * 创建对话
     *
     * @return
     * @Param request
     * @Param multipartFile
     */
    @PostMapping("/create")
    public BaseResponse<Long> createConversation(HttpServletRequest request,
                                                 @RequestPart("image") MultipartFile multipartFile) {
        // 1. 校验文件
        validFile(multipartFile);
        // 2. 创建新的对话，拿到对话id
        User user = userService.getLoginUser(request);
        Long conversationId = conversationService.createConversation(user.getId());
        // 3. 根据对话id拼接文件名
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        String filename = conversationId + "." + fileSuffix;
        String filepath = "D:/imgSpace/" + filename;
        // 4. 保存文件
        File file = new File(filepath);
        try {
            multipartFile.transferTo(file);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "文件上传失败");
        }
        return ResultUtils.success(conversationId);
    }

    /**
     * 校验文件
     *
     * @param multipartFile
     */
    private void validFile(MultipartFile multipartFile) {
        // 文件大小
        long fileSize = multipartFile.getSize();
        // 文件后缀
        String fileSuffix = FileUtil.getSuffix(multipartFile.getOriginalFilename());
        final long ONE_M = 3 * 1024 * 1024L;
        if (fileSize > ONE_M) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 3M");
        }
        if (!"jpg".equals(fileSuffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误");
        }
    }
}
