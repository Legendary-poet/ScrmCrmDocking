package com.nox.kol.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nox.kol.config.UserExcelListener;
import com.nox.kol.entity.User;
import com.nox.kol.service.UploadingExcel;
import com.nox.kol.utils.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/excel")
@Slf4j
public class ExcelController {
    @Autowired
    private UploadingExcel uploadingExcel;

//    @PostMapping("/import")
//    public JSONArray importUser(@RequestPart("file") MultipartFile file) throws Exception {
//        JSONArray array = ExcelUtils.readMultipartFile(file);
//        System.out.println("导入数据为:" + array);
//        uploadingExcel.uploadingLocalExcel(array);
//
//        return array;
//    }

    @PostMapping("/import")
    public void importUser(@RequestPart("file")MultipartFile file) throws Exception {
        List<User> users = ExcelUtils.readMultipartFile(file, User.class);
        uploadingExcel.uploadingLocalExcel(users);
//        for (User user : users) {
//            System.out.println(user.toString());
//        }
    }

//    @RequestMapping(value = "/testFileExcel")
//    public void testFileExcel(MultipartFile file) throws IOException {
//        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
//        // 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
//        EasyExcel.read(file.getInputStream(), User.class, new PageReadListener<User>(dataList -> {
////            try {
////                uploadingExcel.uploadingLocalExcel(dataList);
////            } catch (IOException e) {
////                throw new RuntimeException(e);
////            }
//            for (User demoData : dataList) {
//                // 这里就是你处理代码保存的逻辑了
////                log.info("读取到一条数据{}", JSON.toJSONString(demoData));
//                System.out.println(demoData.toString());
//
//            }
//        })).sheet(0).doRead();
//    }


//    @PostMapping("/testFileExcel")
//    public String testFileExcel(MultipartFile file) throws IOException {
//        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 文件流会自动关闭
//        // 这里每次会读取100条数据 然后返回过来 直接调用使用数据就行
//        EasyExcel.read(file.getInputStream(), User.class, new PageReadListener<User>(dataList -> {
//            for (User user : dataList) {
//                // 这里就是你处理代码保存的逻辑了
//                log.info("读取到一条数据{}", JSON.toJSONString(user));
//                System.out.println(user.toString());
//            }
//        })).sheet(0).doRead();
//        return null;
//    }
    @PostMapping("/upload")
    public String upload(MultipartFile file) throws IOException {
        EasyExcel.read(file.getInputStream(), User.class, new UserExcelListener()).sheet().doRead();

        return "success";
    }

}
