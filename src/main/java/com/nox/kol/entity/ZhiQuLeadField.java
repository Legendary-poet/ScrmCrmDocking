package com.nox.kol.entity;

import java.io.Serializable;

/**
 * zhi_qu_lead_field
 * @author 
 */
public class ZhiQuLeadField implements Serializable {
    private Integer id;

    /**
     * 同mid
     */
    private Integer sysId;

    /**
     * 字段名：创建线索API field_data传的key
     */
    private String name;

    /**
     * 字段标题
     */
    private String title;

    /**
     * 字段类型（1.文本输入框，2.文本域，3.数字输入框，4.日期输入框，5.单选，6.复选，7.下拉菜单
     */
    private Integer datatype;

    /**
     * 是否身份标识('N','Y')
     */
    private String isIdTag;

    /**
     * 是否必填
     */
    private String nullable;

    /**
     * 字段选项值
     */
    private String info;

    /**
     * 字段正则校验内容
     */
    private String regularContent;

    /**
     * '字段是否可被编辑删除（优先于操作权限） 0=不限制，1=不可编辑，2=不可删除，3.不可编辑及删除',
     */
    private Integer editStatus;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSysId() {
        return sysId;
    }

    public void setSysId(Integer sysId) {
        this.sysId = sysId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getDatatype() {
        return datatype;
    }

    public void setDatatype(Integer datatype) {
        this.datatype = datatype;
    }

    public String getIsIdTag() {
        return isIdTag;
    }

    public void setIsIdTag(String isIdTag) {
        this.isIdTag = isIdTag;
    }

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getRegularContent() {
        return regularContent;
    }

    public void setRegularContent(String regularContent) {
        this.regularContent = regularContent;
    }

    public Integer getEditStatus() {
        return editStatus;
    }

    public void setEditStatus(Integer editStatus) {
        this.editStatus = editStatus;
    }
}