package com.nox.kol;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nox.kol.service.CRMAPIService;
import com.nox.kol.service.DuplicateCheckService;
import com.nox.kol.service.SCRMAPIService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

@SpringBootTest(classes = ScrmCrmDockingApplication.class)
@EnableAsync
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@Slf4j
public class ScrmTest {

    @Autowired
    private SCRMAPIService SCRMAPIService;

    @Autowired
    private CRMAPIService CRMAPIService;

    @Autowired
    private DuplicateCheckService duplicateCheckService;

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("latestLeadid","18084938");
    }

    @Test
    public void requestAllLeadListToExcel() throws IOException {
        String result =null;
        int total;
        //查询total
        try {
            result=  SCRMAPIService.requestLeadListPage(1);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            Object t = data.get("total");
            total= (int) t;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File f=new File("D:\\d.txt");
        FileOutputStream fos1=new FileOutputStream(f);
        OutputStreamWriter dos1=new OutputStreamWriter(fos1);
        //查询现有所有线索
        int totalPage=total/100+(total%100!=0?1: 0);
        for(int i=1;i<=totalPage;i++) {
            try {

                result = SCRMAPIService.requestLeadListPage(i);//i为页数
                JSONObject jsonObject = JSONObject.parseObject(result);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray list = data.getJSONArray("data");



                for(int j=0;j<list.size();j++) {
                    JSONObject item = list.getJSONObject(j);
//                    System.out.println(item.toString());
                    String res=item.toString();
                    res = res
                            .replace("{", "")
                            .replace("}", "")
                            .replace(":", ",")
                            .replace("\"", "");
                    System.out.println(res);
                    dos1.write(res);
                    dos1.write("\n");
                }


            } catch (Exception e) {
                System.out.println(e.toString());
            }
//            System.out.println(result);
        }
        dos1.close();
        fos1.close();
//        System.out.println(result);
    }


    @Test
    public void requestLeadList() {
        String result =null;
        try {
            result=  SCRMAPIService.requestLeadList();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }


    @Test
    public void requestLead() {
        String result =null;
        try {
            result=  SCRMAPIService.requestLead();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }



   /* *//**
     * 获得最新那一条线索的id(废弃)
     *//*
    @Test
    public long getLatestLeadid(){
        String result =null;
        long id;
        try {
            result=  SCRMAPIService.requestLeadList();
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray list = data.getJSONArray("data");
            JSONObject object = (JSONObject) list.get(0);
            String lds_id = (String) object.get("lds_id");
            id = Long.parseLong(lds_id);
            System.out.println(l);
            return id;

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
*/
    @Autowired
    private RedisTemplate redisTemplate;



//    private long l=17868261;//最新的线索id
    @Test
    public void requestNewLeadList() {
        String result =null;//存储返回的json字符串
        int flag=0;//标志位 如果查到lds_id就变1
        int flag2=0;//查询最新id的标志位
        ArrayList<JSONObject> arrayList = new ArrayList();//存储最新数据
        long l = Long.parseLong((String) redisTemplate.opsForValue().get("latestLeadid"));
        int page=1;
//        外层循环请求HTTP
//        内层循环一次请求的list。
//        设置标志位0。
//        如果id小于了就变1
//        break退出查询。

        while(flag==0) {
            try {
                result = SCRMAPIService.requestLeadListPage(page);
                page++;
                JSONObject jsonObject = JSONObject.parseObject(result);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray list = data.getJSONArray("data");


                int size = list.size();
                for (Object o : list) {
                    JSONObject objecti = (JSONObject) o;
                    String lds_idi = (String) objecti.get("lds_id");
                    long li = Long.parseLong(lds_idi);//遍历list里每一个线索的id

//                    long l = Long.parseLong((String) redisTemplate.opsForValue().get("latestLeadid"));
//                    System.out.println("当前线索ID:"+li);
//                    System.out.println("Redis线索ID:"+l);
                    if (li == l) {
                        flag=1;
                        break;
                    }
                    // 存储线索
                    arrayList.add(objecti);
                }

                if(flag2==0) {
                    JSONObject object = (JSONObject) list.get(0);
                    String lds_id = (String) object.get("lds_id");
//                    long l = Long.parseLong(lds_id);
                    redisTemplate.opsForValue().set("latestLeadid",lds_id);
                    flag2=1;
//                    System.out.println(l);

                }

            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        Collections.reverse(arrayList);
//        System.out.println(arrayList);
//        System.out.println("测试");

        if(arrayList.size()!=0){
            //有新线索
            int ver=0;//计数
            for (JSONObject leads : arrayList) {
//                if(ver==6){
//                    break;
//                }
                String lds_id = (String) leads.get("lds_id");
                System.out.println("当前线索id:"+lds_id+",为第"+ver+"个线索");
                ver++;
                String mobile = (String) leads.get("mobile");
                String email = (String) leads.get("email");
                String lds_belonger = (String) leads.get("lds_belonger");
                try{
                    //线索手机号查重
                    if (!"".equals(mobile)&&mobile.length()>5) {//不为空进行查重
                        if(mobile.contains("+")&&mobile.contains(" ")) {
                            String[] s = mobile.split(" ");
                            mobile=s[1];
                        }
                        int checkTheLeadsPhoneNumber = duplicateCheckService.checkTheLeadsPhoneNumber(lds_id, mobile);
                        System.out.println("手机号查重"+checkTheLeadsPhoneNumber);
                        if (checkTheLeadsPhoneNumber == 1) {
                            //线索手机号重复
                            continue;
                        }
                    }
                    //线索邮箱查重
                    if(!"".equals(email)) {
                        int checkTheLeadsEmail = duplicateCheckService.checkTheLeadsEmail(lds_id, email);
                        System.out.println("邮箱查重"+checkTheLeadsEmail);
                        if (checkTheLeadsEmail == 1) {
                            //线索邮箱重复
                            continue;
                        }
                    }
                    //联系人手机号查重
                    //联系人电话查重
                    //联系人邮箱查重
                    //客户名称查重
                    //联系人邮箱域名查重
                    //线索邮箱域名查重
                    //线索公司名称查重
                }catch(Exception e){
                    log.error("线索查重异常" + lds_id);
                    System.out.println("线索查重异常" + lds_id);
                }



                //根据lds_belonger查openUserId
                String openUserId = null;
                System.out.println(lds_belonger);
                System.out.println(lds_belonger.equals(""));
                if(!lds_belonger.equals("")){
                    try {
                        openUserId = CRMAPIService.queryOpenUserIdByName(lds_belonger);
                    } catch (IOException e) {
                        log.error("根据lds_belonger查openUserId失败"+e);
                        System.out.println("根据lds_belonger查openUserId失败");
                    }

                }

                //2.1 创建一个新线索
                String objectIds=null;
                try {
                    objectIds = CRMAPIService.createLeadsObj(leads,openUserId);
                } catch (Exception e) {
                    log.error("创建新线索 " + leads.get("lds_id") + " 失败");
                    System.out.println("创建新线索 " + leads.get("lds_id") + " 失败");
                }


                ;
//                //2.2 给leads分配owner
//                try {
//                    result = CRMAPIService.allocateOwner(objectIds, openUserId);
//                } catch (IOException e) {
//                    log.error("线索{}分配owner失败:{}",objectIds,e);
//
//                }

                //2.3 致趣线索映射到crm新线索
                try {
                    String result1 = SCRMAPIService.updateLead(lds_id, "member_27105", objectIds);
                } catch (Exception e) {
                    log.error("致趣线索映射到crm新线索失败"+e);
                    System.out.println("致趣线索映射到crm新线索失败");
                }


            }

        }

    }
    @Test
    public void requestLeadListTime() {
        String result =null;
        try {
            result=  SCRMAPIService.requestLeadListTime();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }

//    @Test
//    public void updateLead() {
//        String result =null;
//        try {
//            result=  SCRMAPIService.updateLead();
////            JSONObject jsonObject = JSON.parseObject(result);
////            JSONObject data = jsonObject.getJSONObject("data");
////            JSONArray list = data.getJSONArray("list");
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//        System.out.println(result);
//    }


    @Test
    public void requestAllLeadList() throws IOException {
        String result =null;
        int total;
        int count1=0;
        int count2=0;
        int count3=0;
        int count4=0;
        int count5=0;
        int count6=0;
        int count7=0;
        int count8=0;
        int count9=0;
        //查询total
        try {
            result=  SCRMAPIService.requestLeadListPage(1);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            Object t = data.get("total");
            total= (int) t;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //查询现有所有线索
        int totalPage=total/100+(total%100!=0?1: 0);
        for(int i=1;i<=totalPage;i++) {
            try {

                result = SCRMAPIService.requestLeadListPage(i);//i为页数
                JSONObject jsonObject = JSONObject.parseObject(result);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray list = data.getJSONArray("data");



                for(int j=0;j<list.size();j++) {
                    JSONObject leads = list.getJSONObject(j);
                    System.out.println("第"+j);
                    String lds_id = (String) leads.get("lds_id");
                    String mobile = (String) leads.get("mobile");
                    String email = (String) leads.get("email");
                    String company = (String) leads.get("member_25956");
                    String lds_belonger = (String) leads.get("lds_belonger");

                    //1线索手机号查重
                    if (!"".equals(mobile)) {//不为空进行查重
                        int checkTheLeadsPhoneNumber = duplicateCheckService.checkTheLeadsPhoneNumber(lds_id, mobile);
                        if (checkTheLeadsPhoneNumber >= 1) {
                            //线索手机号重复
                            count1++;
                            System.out.println("count1"+count1);
                            continue;
                        }
                    }
                    //1线索邮箱查重
                    int checkTheLeadsEmail = duplicateCheckService.checkTheLeadsEmail(lds_id, email);
                    if (checkTheLeadsEmail == 1) {
                        //线索邮箱重复
                        count2++;
                        System.out.println("count1"+count1);
                        continue;
                    }
                    //1联系人手机号查重
                    String result3=  CRMAPIService.checkTheContactMobile(mobile);
                    JSONObject jsonObject3 = JSONObject.parseObject(result3);
                    JSONArray jsonArray3 = jsonObject3.getJSONObject("data").getJSONArray("dataList");
                    if (jsonArray3.size()==0){
                        //没有重复
                    }else if(jsonArray3.size()>=1) {
                        //有重复
                        count3++;
                        System.out.println("count1"+count1);
                        continue;
                    }
                    //1联系人电话查重
                    String result4=  CRMAPIService.checkTheContactTel(mobile);
                    JSONObject jsonObject4 = JSONObject.parseObject(result4);
                    JSONArray jsonArray4 = jsonObject4.getJSONObject("data").getJSONArray("dataList");
                    if (jsonArray4.size()==0){
                        //没有重复
                    }else if(jsonArray4.size()>=1) {
                        //有重复
                        count4++;
                        System.out.println("count4"+count4);
                        continue;
                    }
                    //1联系人邮箱查重
                    String result5=  CRMAPIService.checkTheContactEmail(email);
                    JSONObject jsonObject5 = JSONObject.parseObject(result5);
                    JSONArray jsonArray5 = jsonObject5.getJSONObject("data").getJSONArray("dataList");
                    if (jsonArray5.size()==0){
                        //没有重复
                    }else if(jsonArray5.size()>=1) {
                        //有重复
                        count5++;
                        System.out.println("count5"+count5);
                        continue;
                    }
                    //1客户名称查重
                    String result6=  CRMAPIService.checkTheAccountName(company);
                    JSONObject jsonObject6 = JSONObject.parseObject(result6);
                    JSONArray jsonArray6 = jsonObject6.getJSONObject("data").getJSONArray("dataList");
                    if (jsonArray6.size()==0){
                        //没有重复
                    }else if(jsonArray6.size()>=1) {
                        //有重复
                        count6++;
                        System.out.println("count6"+count6);
                        continue;
                    }
                    //1联系人邮箱域名查重
                    String endEmail= StringUtils.substringAfter(email, "@");
                    System.out.println(endEmail);
                    if(!("outlook.com".equals(endEmail)||("gmail.com".equals(endEmail))||("qq.com".equals(endEmail))||"163.com".equals(endEmail)||"126.com".equals(endEmail))){
                        String result7=  CRMAPIService.checkTheContactEndEmail(endEmail);
                        JSONObject jsonObject7 = JSONObject.parseObject(result7);
                        JSONArray jsonArray7 = jsonObject7.getJSONObject("data").getJSONArray("dataList");
                        if (jsonArray7.size()==0){
                            //没有重复
                        }else if(jsonArray7.size()>=1) {
                            //有重复
                            count7++;
                            System.out.println("count7"+count7);
                            continue;
                        }
                        //1线索邮箱域名查重
                        String result8=  CRMAPIService.checkTheLeadsEndEmail(endEmail);
                        JSONObject jsonObject8 = JSONObject.parseObject(result8);
                        JSONArray jsonArray8 = jsonObject8.getJSONObject("data").getJSONArray("dataList");
                        if (jsonArray8.size()==0){
                            //没有重复
                        }else if(jsonArray8.size()>=1) {
                            //有重复
                            count8++;
                            System.out.println("count8"+count8);
                            continue;
                        }

                    }

                    //1线索公司名称查重
                    String result9=  CRMAPIService.checkTheLeadsCompany(company);
                    JSONObject jsonObject9 = JSONObject.parseObject(result9);
                    JSONArray jsonArray9 = jsonObject9.getJSONObject("data").getJSONArray("dataList");
                    if (jsonArray9.size()==0){
                        //没有重复
                    }else if(jsonArray9.size()>=1) {
                        //有重复
                        count9++;
                        System.out.println("count9"+count9);
                        continue;
                    }



                }


            } catch (Exception e) {
                System.out.println(e.toString());
            }
//            System.out.println(result);
        }

        System.out.println(count1);
        System.out.println(count2);
        System.out.println(count3);
        System.out.println(count4);
        System.out.println(count5);
        System.out.println(count6);
        System.out.println(count7);
        System.out.println(count8);
        System.out.println(count9);
    }


//
//    @Test
//    public void queById() throws IOException {
//        String result =null;
//        try {
//            result=  SCRMAPIService.queById();
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONObject data = jsonObject.getJSONObject("data");
//            Object t = data.get("total");
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//
//    }


    @Test
    public void requestLeadsFollowUpRecords() throws IOException {
        String result =null;
        try {
            String mobile ="+82 01034108223";
            result=  SCRMAPIService.requestLeadsFollowUpRecords(mobile);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray list = data.getJSONArray("list");
            JSONObject jsonObject1 = list.getJSONObject(0);



        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    @Test
    public void updateFollowUpRecords() {
        String result =null;//存储返回的json字符串
        int flag=0;//标志位 如果查到lds_id就变1
        int flag2=0;//查询最新id的标志位
        ArrayList<JSONObject> arrayList = new ArrayList();//存储最新数据
        long l = Long.parseLong((String) redisTemplate.opsForValue().get("latestLeadid"));
        int page=1;

        while(flag==0) {
            try {
                result = SCRMAPIService.requestLeadListPage(page);
                page++;
                JSONObject jsonObject = JSONObject.parseObject(result);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONArray list = data.getJSONArray("data");


                int size = list.size();
                for (Object o : list) {
                    JSONObject objecti = (JSONObject) o;
                    String lds_idi = (String) objecti.get("lds_id");
                    long li = Long.parseLong(lds_idi);//遍历list里每一个线索的id

//                    long l = Long.parseLong((String) redisTemplate.opsForValue().get("latestLeadid"));
//                    System.out.println("当前线索ID:"+li);
//                    System.out.println("Redis线索ID:"+l);
                    if (li == l) {
                        flag=1;
                        break;
                    }
                    // 存储线索
                    arrayList.add(objecti);
                }

                if(flag2==0) {
                    JSONObject object = (JSONObject) list.get(0);
                    String lds_id = (String) object.get("lds_id");
//                    long l = Long.parseLong(lds_id);
                    redisTemplate.opsForValue().set("latestLeadid",lds_id);
                    flag2=1;
//                    System.out.println(l);

                }

            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        Collections.reverse(arrayList);
//        System.out.println(arrayList);
//        System.out.println("测试");

        if(arrayList.size()!=0){
            //有新线索
            int ver=0;//计数
            for (JSONObject leads : arrayList) {

                String lds_id = (String) leads.get("lds_id");
                System.out.println("当前线索id:" + lds_id + ",为第" + ver + "个线索");
                log.info("当前线索id:" + lds_id);
                ver++;
                if (!leads.containsKey("mobile")) {
                    continue;
                }
                String mobile = (String) leads.get("mobile");
                if (!leads.containsKey("member_27105")) {
                    continue;
                }
                String CrmId = (String) leads.get("member_27105");//crm线索id
                String records = null;
                try {

                    String result3 = SCRMAPIService.requestLeadsFollowUpRecords(mobile);
                    JSONObject jsonObject3 = JSONObject.parseObject(result3);
                    JSONObject data3 = jsonObject3.getJSONObject("data");
                    JSONArray list3 = data3.getJSONArray("list");
                    JSONObject jsonObject4 = list3.getJSONObject(0);
//                    String leads_stage = (String) jsonObject4.get("leads_stage");
//                    String leads_status = (String) jsonObject4.get("leads_status");
//                    String owner_name = (String) jsonObject4.get("owner_name");
//                    String owner_department = (String) jsonObject4.get("owner_department");
                    String remake = (String) jsonObject4.get("remake");
//                    String describe = (String) jsonObject4.get("describe");
//                    String create_at = (String) jsonObject4.get("create_at");
                    if(remake==null){
                        log.error("remake is null");
                    }

//                    records = "线索阶段:" + leads_stage + ",记录:" + describe + ",线索状态:" + leads_status + ",备注:" + remake + ",所属人:" + owner_name + ",所属部门" + owner_department;
                    records = "备注:" + remake ;


                } catch (Exception e) {
                    log.error("查询跟进记录异常" + lds_id);
                    System.out.println("查询跟进记录异常" + lds_id);
                    e.printStackTrace();
                }

                try {
                    String s = CRMAPIService.updateLeads(CrmId, records);
                } catch (Exception e) {
                    log.error("字段更新异常" + lds_id);
                    System.out.println("字段更新异常" + lds_id);
                    e.printStackTrace();
                }


            }

        }

    }

    @Test
    public void test111() {
        try {
            String s = CRMAPIService.updateLeads("6357df43b094b6000136fc77", "测试成功");
        } catch (Exception e) {
            log.error("字段更新异常");
            System.out.println("字段更新异常");
            e.printStackTrace();
        }
    }


}
