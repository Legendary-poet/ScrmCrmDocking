package com.nox.kol.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * CRMTest
 *
 */
@Service
@Slf4j
public class DuplicateCheckService {
    @Autowired
    private CRMAPIService CRMAPIService;

    @Autowired
    private SCRMAPIService SCRMAPIService;

    @Autowired
    private RedisTemplate redisTemplate;


    public void getCorpAccessToken() {
        String result = null;
        try {
            result = CRMAPIService.getCorpAccessToken();
            JSONObject jsonObject = JSONObject.parseObject(result);
            String corpAccessToken = (String) jsonObject.get("corpAccessToken");
            String corpId = (String) jsonObject.get("corpId");

            redisTemplate.opsForValue().set("corpAccessToken", corpAccessToken);
            redisTemplate.opsForValue().set("corpId", corpId);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }


    public void getByMobile() {
        String result = null;
        try {
            result = CRMAPIService.getByMobile();
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject empList = jsonObject.getJSONObject("empList");
            String openUserId = (String) empList.get("openUserId");


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }


    public int checkTheLeadsPhoneNumber(String lds_id, String phoneNumber) {
        String result = null;
        try {
            result = CRMAPIService.checkTheLeadsPhoneNumber(phoneNumber);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("dataList");
            if (jsonArray.size() == 0) {
                return 0;//没有重复
            } else if (jsonArray.size() >= 1) {
                //1将致趣线索和crm老线索映射
                //1.1线索id，所属bd,线索状态（判断线索是否转化）
                String crmId = jsonArray.getJSONObject(0).getString("_id");
                String crmOwner=null;
                try{

                    crmOwner = jsonArray.getJSONObject(0).getJSONArray("owner").get(0).toString();
                }catch(Exception e){
                    System.out.println("当前线索没有owner");
                }

//                String crmOwner = jsonArray.getJSONObject(0).getString("owner");
                String conversion = jsonArray.getJSONObject(0).getString("field_thOe1__c");//线索状态（转化）


                //根据线索所属人id查所属人name
                String crmOwnerName=null;//线索所属人BD name
                if (crmOwner!=null) {
                    String result6 = CRMAPIService.searchBdNameById(crmOwner);//
                    JSONObject jsonObject6 = JSONObject.parseObject(result6);
                    JSONArray jsonArray6 = jsonObject6.getJSONObject("data").getJSONArray("dataList");

                    crmOwnerName = jsonArray6.getJSONObject(0).getString("name");
                }

                //1.2查询crm的线索id和bd（所属人），  修改scrm

                //2判断线索是否已转化
                //2.1查询线索转化的客户不为空则已转化，为空没有转化
                String result3 = CRMAPIService.searchCompanyById(crmId);//根据crmid获取线索的客户
                JSONObject jsonObject1 = JSONObject.parseObject(result3);
                JSONArray jsonArray1 = jsonObject1.getJSONObject("data").getJSONArray("dataList");
                if (jsonArray1.size() == 0) {
                    //线索没有转化

                    //将CRM的线索ID更新至致趣.CRM线索ID字段，将线索所属BD(根据id查name)更新至致趣.CRM归属BD
                    String result1 = SCRMAPIService.updateLead(lds_id, "member_27105", crmId);
                    String result2 = SCRMAPIService.updateLead(lds_id, "member_27110", "线索所属BD:" + crmOwnerName);


                } else {
                    //线索已经转化
                    //将CRM的线索ID更新至致趣.CRM线索ID字段，将CRM的客户ID更新至致趣.CRM客户ID字段，
                    //将【线索状态+线索所属BD+客户成交状态+客户所属BD】 更新至致趣.CRM归属BD
                    String result4 = SCRMAPIService.updateLead(lds_id, "member_27105", crmId);
                    String accountId = jsonArray1.getJSONObject(0).getString("_id");//客户id
                    String result5 = SCRMAPIService.updateLead(lds_id, "member_27108", accountId);
                    String accountStatus = jsonArray1.getJSONObject(0).getString("deal_status");//客户成交状态
                    String accountOwner = jsonArray1.getJSONObject(0).getJSONArray("owner").get(0).toString();//客户所属bd

                    //根据客户所属人（bd）id查客户所属人name
                    String result7 = CRMAPIService.searchBdNameById(accountOwner);//
                    JSONObject jsonObject7 = JSONObject.parseObject(result7);
                    JSONArray jsonArray7 = jsonObject7.getJSONObject("data").getJSONArray("dataList");
                    String accountOwnerName = jsonArray7.getJSONObject(0).getString("name");//线索所属人BD name

                    //数据字典进行数据转换

                    String crmBdField = "线索状态:" + conversion + ",线索所属BD:" + crmOwnerName + ",客户成交状态:" + accountStatus + ",客户所属BD:" + accountOwnerName;
                    crmBdField = crmBdField.replace("h3mB6wAoj", "未分配")
                            .replace("Cj68eiU7a", "未跟进")
                            .replace("2Z31f4W0O", "跟进中")
                            .replace("JSTQf1blg", "已转换")
                            .replace("A3qpg5ia2", "已成交")
                            .replace("4NqjyM213", "无效");
                    crmBdField = crmBdField.replace("1", "未成交").replace("2", "已成交").replace("3", "多次成交");

                    String result8 = SCRMAPIService.updateLead(lds_id, "member_27110", crmBdField);


                }

                return 1;//有重复

            } else if (jsonArray.size() > 1) {
                //TODO : 抛异常，正常情况下crm里不应该有两个线索
                return 2;
            }

        } catch (Exception e) {
            System.out.println("线索电话号码查重失败"+e.toString());
            log.error("线索电话号码查重失败{}",e);
        }
        System.out.println(result);
        return 2;//异常
    }


    public int checkTheLeadsEmail(String lds_id, String email) {
        String result = null;
        try {

            result = CRMAPIService.checkTheLeadsEmail(email);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("dataList");
            if (jsonArray.size() == 0) {
                return 0;//没有重复
            } else if (jsonArray.size() >= 1) {
                //1将致趣线索和crm老线索映射
                //1.1线索id，所属bd,线索状态（判断线索是否转化）
                String crmId = jsonArray.getJSONObject(0).getString("_id");
                String crmOwner=null;
                try{

                    crmOwner = jsonArray.getJSONObject(0).getJSONArray("owner").get(0).toString();
                }catch(Exception e){
                    System.out.println("当前线索没有owner");
                }
                String conversion = jsonArray.getJSONObject(0).getString("field_thOe1__c");//线索状态（转化）

                //根据线索所属人id查所属人name
                //根据线索所属人id查所属人name
                String crmOwnerName=null;//线索所属人BD name
                if (crmOwner!=null) {
                    String result6 = CRMAPIService.searchBdNameById(crmOwner);//
                    JSONObject jsonObject6 = JSONObject.parseObject(result6);
                    JSONArray jsonArray6 = jsonObject6.getJSONObject("data").getJSONArray("dataList");

                    crmOwnerName = jsonArray6.getJSONObject(0).getString("name");
                }

                //1.2查询crm的线索id和bd（所属人），  修改scrm

                //2判断线索是否已转化
                //2.1查询线索转化的客户不为空则已转化，为空没有转化
                String result3 = CRMAPIService.searchCompanyById(crmId);//根据crmid获取线索的客户
                JSONObject jsonObject1 = JSONObject.parseObject(result3);
                JSONArray jsonArray1 = jsonObject1.getJSONObject("data").getJSONArray("dataList");

                if (jsonArray1.size() == 0) {
                    //线索没有转化

                    //将CRM的线索ID更新至致趣.CRM线索ID字段，将线索所属BD(根据id查name)更新至致趣.CRM归属BD
                    String result1 = SCRMAPIService.updateLead(lds_id, "member_27105", crmId);
                    String result2 = SCRMAPIService.updateLead(lds_id, "member_27110", "线索所属BD:" + crmOwnerName);


                } else {
                    //线索已经转化
                    //将CRM的线索ID更新至致趣.CRM线索ID字段，将CRM的客户ID更新至致趣.CRM客户ID字段，
                    //将【线索状态+线索所属BD+客户成交状态+客户所属BD】 更新至致趣.CRM归属BD
                    String result4 = SCRMAPIService.updateLead(lds_id, "member_27105", crmId);
                    String accountId = jsonArray1.getJSONObject(0).getString("_id");//客户id
                    String result5 = SCRMAPIService.updateLead(lds_id, "member_27108", accountId);
                    String accountStatus = jsonArray1.getJSONObject(0).getString("deal_status");//客户成交状态
                    String accountOwner = jsonArray1.getJSONObject(0).getJSONArray("owner").get(0).toString();//客户所属bd

                    //根据客户所属人（bd）id查客户所属人name
                    String result7 = CRMAPIService.searchBdNameById(accountOwner);//
                    JSONObject jsonObject7 = JSONObject.parseObject(result7);
                    JSONArray jsonArray7 = jsonObject7.getJSONObject("data").getJSONArray("dataList");
                    String accountOwnerName = jsonArray7.getJSONObject(0).getString("name");//线索所属人BD name


                    String crmBdField = "线索状态:" + conversion + ", 线索所属BD:" + crmOwnerName + ", 客户成交状态:" + accountStatus + ", 客户所属BD:" + accountOwnerName;
                    //数据字典进行数据转换
                    crmBdField = crmBdField.replace("h3mB6wAoj", "未分配")
                            .replace("Cj68eiU7a", "未跟进")
                            .replace("2Z31f4W0O", "跟进中")
                            .replace("JSTQf1blg", "已转换")
                            .replace("A3qpg5ia2", "已成交")
                            .replace("4NqjyM213", "无效");
                    crmBdField = crmBdField.replace("1", "未成交").replace("2", "已成交").replace("3", "多次成交");
                    String result8 = SCRMAPIService.updateLead(lds_id, "member_27110", crmBdField);


                }

                return 1;//有重复
            }

        } catch (Exception e) {
            System.out.println("线索邮箱查重失败"+e.toString());
            log.error("线索邮箱查重失败{}",e);
            e.printStackTrace();

        }
        System.out.println(result);
        return 2;//异常
    }


}