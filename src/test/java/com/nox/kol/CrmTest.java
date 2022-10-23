package com.nox.kol;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nox.kol.service.CRMAPIService;
import com.nox.kol.service.SCRMAPIService;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ScrmCrmDockingApplication.class)
@EnableAsync
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class CrmTest {

    @Autowired
    private CRMAPIService CRMAPIService;

    @Autowired
    private SCRMAPIService SCRMAPIService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void getCorpAccessToken() {
        String result =null;
        try {
            result=  CRMAPIService.getCorpAccessToken();
            JSONObject jsonObject = JSONObject.parseObject(result);
            String corpAccessToken = (String) jsonObject.get("corpAccessToken");
            String corpId = (String) jsonObject.get("corpId");

            redisTemplate.opsForValue().set("corpAccessToken",corpAccessToken);
            redisTemplate.opsForValue().set("corpId",corpId);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }


    @Test
    public void getByMobile() {
        String result =null;
        try {
            result=  CRMAPIService.getByMobile();
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject empList = jsonObject.getJSONObject("empList");
            String openUserId = (String) empList.get("openUserId");


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }

    @Test
    public void checkTheLeadsPhoneNumber() {
        String result =null;
        try {
            String lds_id="17990128";
            String phoneNumber = "18810996277";
            result=  CRMAPIService.checkTheLeadsPhoneNumber(phoneNumber);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("dataList");
            if (jsonArray.size()==0){
//                return 0;//没有重复
            }else if(jsonArray.size()==1) {
                //1将致趣线索和crm老线索映射
                //1.1线索id，所属bd,线索状态（判断线索是否转化）
                String crmId = jsonArray.getJSONObject(0).getString("_id");
                String crmOwner = jsonArray.getJSONObject(0).getJSONArray("owner").get(0).toString();
                String conversion = jsonArray.getJSONObject(0).getString("field_thOe1__c");//线索状态（转化）


                //根据线索所属人id查所属人name
                String result6 = CRMAPIService.searchBdNameById(crmOwner);//
                JSONObject jsonObject6 = JSONObject.parseObject(result6);
                JSONArray jsonArray6 = jsonObject6.getJSONObject("data").getJSONArray("dataList");
                String  crmOwnerName= jsonArray6.getJSONObject(0).getString("name");//线索所属人BD name

                //1.2查询crm的线索id和bd（所属人），  修改scrm

                //2判断线索是否已转化
                //2.1查询线索转化的客户不为空则已转化，为空没有转化
                String result3 = CRMAPIService.searchCompanyById(crmId);//根据crmid获取线索的客户
                JSONObject jsonObject1 = JSONObject.parseObject(result3);
                JSONArray jsonArray1 = jsonObject1.getJSONObject("data").getJSONArray("dataList");
                if(jsonArray1.size()==0){
                    //线索没有转化

                    //将CRM的线索ID更新至致趣.CRM线索ID字段，将线索所属BD(根据id查name)更新至致趣.CRM归属BD
                    String result1=  SCRMAPIService.updateLead(lds_id,"member_27105",crmId);
                    String result2=  SCRMAPIService.updateLead(lds_id,"member_27110","线索所属BD:"+crmOwnerName);


                }else {
                    //线索已经转化
                    //将CRM的线索ID更新至致趣.CRM线索ID字段，将CRM的客户ID更新至致趣.CRM客户ID字段，
                    //将【线索状态+线索所属BD+客户成交状态+客户所属BD】 更新至致趣.CRM归属BD
                    String result4=  SCRMAPIService.updateLead(lds_id,"member_27105",crmId);
                    String accountId = jsonArray1.getJSONObject(0).getString("_id");//客户id
                    String result5=  SCRMAPIService.updateLead(lds_id,"member_27108",accountId);
                    String accountStatus = jsonArray1.getJSONObject(0).getString("deal_status");//客户成交状态
                    String accountOwner = jsonArray1.getJSONObject(0).getJSONArray("owner").get(0).toString();//客户所属bd

                    //根据客户所属人（bd）id查客户所属人name
                    String result7 = CRMAPIService.searchBdNameById(accountOwner);//
                    JSONObject jsonObject7 = JSONObject.parseObject(result7);
                    JSONArray jsonArray7 = jsonObject7.getJSONObject("data").getJSONArray("dataList");
                    String  accountOwnerName= jsonArray7.getJSONObject(0).getString("name");//线索所属人BD name


                    String crmBdField="线索状态:"+conversion+",线索所属BD:"+crmOwnerName+",客户成交状态:"+accountStatus+",客户所属BD:"+accountOwnerName;
                    //数据字典进行数据转换
                    crmBdField=crmBdField.replace("h3mB6wAoj","未分配")
                            .replace("Cj68eiU7a","未跟进")
                            .replace("2Z31f4W0O","跟进中")
                            .replace("JSTQf1blg","已转换")
                            .replace("A3qpg5ia2","已成交")
                            .replace("4NqjyM213","无效");
                    crmBdField=crmBdField.replace("1","未成交").replace("2","已成交").replace("3","多次成交");
                    String result8=  SCRMAPIService.updateLead(lds_id,"member_27110",crmBdField);


                }

//                return 1;//有重复

            }else if (jsonArray.size()>1){
                //TODO : 抛异常，正常情况下crm里不应该有两个线索
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
//        return 2;//异常
    }
    @Test
    public void checkTheLeadsEmail() {
        String result =null;
        try {
            String lds_id="17990128";
            String email ="zhoujingming@noxgroup.com";
            result=  CRMAPIService.checkTheLeadsEmail(email);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("dataList");
            if (jsonArray.size()==0){
//                return 0;//没有重复
            }else if(jsonArray.size()==1) {
                //1将致趣线索和crm老线索映射
                //1.1线索id，所属bd,线索状态（判断线索是否转化）
                String crmId = jsonArray.getJSONObject(0).getString("_id");
                String crmOwner = jsonArray.getJSONObject(0).getJSONArray("owner").get(0).toString();
                String conversion = jsonArray.getJSONObject(0).getString("field_thOe1__c");//线索状态（转化）

                //根据线索所属人id查所属人name
                String result6 = CRMAPIService.searchBdNameById(crmOwner);//
                JSONObject jsonObject6 = JSONObject.parseObject(result6);
                JSONArray jsonArray6 = jsonObject6.getJSONObject("data").getJSONArray("dataList");
                String  crmOwnerName= jsonArray6.getJSONObject(0).getString("name");//线索所属人BD name

                //1.2查询crm的线索id和bd（所属人），  修改scrm

                //2判断线索是否已转化
                //2.1查询线索转化的客户不为空则已转化，为空没有转化
                String result3 = CRMAPIService.searchCompanyById(crmId);//根据crmid获取线索的客户
                JSONObject jsonObject1 = JSONObject.parseObject(result3);
                JSONArray jsonArray1 = jsonObject1.getJSONObject("data").getJSONArray("dataList");

                if(jsonArray1.size()==0){
                    //线索没有转化

                    //将CRM的线索ID更新至致趣.CRM线索ID字段，将线索所属BD(根据id查name)更新至致趣.CRM归属BD
                    String result1=  SCRMAPIService.updateLead(lds_id,"member_27105",crmId);
                    String result2=  SCRMAPIService.updateLead(lds_id,"member_27110","线索所属BD:"+crmOwnerName);


                }else {
                    //线索已经转化
                    //将CRM的线索ID更新至致趣.CRM线索ID字段，将CRM的客户ID更新至致趣.CRM客户ID字段，
                    //将【线索状态+线索所属BD+客户成交状态+客户所属BD】 更新至致趣.CRM归属BD
                    String result4=  SCRMAPIService.updateLead(lds_id,"member_27105",crmId);
                    String accountId = jsonArray1.getJSONObject(0).getString("_id");//客户id
                    String result5=  SCRMAPIService.updateLead(lds_id,"member_27108",accountId);
                    String accountStatus = jsonArray1.getJSONObject(0).getString("deal_status");//客户成交状态
                    String accountOwner = jsonArray1.getJSONObject(0).getJSONArray("owner").get(0).toString();//客户所属bd

                    //根据客户所属人（bd）id查客户所属人name
                    String result7 = CRMAPIService.searchBdNameById(accountOwner);//
                    JSONObject jsonObject7 = JSONObject.parseObject(result7);
                    JSONArray jsonArray7 = jsonObject7.getJSONObject("data").getJSONArray("dataList");
                    String  accountOwnerName= jsonArray7.getJSONObject(0).getString("name");//线索所属人BD name


                    String crmBdField="线索状态:"+conversion+", 线索所属BD:"+crmOwnerName+", 客户成交状态:"+accountStatus+", 客户所属BD:"+accountOwnerName;
                    //数据字典进行数据转换
                    crmBdField=crmBdField.replace("h3mB6wAoj","未分配")
                            .replace("Cj68eiU7a","未跟进")
                            .replace("2Z31f4W0O","跟进中")
                            .replace("JSTQf1blg","已转换")
                            .replace("A3qpg5ia2","已成交")
                            .replace("4NqjyM213","无效");
                    crmBdField=crmBdField.replace("1","未成交").replace("2","已成交").replace("3","多次成交");
                    String result8=  SCRMAPIService.updateLead(lds_id,"member_27110",crmBdField);


                }

//                return 1;//有重复




            }else if (jsonArray.size()>1){
                //TODO : 抛异常，正常情况下crm里不应该有两个线索

            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
        //        return 2;//异常
    }

    @Test
    public void checkTheLeadsEndEmail() {
        String result =null;
        try {
            String lds_id="17990128";
            String email ="zhoujingming@noxgroup.com";
            String endEmail=StringUtils.substringAfter(email, "@");
            result=  CRMAPIService.checkTheLeadsEndEmail(endEmail);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("dataList");
            if (jsonArray.size()==0){
//                return 0;//没有重复
            }else if(jsonArray.size()==1) {
                //1将致趣线索和crm老线索映射
                //1.1线索id，所属bd,线索状态（判断线索是否转化）
                String crmId = jsonArray.getJSONObject(0).getString("_id");
                String crmOwner = jsonArray.getJSONObject(0).getJSONArray("owner").get(0).toString();
                String conversion = jsonArray.getJSONObject(0).getString("field_thOe1__c");//线索状态（转化）

                //根据线索所属人id查所属人name
                String result6 = CRMAPIService.searchBdNameById(crmOwner);//
                JSONObject jsonObject6 = JSONObject.parseObject(result6);
                JSONArray jsonArray6 = jsonObject6.getJSONObject("data").getJSONArray("dataList");
                String  crmOwnerName= jsonArray6.getJSONObject(0).getString("name");//线索所属人BD name

                //1.2查询crm的线索id和bd（所属人），  修改scrm

                //2判断线索是否已转化
                //2.1查询线索转化的客户不为空则已转化，为空没有转化
                String result3 = CRMAPIService.searchCompanyById(crmId);//根据crmid获取线索的客户
                JSONObject jsonObject1 = JSONObject.parseObject(result3);
                JSONArray jsonArray1 = jsonObject1.getJSONObject("data").getJSONArray("dataList");

                if(jsonArray1.size()==0){
                    //线索没有转化

                    //将CRM的线索ID更新至致趣.CRM线索ID字段，将线索所属BD(根据id查name)更新至致趣.CRM归属BD
                    String result1=  SCRMAPIService.updateLead(lds_id,"member_27105",crmId);
                    String result2=  SCRMAPIService.updateLead(lds_id,"member_27110","线索所属BD:"+crmOwnerName);


                }else {
                    //线索已经转化

                    //1. 创建一个新联系人

                    //2. 创建一个新商机并关联上述联系人
                    //3.致趣线索映射商机id、联系人id、客户id


                  /*  //线索已经转化
                    //将CRM的线索ID更新至致趣.CRM线索ID字段，将CRM的客户ID更新至致趣.CRM客户ID字段，
                    //将【线索状态+线索所属BD+客户成交状态+客户所属BD】 更新至致趣.CRM归属BD
                    String result4=  SCRMAPIService.updateLead(lds_id,"member_27105",crmId);
                    String accountId = jsonArray1.getJSONObject(0).getString("_id");//客户id
                    String result5=  SCRMAPIService.updateLead(lds_id,"member_27108",accountId);
                    String accountStatus = jsonArray1.getJSONObject(0).getString("deal_status");//客户成交状态
                    String accountOwner = jsonArray1.getJSONObject(0).getJSONArray("owner").get(0).toString();//客户所属bd

                    //根据客户所属人（bd）id查客户所属人name
                    String result7 = CRMAPIService.searchBdNameById(accountOwner);//
                    JSONObject jsonObject7 = JSONObject.parseObject(result7);
                    JSONArray jsonArray7 = jsonObject7.getJSONObject("data").getJSONArray("dataList");
                    String  accountOwnerName= jsonArray7.getJSONObject(0).getString("name");//线索所属人BD name


                    String crmBdField="线索状态:"+conversion+", 线索所属BD:"+crmOwnerName+", 客户成交状态:"+accountStatus+", 客户所属BD:"+accountOwnerName;
                    //数据字典进行数据转换
                    crmBdField=crmBdField.replace("h3mB6wAoj","未分配")
                            .replace("Cj68eiU7a","未跟进")
                            .replace("2Z31f4W0O","跟进中")
                            .replace("JSTQf1blg","已转换")
                            .replace("A3qpg5ia2","已成交")
                            .replace("4NqjyM213","无效");
                    crmBdField=crmBdField.replace("1","未成交").replace("2","已成交").replace("3","多次成交");
                    String result8=  SCRMAPIService.updateLead(lds_id,"member_27110",crmBdField);
*/

                }

//                return 1;//有重复




            }else if (jsonArray.size()>1){
                //TODO : 抛异常，正常情况下crm里不应该有两个线索

            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
        //        return 2;//异常
    }

    @Test
    public void checkTheLeadsCompany() {
        String result =null;
        try {
            String company="";
            result=  CRMAPIService.checkTheLeadsCompany(company);
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONObject empList = jsonObject.getJSONObject("empList");
//            String openUserId = (String) empList.get("openUserId");


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }




    @Test
    public void checkTheContactMobile() {
        String result =null;
        try {
            String lds_id="17990128";
            String phoneNumber = "19931850340";
            result=  CRMAPIService.checkTheContactMobile(phoneNumber);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("dataList");
            if (jsonArray.size()==0){
//                return 0;//没有重复
            }else if(jsonArray.size()==1) {
                //有重复联系人
                //判断联系人当前是否已购买


                //1将致趣线索和crm老线索映射
                //1.1线索id，所属bd,线索状态（判断线索是否转化）
                String crmId = jsonArray.getJSONObject(0).getString("_id");
                String crmOwner = jsonArray.getJSONObject(0).getJSONArray("owner").get(0).toString();
                String conversion = jsonArray.getJSONObject(0).getString("field_thOe1__c");//线索状态（转化）


                //根据线索所属人id查所属人name
                String result6 = CRMAPIService.searchBdNameById(crmOwner);//
                JSONObject jsonObject6 = JSONObject.parseObject(result6);
                JSONArray jsonArray6 = jsonObject6.getJSONObject("data").getJSONArray("dataList");
                String  crmOwnerName= jsonArray6.getJSONObject(0).getString("name");//线索所属人BD name

                //1.2查询crm的线索id和bd（所属人），  修改scrm

                //2判断线索是否已转化
                //2.1查询线索转化的客户不为空则已转化，为空没有转化
                String result3 = CRMAPIService.searchCompanyById(crmId);//根据crmid获取线索的客户
                JSONObject jsonObject1 = JSONObject.parseObject(result3);
                JSONArray jsonArray1 = jsonObject1.getJSONObject("data").getJSONArray("dataList");
                if(jsonArray1.size()==0){
                    //线索没有转化

                    //将CRM的线索ID更新至致趣.CRM线索ID字段，将线索所属BD(根据id查name)更新至致趣.CRM归属BD
                    String result1=  SCRMAPIService.updateLead(lds_id,"member_27105",crmId);
                    String result2=  SCRMAPIService.updateLead(lds_id,"member_27110","线索所属BD:"+crmOwnerName);


                }else {
                    //线索已经转化
                    //将CRM的线索ID更新至致趣.CRM线索ID字段，将CRM的客户ID更新至致趣.CRM客户ID字段，
                    //将【线索状态+线索所属BD+客户成交状态+客户所属BD】 更新至致趣.CRM归属BD
                    String result4=  SCRMAPIService.updateLead(lds_id,"member_27105",crmId);
                    String accountId = jsonArray1.getJSONObject(0).getString("_id");//客户id
                    String result5=  SCRMAPIService.updateLead(lds_id,"member_27108",accountId);
                    String accountStatus = jsonArray1.getJSONObject(0).getString("deal_status");//客户成交状态
                    String accountOwner = jsonArray1.getJSONObject(0).getJSONArray("owner").get(0).toString();//客户所属bd

                    //根据客户所属人（bd）id查客户所属人name
                    String result7 = CRMAPIService.searchBdNameById(accountOwner);//
                    JSONObject jsonObject7 = JSONObject.parseObject(result7);
                    JSONArray jsonArray7 = jsonObject7.getJSONObject("data").getJSONArray("dataList");
                    String  accountOwnerName= jsonArray7.getJSONObject(0).getString("name");//线索所属人BD name


                    String crmBdField="线索状态:"+conversion+",线索所属BD:"+crmOwnerName+",客户成交状态:"+accountStatus+",客户所属BD:"+accountOwnerName;
                    //数据字典进行数据转换
                    crmBdField=crmBdField.replace("h3mB6wAoj","未分配")
                            .replace("Cj68eiU7a","未跟进")
                            .replace("2Z31f4W0O","跟进中")
                            .replace("JSTQf1blg","已转换")
                            .replace("A3qpg5ia2","已成交")
                            .replace("4NqjyM213","无效");
                    crmBdField=crmBdField.replace("1","未成交").replace("2","已成交").replace("3","多次成交");
                    String result8=  SCRMAPIService.updateLead(lds_id,"member_27110",crmBdField);


                }

//                return 1;//有重复

            }else if (jsonArray.size()>1){
                //TODO : 抛异常，正常情况下crm里不应该有两个线索
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
//        return 2;//异常
    }







    @Test
    public void checkTheContactEmail() {
        String result =null;
        try {
            String params="";
            result=  CRMAPIService.checkTheContactEmail(params);
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONObject empList = jsonObject.getJSONObject("empList");
//            String openUserId = (String) empList.get("openUserId");


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }

    @Test
    public void checkTheContactEndEmail() {
        String result =null;
        try {
            String params="";
            result=  CRMAPIService.checkTheContactEndEmail(params);
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONObject empList = jsonObject.getJSONObject("empList");
//            String openUserId = (String) empList.get("openUserId");


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }



    @Test
    public void checkTheContactTel() {
        String result =null;
        try {
            String params="";
            result=  CRMAPIService.checkTheContactTel(params);
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONObject empList = jsonObject.getJSONObject("empList");
//            String openUserId = (String) empList.get("openUserId");


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }
    @Test
    public void checkTheAccountName() {
        String result =null;
        try {
            String params="";
            result=  CRMAPIService.checkTheAccountName(params);
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONObject empList = jsonObject.getJSONObject("empList");
//            String openUserId = (String) empList.get("openUserId");


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }


    @Test
    public void searchCompanyById(){
        String result =null;
        try {
            String leadsId="63216ab79283370001dcd255";
            result=  CRMAPIService.searchCompanyById(leadsId);



        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }

    @Test
    public void searchBdNameById(){
        String result =null;
        try {
            String openUserId="FSUID_DF19B419FD388B884C7B2BDD179E0149";
//            String openUserId="FSUID_DF19B419FD388B884C7B2BDD179E0149";
            result=  CRMAPIService.searchBdNameById(openUserId);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }


    @Test
    public void createLeadsObj(){
        String result =null;
        try {
            String s="{\n" +
                    "        \"address\": \"\",\n" +
                    "        \"bind_type\": \"未绑定\",\n" +
                    "        \"create_time\": \"2022-10-18 10:21\",\n" +
                    "        \"email\": \"ahyeon.cho95@gmail.com\",\n" +
                    "        \"identification\": \"\",\n" +
                    "        \"is_frozen\": \"未冻结\",\n" +
                    "        \"lds_belonger\": \"\",\n" +
                    "        \"lds_belonger_branches\": \"\",\n" +
                    "        \"lds_create_time\": \"2022-10-18 10:21\",\n" +
                    "        \"lds_creator\": \"\",\n" +
                    "        \"lds_id\": \"17989908\",\n" +
                    "        \"lds_inviter\": \"\",\n" +
                    "        \"lds_last_allot_time\": \"\",\n" +
                    "        \"lds_last_follow_time\": \"\",\n" +
                    "        \"lds_last_follow_type\": \"\",\n" +
                    "        \"lds_modify_time\": \"\",\n" +
                    "        \"lds_mql_time\": \"\",\n" +
                    "        \"lds_phase\": \"Raw leads\",\n" +
                    "        \"lds_phase_status\": \"培育中\",\n" +
                    "        \"lds_point\": 0,\n" +
                    "        \"lds_source\": \"官网注册\",\n" +
                    "        \"lds_source_pid\": \"官网\",\n" +
                    "        \"lds_sql_time\": \"\",\n" +
                    "        \"level_id\": \"会员卡名称\",\n" +
                    "        \"member_25956\": \"prgate\",\n" +
                    "        \"member_25957\": \"团队成员\",\n" +
                    "        \"member_26228\": \"其他\",\n" +
                    "        \"member_26416\": \"31244475521c956ac4411477e78fff5d\",\n" +
                    "        \"member_26417\": \"50-100\",\n" +
                    "        \"member_tags_num\": 1,\n" +
                    "        \"mobile\": \"+82 1047592346\",\n" +
                    "        \"mobile_type\": \"86\",\n" +
                    "        \"name\": \"조아연\",\n" +
                    "        \"points\": 0,\n" +
                    "        \"points_total\": 0,\n" +
                    "        \"sex\": \"未知\",\n" +
                    "        \"source\": \"API\",\n" +
                    "        \"birthday\": \"\",\n" +
                    "        \"member_wx_system_count\": \"\",\n" +
                    "        \"lds_record\": \"\"\n" +
                    "      }";
            JSONObject leads = JSONObject.parseObject(s);
            result=  CRMAPIService.createLeadsObj(leads);



        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }



    @Test
    public void allocateOwner(){
        String result =null;
        try {
            String objectIds="635242ebd6f02f00012103d2";
            String openUserId="FSUID_DF19B419FD388B884C7B2BDD179E0149";

            result=  CRMAPIService.allocateOwner(objectIds,openUserId);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }

    @Test
    public void queryOpenUserIdByName(){
        String result =null;
        try {
            String name="谢尔顿";

            result=  CRMAPIService.queryOpenUserIdByName(name);
            System.out.println(result);

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }



}
