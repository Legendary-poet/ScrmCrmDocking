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
import org.springframework.web.bind.annotation.*;
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





    @GetMapping("/test")
    public String test(){

        return "test成功";
    }


}
