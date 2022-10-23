package com.nox.kol.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nox.kol.constant.ErrorConstant;
import com.nox.kol.entity.User;
import com.nox.kol.exception.KOLException;
import com.nox.kol.vo.CreateVo;
import com.nox.kol.vo.QueryVo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UploadingExcel {
    private final static Logger logger = LoggerFactory.getLogger(SCRMAPIService.class);


    @Value("${crm_api_request_params_appId}")
    private String appId;

    @Value("${crm_api_request_params_appSecret}")
    private String appSecret;

    @Value("${crm_api_request_params_permanentCode}")
    private String permanentCode;

    @Value("${crm_api_request_params_mobile}")
    private String mobile;

    @Value("${crm_api_request_params_currentOpenUserId}")
    private String currentOpenUserId;

    @Autowired
    private SCRMAPIService SCRMAPIService;

    @Autowired
    private CRMAPIService CRMAPIService;

    @Autowired
    private DuplicateCheckService duplicateCheckService;

    @Autowired
    private RedisTemplate redisTemplate;


    public String uploadingLocalExcel(List<User> users) throws IOException {




        int size=users.size();
        for (User leads : users) {
            //遍历每一个本地线索
            System.out.println(leads.toString());

//            String mobile = (String) leads.get("电话");
//            //线索手机号查重
//            if(!"".equals(mobile) ) {//不为空进行查重
//                int checkTheLeadsPhoneNumber = checkTheLeadsPhoneNumber(mobile);
//                if (checkTheLeadsPhoneNumber == 1) {
//                    //线索手机号重复
//                    continue;
//                }
//            }
//            //线索邮箱查重
//            String email = (String) leads.get("邮箱");
//            if(!"".equals(email) ) {//不为空进行查重
//                int checkTheLeadsEmail = checkTheLeadsEmail(email);
//                if (checkTheLeadsEmail == 1) {
//                    //线索邮箱重复
//                    continue;
//                }
//            }
//            //线索公司名称查重
//            String company = (String) leads.get("公司名");
//            if(!"".equals(company) ) {//不为空进行查重
//                int checkTheLeadsEmail = checkTheLeadsCompany(company);
//                if (checkTheLeadsEmail == 1) {
//                    //线索公司重复
//                    continue;
//                }
//            }



            //crm 中创建新线索
            String leadsObj = createLeadsObj(leads);
            JSONObject jsonObject = JSONObject.parseObject(leadsObj);
            System.out.println(jsonObject.toString());


        }
        return "0";

    }

    /**
     * crm创建新线索到线索池
     * @param leads
     * @return
     * @throws IOException
     */
    public String createLeadsObj(User leads) throws IOException {

        //先把线索除owner使用创建线索接口插入
        //根据返回的 "dataId"插入owner
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");

        CreateVo createVo = new CreateVo();
        createVo.setCorpAccessToken(corpAccessToken);
        createVo.setCorpId(corpId);
        createVo.setCurrentOpenUserId(currentOpenUserId);

        CreateVo.DataDTO dataDTO = new CreateVo.DataDTO();
        dataDTO.setDataObjectApiName("LeadsObj");
        CreateVo.DataDTO.ObjectDataDTO objectDataDTO = new CreateVo.DataDTO.ObjectDataDTO();
        objectDataDTO.setName(leads.getName());
        objectDataDTO.setEmail(leads.getEmail());
        String day = leads.getDay();
        System.out.println(day);
        if (day!=null){

            Date date = null;
            try {
                DateFormat format1 = new SimpleDateFormat("yyyy.MM.dd");
                date = format1.parse(day.toString());
                System.out.println(date);
                long ts = date.getTime();
                day= String.valueOf(ts);
                System.out.println(day);
            } catch (ParseException e) {

                e.printStackTrace();

            }
        }

        objectDataDTO.setField_uM7hS__c(day);//日期

        String tel = leads.getTel();
        tel=tel.replace(" ","");
        if(tel.contains(".")) {
            tel = tel.substring(0, tel.indexOf("E"));
            tel = tel.replace(".", "");
        }
        objectDataDTO.setMobile(tel);
        objectDataDTO.setField_0253D__c(leads.getBd());//原BD
        objectDataDTO.setAddress(leads.getAddress());
        objectDataDTO.setField_215ot__c(leads.getTags());//备注
        objectDataDTO.setCompany(leads.getCompany());//公司
        objectDataDTO.setField_21Y8q__c(leads.getTrade());//行业
        objectDataDTO.setLeads_pool_id("634a503e455eb30001e9fc6d");
        objectDataDTO.setSource(9);
        objectDataDTO.setField_YX2nF__c("wjSd2T4L1");
        dataDTO.setObject_data(objectDataDTO);


        createVo.setData(dataDTO);

        String requestBody = JSON.toJSONString(createVo);



        int rowNum= leads.getRowNum();
        try {
            log.info("创建线索rowNum:"+rowNum);
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,requestBody );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/create")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();

            return response.body().string();
        } catch (Exception e) {
            logger.error("createLeadsObj Failed Cause By {}", e);
            throw e;
        }


    }



    public int checkTheLeadsPhoneNumber(String phoneNumber) {
        String result = null;
        try {
            result = CRMAPIService.checkTheLeadsPhoneNumber(phoneNumber);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("dataList");
            if (jsonArray.size() == 0) {
                return 0;//没有重复
            } else if (jsonArray.size() == 1) {

                return 1;//有重复

            } else if (jsonArray.size() > 1) {
                //TODO : 抛异常，正常情况下crm里不应该有两个线索
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
        return 2;//异常
    }

    public int checkTheLeadsEmail( String email) {
        String result = null;
        try {

            result = CRMAPIService.checkTheLeadsEmail(email);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("dataList");
            if (jsonArray.size() == 0) {
                return 0;//没有重复
            } else if (jsonArray.size() == 1) {
                return 1;//有重复
            } else if (jsonArray.size() > 1) {
                //TODO : 抛异常，正常情况下crm里不应该有两个线索

            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }
        System.out.println(result);
        return 2;//异常
    }


    public int checkTheLeadsCompany(String company) {
        String result =null;
        try {
            result=  CRMAPIService.checkTheLeadsCompany(company);
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("dataList");
            if (jsonArray.size() == 0) {
                return 0;//没有重复
            } else if (jsonArray.size() == 1) {
                return 1;//有重复
            } else if (jsonArray.size() > 1) {
                //TODO : 抛异常，正常情况下crm里不应该有两个线索

            }


        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return 2;//异常
    }
}
