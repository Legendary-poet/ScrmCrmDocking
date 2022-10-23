package com.nox.kol.entity;

import com.nox.kol.utils.ExcelImport;
import lombok.Data;

@Data
public class User {

    private int rowNum;

    @ExcelImport("姓名")
    private String name;
    @ExcelImport("邮箱")
    private String email;
    @ExcelImport("线索取得日期")
    private String day;
    @ExcelImport("电话")
    private String tel;
    @ExcelImport("原BD")
    private String bd;
    @ExcelImport("地址")
    private String address;
    @ExcelImport("标签/备注")
    private String tags;
    @ExcelImport("公司名")
    private String company;
    @ExcelImport("行业")
    private String trade;



}
