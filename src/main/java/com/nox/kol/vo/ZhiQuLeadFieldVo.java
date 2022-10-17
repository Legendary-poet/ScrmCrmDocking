package com.nox.kol.vo;

import com.nox.kol.entity.ZhiQuLeadField;

/**
 * @author tianxueyang
 * @version 1.0
 * @description
 * @date 2022/8/26 11:49
 */
public class ZhiQuLeadFieldVo extends ZhiQuLeadField {
    /***
     *  name
     *             mobile
     *     identification
     *             sex
     *     birthday
     *             email
     *     addr
     *             member_25956
     *     member_25957
     *             member_26227
     *     member_26228
     *     member_26416
     *     member_26417
     */
    public final  static  String FIELD_KEY_NAME="name"; //名字
    public final  static  String FIELD_KEY_MOBILE="mobile"; //电话
    public final  static  String FIELD_KEY_IDENTIFICATION="identification"; //身份证号
    public final  static  String FIELD_KEY_SEX="sex"; //性别 ，男 ，女
    public final  static  String FIELD_KEY_BIRTHDAY="birthday"; //生日
    public final  static  String FIELD_KEY_EMAIL="email"; //邮箱
    public final  static  String FIELD_KEY_ADRR="addr"; //地址
    public final  static  String FIELD_KEY_COMPANY_NAME="member_25956"; //公司名字
    public final  static  String FIELD_KEY_POSITION="member_25957"; //职位
    public final  static  String FIELD_KEY_REQUIREMENT="member_26227"; //需求
    public final  static  String FIELD_KEY_PROFESSION="member_26228"; //行业
    public final  static  String FIELD_KEY_UID="member_26416"; //UID
    public final  static  String FIELD_KEY_COMPANY_SCALE="member_26417"; //公司规模
    /***
     *
     *{
     *                 "sys_id":"32088",
     *                 "creator":"21681",
     *                 "source_pid":"28737",
     *                 "create_time":"1661427769",
     *                 "source_pos":"0",
     *                 "modifier":"0",
     *                 "modify_time":"0",
     *                 "name":"咨询营销专家",
     *                 "is_del":"n",
     *                 "remark":"",
     *                 "id":"29309"
     *             },
     *             {
     *                 "sys_id":"32088",
     *                 "creator":"21681",
     *                 "source_pid":"28737",
     *                 "create_time":"1661427786",
     *                 "source_pos":"0",
     *                 "modifier":"0",
     *                 "modify_time":"0",
     *                 "name":"联系我们",
     *                 "is_del":"n",
     *                 "remark":"",
     *                 "id":"29310"
     *             },
     *             {
     *                 "sys_id":"32088",
     *                 "creator":"21681",
     *                 "source_pid":"28737",
     *                 "create_time":"1661427804",
     *                 "source_pos":"0",
     *                 "modifier":"0",
     *                 "modify_time":"0",
     *                 "name":"官网注册",
     *                 "is_del":"n",
     *                 "remark":"",
     *                 "id":"29311"
     *             }
     *         ],
     */
    public final  static  String FIELD_KEY_SOURCE="source"; //来源

}
