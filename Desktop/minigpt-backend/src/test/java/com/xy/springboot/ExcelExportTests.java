package com.xy.springboot;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.FileUtils;
import com.alibaba.excel.util.MapUtils;
import com.xy.springboot.model.dto.conversion.ConversationExportRequest;
import com.xy.springboot.model.entity.Message;
import com.xy.springboot.model.entity.Response;
import com.xy.springboot.service.MessageService;
import com.xy.springboot.service.ResponseService;
import com.xy.springboot.utils.TemplateExcelUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


@SpringBootTest
public class ExcelExportTests {
    @Test
    public void exportExcel() throws IOException {
//        String fileName = "D:\\imgSpace\\test.xlsx";
//        EasyExcel.write(fileName).sheet("测试").doWrite(dataList());
    }

//    private List<List<Object>> dataList() throws IOException {
//        List<List<Object>> list = new ArrayList<>();
//        String imgPath = "D:\\imgSpace\\3.jpg";
//        File imgFile = new File(imgPath);
//        BufferedImage image= ImageIO.read(imgFile);
//        Double width = (double) image.getWidth();
//        Double height = (double) image.getHeight();
//        WriteCellData<Void> voidWriteCellData = TemplateExcelUtils.imageCells(FileUtils.readFileToByteArray(imgFile),width,height,0.6,1.9);
//
//        list.add(new ArrayList<Object>() {{
//            add(voidWriteCellData);
//        }});
//        for (int i = 0; i < 10; i++) {
//            List<Object> data = new ArrayList<>();
//            data.add("字符串" + i);
//            data.add(0.56);
//            list.add(data);
//        }
//        return list;
//    }

    @Resource
    private MessageService messageService;

    @Resource
    private ResponseService responseService;

//    @Test
//    public void exportConversation() throws IOException {
//        Long conversationId = 7L;
//        List<Message> messages = messageService.getMessageByConversationId(conversationId);
//        List<Response> responses = responseService.getResponseByMessageIds(
//                messages.stream().map(Message::getId).collect(Collectors.toList()));
//        List<List<Object>> excelData = null;
//        try {
//            excelData = generateExcelData(conversationId, messages, responses);
//            System.out.println(excelData);
//            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//            response.setCharacterEncoding("utf-8");
//            EasyExcel.write(response.getOutputStream()).autoCloseStream(Boolean.FALSE).sheet("对话")
//                    .doWrite(excelData);
//        } catch (IOException e) {
//            response.reset();
//            response.setContentType("application/json");
//            response.setCharacterEncoding("utf-8");
//            Map<String, String> map = MapUtils.newHashMap();
//            map.put("status", "failure");
//            map.put("message", "下载文件失败" + e.getMessage());
//            response.getWriter().println(map);
//        }
//
//    }
//
//    public List<List<Object>> generateExcelData(Long conversationId, List<Message> messages, List<Response> responses) throws IOException {
//        List<List<Object>> list = new ArrayList<>();
//        String imgPath = "D:\\imgSpace\\" + conversationId + ".jpg";
//        File imgFile = new File(imgPath);
//        BufferedImage image= ImageIO.read(imgFile);
//        Double width = (double) image.getWidth();
//        Double height = (double) image.getHeight();
//        WriteCellData<Void> voidWriteCellData = TemplateExcelUtils.imageCells(FileUtils.readFileToByteArray(imgFile),width,height,0.6,1.9);
//
//        list.add(new ArrayList<Object>() {{
//            add(voidWriteCellData);
//        }});
//        Map<Long, String> repMap = new HashMap<>();
//        for (Response rep:responses) {
//            repMap.put(rep.getMessageId(), rep.getContent());
//        }
//        for (Message msg:messages) {
//            if (repMap.containsKey(msg.getId())) {
//                list.add(Arrays.asList(new String[]{"提问", msg.getContent()}));
//                list.add(Arrays.asList(new String[]{"回答", repMap.get(msg.getId())}));
//            }
//        }
//        return list;
//    }
}
