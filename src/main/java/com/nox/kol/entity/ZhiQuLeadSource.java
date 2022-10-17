package com.nox.kol.entity;

import java.io.Serializable;

/**
 * zhi_qi_lead_source
 * @author 
 */
public class ZhiQuLeadSource implements Serializable {


    private Long id;

    /**
     * xitongid
     */
    private String sysId;

    /**
     * 字段名称
     */
    private String name;

    private String creator;

    private Long createTime;

    private String modifier;

    private Long modifyTime;

    /**
     * 255
     */
    private String remark;

    private String isDel;

    /**
     * 父Id
     */
    private Long sourcePid;

    private String sourcePos;



    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSysId() {
        return sysId;
    }

    public void setSysId(String sysId) {
        this.sysId = sysId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public Long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Long modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public Long getSourcePid() {
        return sourcePid;
    }

    public void setSourcePid(Long sourcePid) {
        this.sourcePid = sourcePid;
    }

    public String getSourcePos() {
        return sourcePos;
    }

    public void setSourcePos(String sourcePos) {
        this.sourcePos = sourcePos;
    }


}