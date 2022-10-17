package com.nox.kol;

import com.alibaba.fastjson.JSONObject;
import com.nox.kol.service.CRMAPIService;
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
            result=  CRMAPIService.checkTheLeadsPhoneNumber();
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONObject empList = jsonObject.getJSONObject("empList");
//            String openUserId = (String) empList.get("openUserId");


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }


    @Test
    public void checkTheLeadsEndEmail() {
        String result =null;
        try {
            result=  CRMAPIService.checkTheLeadsEndEmail();
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONObject empList = jsonObject.getJSONObject("empList");
//            String openUserId = (String) empList.get("openUserId");


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }

    @Test
    public void checkTheLeadsCompany() {
        String result =null;
        try {
            result=  CRMAPIService.checkTheLeadsCompany();
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONObject empList = jsonObject.getJSONObject("empList");
//            String openUserId = (String) empList.get("openUserId");


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }

    @Test
    public void checkTheLeadsEmail() {
        String result =null;
        try {
            result=  CRMAPIService.checkTheLeadsEmail();
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONObject empList = jsonObject.getJSONObject("empList");
//            String openUserId = (String) empList.get("openUserId");


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }

    @Test
    public void checkTheContactEmail() {
        String result =null;
        try {
            result=  CRMAPIService.checkTheContactEmail();
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
            result=  CRMAPIService.checkTheContactEndEmail();
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
            result=  CRMAPIService.checkTheContactMobile();
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
            result=  CRMAPIService.checkTheContactTel();
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
            result=  CRMAPIService.checkTheAccountName();
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            JSONObject empList = jsonObject.getJSONObject("empList");
//            String openUserId = (String) empList.get("openUserId");


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
    }
}
