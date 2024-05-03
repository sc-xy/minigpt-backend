package com.xy.springboot;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.util.FileUtils;
import com.xy.springboot.utils.TemplateExcelUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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
}
