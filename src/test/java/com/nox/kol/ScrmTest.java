package com.nox.kol;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nox.kol.service.SCRMAPIService;
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

@SpringBootTest(classes = ScrmCrmDockingApplication.class)
@EnableAsync
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ScrmTest {

    @Autowired
    private SCRMAPIService SCRMAPIService;

    @Test
    public void testRedis(){
        redisTemplate.opsForValue().set("latestLeadid","17868261");
    }

    @Test
    public void requestAllLeadList() throws IOException {
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
                    System.out.println(li);
                    System.out.println(l);
                    if (li == l) {
                        flag=1;
                        break;
                    }
                    //TODO : 存储线索
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
        System.out.println(arrayList);
        System.out.println("测试");

        if(arrayList.size()!=0){
            //有新线索
            for (JSONObject leads : arrayList) {
                String mobile = (String) leads.get("mobile");
                String email = (String) leads.get("email");
                //线索手机号查重


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

    @Test
    public void updateLead() {
        String result =null;
        try {
            result=  SCRMAPIService.updateLead();
//            JSONObject jsonObject = JSON.parseObject(result);
//            JSONObject data = jsonObject.getJSONObject("data");
//            JSONArray list = data.getJSONArray("list");

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }

}
