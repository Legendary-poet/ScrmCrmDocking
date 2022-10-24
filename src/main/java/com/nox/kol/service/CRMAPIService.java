package com.nox.kol.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nox.kol.constant.ErrorConstant;
import com.nox.kol.exception.KOLException;
import com.nox.kol.vo.AllocateVo;
import com.nox.kol.vo.CreateVo;
import com.nox.kol.vo.QueryVo;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class CRMAPIService {
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
    private RedisTemplate redisTemplate;

    /**
     * 获取应用级授权
     *
     * @return
     */
    public String getCorpAccessToken() throws IOException {
        HashMap<String, Object> params = new HashMap<>();

        params.put("appId", appId);
        params.put("appSecret", appSecret);
        params.put("permanentCode", permanentCode);

        try {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
            String body = JSONObject.toJSONString(params);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
            //什么意思？parse()里的字符串如果光写json就报错，但scrm里写json就不报错
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/corpAccessToken/get/V2")
                    .post(requestBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            Response execute = call.execute();
            if (execute.code() != 200) {
                throw new KOLException(ErrorConstant.ERROR_NUM_11002, ErrorConstant.ERROR_11002_ERROR);
            }
            return execute.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }
    }

    /**
     * 获取员工手机号
     *
     * @return
     */
    public String getByMobile() throws IOException {
        HashMap<String, Object> params = new HashMap<>();

        long corpAccessToken = Long.parseLong((String) redisTemplate.opsForValue().get("corpAccessToken"));
        long corpId = Long.parseLong((String) redisTemplate.opsForValue().get("corpId"));


        params.put("corpAccessToken", corpAccessToken);
        params.put("corpId", corpId);
        params.put("mobile", mobile);

        try {
            OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();
            String body = JSONObject.toJSONString(params);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), body);
            //什么意思？parse()里的字符串如果光写json就报错，但scrm里写json就不报错
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/corpAccessToken/get/V2")
                    .post(requestBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            Response execute = call.execute();
            if (execute.code() != 200) {
                throw new KOLException(ErrorConstant.ERROR_NUM_11002, ErrorConstant.ERROR_11002_ERROR);
            }
            return execute.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }
    }

    /**
     *  线索查重,根据手机号，等值查询线索（根据条件查询线索对象的列表）
     *
     * @return
     */
    public String checkTheLeadsPhoneNumber(String phoneNumber) throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        params.put("corpAccessToken", corpAccessToken);
        params.put("corpId", corpId);
        params.put("currentOpenUserId", currentOpenUserId);


        String data="{\r\n    \"dataObjectApiName\": \"LeadsObj\",\r\n    \"search_query_info\": {\r\n      \"limit\": 100,\r\n      \"offset\": 0,\r\n      \"filters\": [\r\n        {\r\n          \"field_name\": \"mobile\",\r\n          \"field_values\": [\r\n            \"替换\"\r\n          ],\r\n          \"operator\": \"EQ\"\r\n        },\r\n        {\r\n          \"field_name\": \"life_status\",\r\n          \"field_values\": [\r\n            \"normal\"\r\n          ],\r\n          \"operator\": \"EQ\"\r\n        }\r\n      ],\r\n      \"orders\": [\r\n        {\r\n          \"fieldName\": \"create_time\",\r\n          \"isAsc\": false\r\n        }\r\n      ],\r\n      \"fieldProjection\": [\r\n        \"_id\",\r\n        \"name\",\r\n        \"mobile\",\r\n        \"email\",\r\n        \"owner\",\r\n       \"field_thOe1__c\" ]\r\n    }\r\n  }";
        data=data.replace("替换",phoneNumber);
        params.put("data", "data111");
        String requestBody = JSONObject.toJSONString(params);
        String replace = requestBody.replace("\"data111\"", data);


        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,replace );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/query")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }
    }

    /**
     * 线索查重,线索邮箱查重
     * @return
     * @throws IOException
     */
    public String checkTheLeadsEmail(String email) throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        params.put("corpAccessToken", corpAccessToken);
        params.put("corpId", corpId);
        params.put("currentOpenUserId", currentOpenUserId);

        String data="{\r\n        \"dataObjectApiName\": \"LeadsObj\",\r\n        \"search_query_info\": {\r\n   \"limit\": 100,\r\n  \"offset\": 0,\r\n            \"filters\": [\r\n                {\r\n                    \"field_name\": \"email\",\r\n                    \"field_values\": [\r\n                        \"noxgroup.com\"\r\n                    ],\r\n                    \"operator\": \"EQ\"\r\n                },\r\n                {\r\n                    \"field_name\": \"life_status\",\r\n                    \"field_values\": [\r\n                        \"normal\"\r\n                    ],\r\n                    \"operator\": \"EQ\"\r\n                }\r\n            ],\r\n            \"orders\": [\r\n                {\r\n                    \"fieldName\": \"create_time\",\r\n                    \"isAsc\": false\r\n                }\r\n            ],\r\n            \"fieldProjection\": [\r\n                \"_id\",\r\n                \"name\",\r\n     \"mobile\",\r\n     \"email\",\r\n    \"owner\"\r\n \"field_thOe1__c\"  ]\r\n        }\r\n    }";
        data=data.replace("noxgroup.com",email);
        params.put("data", "data111");
        String requestBody = JSONObject.toJSONString(params);
        String replace = requestBody.replace("\"data111\"", data);


        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,replace );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/query")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }
    }

    /**
     * 联系人查重,根据手机号，等值查询联系人的手机号
     * @return
     * @throws IOException
     */
    public String checkTheContactMobile(String mobile) throws IOException {

        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO.setFieldName("mobile1");
        filtersDTO.setFieldValues(Collections.singletonList(mobile));
        filtersDTO.setOperator("EQ");
        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO1 = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO1.setFieldName("life_status");
        filtersDTO1.setFieldValues(Collections.singletonList("normal"));
        filtersDTO1.setOperator("EQ");
        List<QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO> filters = new ArrayList<>();
        filters.add(filtersDTO);
        filters.add(filtersDTO1);

        QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO ordersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO();
        ordersDTO.setFieldName("create_time");
        ordersDTO.setIsAsc(false);
        List<QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO> orders = new ArrayList<>();
        orders.add(ordersDTO);
        QueryVo.DataDTO.SearchQueryInfoDTO searchQueryInfoDTO = new QueryVo.DataDTO.SearchQueryInfoDTO();
        searchQueryInfoDTO.setFilters(filters);
        searchQueryInfoDTO.setOrders(orders);
        searchQueryInfoDTO.setLimit(100);
        searchQueryInfoDTO.setOffset(0);
        List<String> fileldProjection = new ArrayList();

        fileldProjection.add("company");
        fileldProjection.add("name");
        fileldProjection.add("tel1");
        fileldProjection.add("mobile1");
        fileldProjection.add("email");
        fileldProjection.add("owner");
        searchQueryInfoDTO.setFieldProjection(fileldProjection);



        QueryVo.DataDTO dataDTO = new QueryVo.DataDTO();
        dataDTO.setDataObjectApiName("ContactObj");
        dataDTO.setSearchQueryInfo(searchQueryInfoDTO);

        QueryVo queryVo = new QueryVo();
        queryVo.setCorpAccessToken(corpAccessToken);
        queryVo.setCorpId(corpId);
        queryVo.setCurrentOpenUserId(currentOpenUserId);
        queryVo.setData(dataDTO);

        String requestBody = JSON.toJSONString(queryVo);

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,requestBody );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/query")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }
    }





    /**
     * 线索查重,根据邮箱，后匹配查询线索
     * @return
     * @throws IOException
     */
    public String checkTheLeadsEndEmail(String endEmail) throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        params.put("corpAccessToken", corpAccessToken);
        params.put("corpId", corpId);
        params.put("currentOpenUserId", currentOpenUserId);

        String data="{\r\n        \"dataObjectApiName\": \"LeadsObj\",\r\n        \"search_query_info\": {\r\n            \"limit\": 100,\r\n            \"offset\": 0,\r\n            \"filters\": [\r\n                {\r\n                    \"field_name\": \"email\",\r\n                    \"field_values\": [\r\n                        \"noxgroup.com\"\r\n                    ],\r\n                    \"operator\": \"ENDWITH\"\r\n                },\r\n                {\r\n                    \"field_name\": \"life_status\",\r\n                    \"field_values\": [\r\n                        \"normal\"\r\n                    ],\r\n                    \"operator\": \"EQ\"\r\n                }\r\n            ],\r\n            \"orders\": [\r\n                {\r\n                    \"fieldName\": \"create_time\",\r\n                    \"isAsc\": false\r\n                }\r\n            ],\r\n            \"fieldProjection\": [\r\n                \"_id\",\r\n                \"name\",\r\n                \"mobile\",\r\n                \"email\",\r\n                \"owner\"\r\n            ]\r\n        }\r\n    }";
        data.replace("noxgroup.com",endEmail);
        params.put("data", "data111");
        String requestBody = JSONObject.toJSONString(params);
        String replace = requestBody.replace("\"data111\"", data);


        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,replace );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/query")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }
    }

    /**
     * 线索查重,根据公司名称，模糊查询线索的公司名称
     * @return
     * @throws IOException
     */
    public String checkTheLeadsCompany(String company) throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        params.put("corpAccessToken", corpAccessToken);
        params.put("corpId", corpId);
        params.put("currentOpenUserId", currentOpenUserId);

        String data="{\r\n        \"dataObjectApiName\": \"LeadsObj\",\r\n        \"search_query_info\": {\r\n            \"limit\": 100,\r\n            \"offset\": 0,\r\n            \"filters\": [\r\n                {\r\n                    \"field_name\": \"name\",\r\n                    \"field_values\": [\r\n                        \"建业\"\r\n                    ],\r\n                    \"operator\": \"LIKE\"\r\n                },\r\n                {\r\n                    \"field_name\": \"life_status\",\r\n                    \"field_values\": [\r\n                        \"normal\"\r\n                    ],\r\n                    \"operator\": \"EQ\"\r\n                }\r\n            ],\r\n            \"orders\": [\r\n                {\r\n                    \"fieldName\": \"create_time\",\r\n                    \"isAsc\": false\r\n                }\r\n            ],\r\n            \"fieldProjection\": [\r\n                \"_id\",\r\n                \"name\",\r\n                \"mobile\",\r\n                \"email\",\r\n                \"owner\"\r\n            ]\r\n        }\r\n    }";
        data.replace("建业",company);
        params.put("data", "data111");
        String requestBody = JSONObject.toJSONString(params);
        String replace = requestBody.replace("\"data111\"", data);


        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,replace );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/query")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }
    }



    /**
     * 联系人查重,邮箱查重
     * @return
     * @throws IOException
     */
    public String checkTheContactEmail(String email) throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        HashMap<String, Object> searchQueryInfo = new HashMap<>();
        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> filters1 = new HashMap<>();
        HashMap<String, Object> filters2 = new HashMap<>();
        HashMap<String, Object> order = new HashMap<>();
        ArrayList<Object> orders = new ArrayList<>();
        ArrayList<Object> fieldProjection = new ArrayList<>();
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");

        params.put("corpAccessToken", corpAccessToken);
        params.put("corpId", corpId);
        params.put("currentOpenUserId", currentOpenUserId);
        params.put("data", data);

        data.put("search_query_info",searchQueryInfo);
        data.put("dataObjectApiName","ContactObj");

        ArrayList<Object> fieldValues1 = new ArrayList<>();
        fieldValues1.add(email);
        filters1.put("field_name","email");
        filters1.put("field_values",fieldValues1);
        filters1.put("operator","EQ");
        ArrayList<Object> fieldValues2 = new ArrayList<>();
        fieldValues2.add("normal");
        filters2.put("field_name","life_status");
        filters2.put("field_values",fieldValues2);
        filters2.put("operator","EQ");
        ArrayList filters = new ArrayList();
        filters.add(filters1);
        filters.add(filters2);

        order.put("fieldName","create_time");
        order.put("isAsc","false");
        orders.add(order);
        fieldProjection.add("company");
        fieldProjection.add("name");
        fieldProjection.add("tel1");
        fieldProjection.add("mobile1");
        fieldProjection.add("email");
        fieldProjection.add("owner");

        searchQueryInfo.put("offset",0);
        searchQueryInfo.put("limit",50);
        searchQueryInfo.put("filters",filters);
        searchQueryInfo.put("orders",orders);
        searchQueryInfo.put("fieldProjection",fieldProjection);

        String requestBody = JSONObject.toJSONString(params);
        System.out.println(requestBody);


        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,requestBody );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/query")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }
    }



    /**
     * 联系人查重,根据邮箱，后匹配查询线索
     * @return
     * @throws IOException
     */
    public String checkTheContactEndEmail(String email) throws IOException {

        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO.setFieldName("email");
        String endEmail= StringUtils.substringAfter(email, "@");
        filtersDTO.setFieldValues(Collections.singletonList(endEmail));
        filtersDTO.setOperator("ENDWITH");
        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO1 = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO1.setFieldName("life_status");
        filtersDTO1.setFieldValues(Collections.singletonList("normal"));
        filtersDTO1.setOperator("EQ");
        List<QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO> filters = new ArrayList<>();
        filters.add(filtersDTO);
        filters.add(filtersDTO1);

        QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO ordersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO();
        ordersDTO.setFieldName("create_time");
        ordersDTO.setIsAsc(false);
        List<QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO> orders = new ArrayList<>();
        orders.add(ordersDTO);
        QueryVo.DataDTO.SearchQueryInfoDTO searchQueryInfoDTO = new QueryVo.DataDTO.SearchQueryInfoDTO();
        searchQueryInfoDTO.setFilters(filters);
        searchQueryInfoDTO.setOrders(orders);
        searchQueryInfoDTO.setLimit(100);
        searchQueryInfoDTO.setOffset(0);
        List<String> fileldProjection = new ArrayList();
        fileldProjection.add("company");
        fileldProjection.add("name");
        fileldProjection.add("tel1");
        fileldProjection.add("mobile1");
        fileldProjection.add("email");
        fileldProjection.add("owner");
        searchQueryInfoDTO.setFieldProjection(fileldProjection);

        QueryVo.DataDTO dataDTO = new QueryVo.DataDTO();
        dataDTO.setDataObjectApiName("ContactObj");
        dataDTO.setSearchQueryInfo(searchQueryInfoDTO);

        QueryVo queryVo = new QueryVo();
        queryVo.setCorpAccessToken(corpAccessToken);
        queryVo.setCorpId(corpId);
        queryVo.setCurrentOpenUserId(currentOpenUserId);
        queryVo.setData(dataDTO);

        String requestBody = JSON.toJSONString(queryVo);

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,requestBody );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/query")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }
    }



    /**
     * 联系人查重,根据手机号，等值查询联系人的电话
     * @return
     * @throws IOException
     */
    public String checkTheContactTel(String tel) throws IOException {

        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO.setFieldName("tel1");
        filtersDTO.setFieldValues(Collections.singletonList(tel));
        filtersDTO.setOperator("EQ");
        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO1 = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO1.setFieldName("life_status");
        filtersDTO1.setFieldValues(Collections.singletonList("normal"));
        filtersDTO1.setOperator("EQ");
        List<QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO> filters = new ArrayList<>();
        filters.add(filtersDTO);
        filters.add(filtersDTO1);

        QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO ordersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO();
        ordersDTO.setFieldName("create_time");
        ordersDTO.setIsAsc(false);
        List<QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO> orders = new ArrayList<>();
        orders.add(ordersDTO);
        QueryVo.DataDTO.SearchQueryInfoDTO searchQueryInfoDTO = new QueryVo.DataDTO.SearchQueryInfoDTO();
        searchQueryInfoDTO.setFilters(filters);
        searchQueryInfoDTO.setOrders(orders);
        searchQueryInfoDTO.setLimit(100);
        searchQueryInfoDTO.setOffset(0);
        List<String> fileldProjection = new ArrayList();
        fileldProjection.add("company");
        fileldProjection.add("name");
        fileldProjection.add("tel1");
        fileldProjection.add("mobile1");
        fileldProjection.add("email");
        fileldProjection.add("owner");
        searchQueryInfoDTO.setFieldProjection(fileldProjection);

        QueryVo.DataDTO dataDTO = new QueryVo.DataDTO();
        dataDTO.setDataObjectApiName("ContactObj");
        dataDTO.setSearchQueryInfo(searchQueryInfoDTO);

        QueryVo queryVo = new QueryVo();
        queryVo.setCorpAccessToken(corpAccessToken);
        queryVo.setCorpId(corpId);
        queryVo.setCurrentOpenUserId(currentOpenUserId);
        queryVo.setData(dataDTO);

        String requestBody = JSON.toJSONString(queryVo);

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,requestBody );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/query")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }
    }

    /**
     * 客户查重,根据公司名称，模糊查询客户的公司名称
     * @return
     * @throws IOException
     */
    public String checkTheAccountName(String name) throws IOException {

        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO.setFieldName("name");
        filtersDTO.setFieldValues(Collections.singletonList(name));
        filtersDTO.setOperator("LIKE");
        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO1 = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO1.setFieldName("life_status");
        filtersDTO1.setFieldValues(Collections.singletonList("normal"));
        filtersDTO1.setOperator("EQ");
        List<QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO> filters = new ArrayList<>();
        filters.add(filtersDTO);
        filters.add(filtersDTO1);

        QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO ordersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO();
        ordersDTO.setFieldName("create_time");
        ordersDTO.setIsAsc(false);
        List<QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO> orders = new ArrayList<>();
        orders.add(ordersDTO);
        QueryVo.DataDTO.SearchQueryInfoDTO searchQueryInfoDTO = new QueryVo.DataDTO.SearchQueryInfoDTO();
        searchQueryInfoDTO.setFilters(filters);
        searchQueryInfoDTO.setOrders(orders);
        searchQueryInfoDTO.setLimit(100);
        searchQueryInfoDTO.setOffset(0);
        List<String> fileldProjection = new ArrayList();
        fileldProjection.add("_id");
        fileldProjection.add("name");
        fileldProjection.add("field_IGd20__c");
        fileldProjection.add("owner");
        searchQueryInfoDTO.setFieldProjection(fileldProjection);

        QueryVo.DataDTO dataDTO = new QueryVo.DataDTO();
        dataDTO.setDataObjectApiName("AccountObj");
        dataDTO.setSearchQueryInfo(searchQueryInfoDTO);

        QueryVo queryVo = new QueryVo();
        queryVo.setCorpAccessToken(corpAccessToken);
        queryVo.setCorpId(corpId);
        queryVo.setCurrentOpenUserId(currentOpenUserId);
        queryVo.setData(dataDTO);

        String requestBody = JSON.toJSONString(queryVo);

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,requestBody );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/query")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }
    }

    /**
     * 查询线索转化的客户,判断线索是否转化（没转化就是查不到）
     * @return
     * @throws IOException
     */
    public String searchCompanyById(String leadsId) throws IOException {

        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO.setFieldName("leads_id");
        filtersDTO.setFieldValues(Collections.singletonList(leadsId));
        filtersDTO.setOperator("LIKE");
        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO1 = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO1.setFieldName("life_status");
        filtersDTO1.setFieldValues(Collections.singletonList("normal"));
        filtersDTO1.setOperator("EQ");
        List<QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO> filters = new ArrayList<>();
        filters.add(filtersDTO);
        filters.add(filtersDTO1);

        QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO ordersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO();
        ordersDTO.setFieldName("create_time");
        ordersDTO.setIsAsc(false);
        List<QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO> orders = new ArrayList<>();
        orders.add(ordersDTO);
        QueryVo.DataDTO.SearchQueryInfoDTO searchQueryInfoDTO = new QueryVo.DataDTO.SearchQueryInfoDTO();
        searchQueryInfoDTO.setFilters(filters);
        searchQueryInfoDTO.setOrders(orders);
        searchQueryInfoDTO.setLimit(100);
        searchQueryInfoDTO.setOffset(0);
        List<String> fileldProjection = new ArrayList();
        fileldProjection.add("_id");
        fileldProjection.add("name");
        fileldProjection.add("deal_status");
        fileldProjection.add("owner");
        searchQueryInfoDTO.setFieldProjection(fileldProjection);

        QueryVo.DataDTO dataDTO = new QueryVo.DataDTO();
        dataDTO.setDataObjectApiName("AccountObj");
        dataDTO.setSearchQueryInfo(searchQueryInfoDTO);

        QueryVo queryVo = new QueryVo();
        queryVo.setCorpAccessToken(corpAccessToken);
        queryVo.setCorpId(corpId);
        queryVo.setCurrentOpenUserId(currentOpenUserId);
        queryVo.setData(dataDTO);

        String requestBody = JSON.toJSONString(queryVo);

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,requestBody );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/query")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }
    }



    /**
     * 根据openUserId查询员工信息,实现根据所属人id查所属人（bd）的name
     * @return
     * @throws IOException
     */
    public String searchBdNameById(String openUserId) throws IOException {
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO.setFieldName("user_id");
        filtersDTO.setFieldValues(Collections.singletonList(openUserId));
        filtersDTO.setOperator("LIKE");
        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO1 = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO1.setFieldName("life_status");
        filtersDTO1.setFieldValues(Collections.singletonList("normal"));
        filtersDTO1.setOperator("EQ");
        List<QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO> filters = new ArrayList<>();
        filters.add(filtersDTO);
        filters.add(filtersDTO1);

        QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO ordersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO();
        ordersDTO.setFieldName("create_time");
        ordersDTO.setIsAsc(false);
        List<QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO> orders = new ArrayList<>();
        orders.add(ordersDTO);
        QueryVo.DataDTO.SearchQueryInfoDTO searchQueryInfoDTO = new QueryVo.DataDTO.SearchQueryInfoDTO();
        searchQueryInfoDTO.setFilters(filters);
        searchQueryInfoDTO.setOrders(orders);
        searchQueryInfoDTO.setLimit(2);
        searchQueryInfoDTO.setOffset(0);
        List<String> fileldProjection = new ArrayList();
        fileldProjection.add("_id");
        fileldProjection.add("name");
        searchQueryInfoDTO.setFieldProjection(fileldProjection);

        QueryVo.DataDTO dataDTO = new QueryVo.DataDTO();
        dataDTO.setDataObjectApiName("PersonnelObj");
        dataDTO.setSearchQueryInfo(searchQueryInfoDTO);

        QueryVo queryVo = new QueryVo();
        queryVo.setCorpAccessToken(corpAccessToken);
        queryVo.setCorpId(corpId);
        queryVo.setCurrentOpenUserId("FSUID_DF19B419FD388B884C7B2BDD179E0149");
        queryVo.setData(dataDTO);

        String requestBody = JSON.toJSONString(queryVo);

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,requestBody );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/query")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }

    }

    /**
     * 新建线索对象
     * @param
     * @return
     * @throws IOException
     */
    public String createLeadsObj(JSONObject leads,String openUserId) throws Exception {
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
        objectDataDTO.setName((String) leads.get("name"));
        objectDataDTO.setField_aEh03__c((String) leads.get("sex"));
        String mobile = (String) leads.get("mobile");
        if(mobile.contains("+")&&mobile.contains(" ")){
            String[] s = mobile.split(" ");
            objectDataDTO.setMobile_pre__c(s[0]);
            mobile=s[1];
        }
        objectDataDTO.setMobile(mobile);
        objectDataDTO.setEmail((String) leads.get("email"));
        objectDataDTO.setAddress((String) leads.get("address"));
        objectDataDTO.setCompany((String) leads.get("member_25956"));
        objectDataDTO.setJob_title((String) leads.get("member_25957"));
        objectDataDTO.setDepartment((String) leads.get(""));
        objectDataDTO.setRemark((String) leads.get("member_26227"));

        objectDataDTO.setField_21Y8q__c((String) leads.get("member_26228"));
        objectDataDTO.setField_m1ubR__c(String.valueOf(leads.get("points")));//剩余积分
        if(openUserId!=null){
            objectDataDTO.setOwner(Collections.singletonList(openUserId));
        }else {
            objectDataDTO.setLeads_pool_id("634a503e455eb30001e9fc6d");
        }


        String source = leads.get("lds_source_pid").toString();//线索一级来源
        source = source
                .replace("直接注册", "2")
                .replace("线上直播", "3")
                .replace("公号投放", "4")
                .replace("线下活动", "5")
                .replace("官网", "6")
                .replace("投放", "4")
                .replace("官微咨询", "7")
                .replace("邮件咨询", "8");
//        objectDataDTO.setSource();//lds_source_pid
        objectDataDTO.setField_g26sD__c((String) leads.get("lds_source"));
        //todo 调用获取会员标签接口
        objectDataDTO.setField_215ot__c((String) leads.get(""));//标签
        objectDataDTO.setField_Rr4xw__c((String) leads.get(""));//sdr
        objectDataDTO.setField_G6s6C__c((String) leads.get("member_26416"));
        objectDataDTO.setField_35AB2__c((String) leads.get("lds_id"));//


        dataDTO.setObject_data(objectDataDTO);
        createVo.setData(dataDTO);


        String requestBody = JSON.toJSONString(createVo);

        String dataId;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestBody);
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/create")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(result);
            dataId = (String) jsonObject.get("dataId");


        } catch (Exception e) {
            logger.error("新线索创建失败", e);
            throw e;
        }
        return dataId;

    }


    /**
     * 创建联系人对象
     * @return
     * @throws IOException
     */
    public String createContactObj(String openUserId) throws IOException {
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO.setFieldName("user_id");
        filtersDTO.setFieldValues(Collections.singletonList(openUserId));
        filtersDTO.setOperator("LIKE");
        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO1 = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO1.setFieldName("life_status");
        filtersDTO1.setFieldValues(Collections.singletonList("normal"));
        filtersDTO1.setOperator("EQ");
        List<QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO> filters = new ArrayList<>();
        filters.add(filtersDTO);
        filters.add(filtersDTO1);

        QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO ordersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO();
        ordersDTO.setFieldName("create_time");
        ordersDTO.setIsAsc(false);
        List<QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO> orders = new ArrayList<>();
        orders.add(ordersDTO);
        QueryVo.DataDTO.SearchQueryInfoDTO searchQueryInfoDTO = new QueryVo.DataDTO.SearchQueryInfoDTO();
        searchQueryInfoDTO.setFilters(filters);
        searchQueryInfoDTO.setOrders(orders);
        searchQueryInfoDTO.setLimit(2);
        searchQueryInfoDTO.setOffset(0);
        List<String> fileldProjection = new ArrayList();
        fileldProjection.add("_id");
        fileldProjection.add("name");
        searchQueryInfoDTO.setFieldProjection(fileldProjection);

        QueryVo.DataDTO dataDTO = new QueryVo.DataDTO();
        dataDTO.setDataObjectApiName("PersonnelObj");
        dataDTO.setSearchQueryInfo(searchQueryInfoDTO);

        QueryVo queryVo = new QueryVo();
        queryVo.setCorpAccessToken(corpAccessToken);
        queryVo.setCorpId(corpId);
        queryVo.setCurrentOpenUserId(currentOpenUserId);
        queryVo.setData(dataDTO);

        String requestBody = JSON.toJSONString(queryVo);

        try {
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
            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }

    }



    /**
     * 分配线索
     * @return
     * @throws IOException
     */
    public String allocateOwner(String objectIds,String ownerOpenUserId) throws IOException {
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        AllocateVo.DataDTO dataDTO = new AllocateVo.DataDTO();
        dataDTO.setApiName("LeadsObj");
        dataDTO.setObjectIds(Collections.singletonList(objectIds));
        dataDTO.setObjectPoolId("634a503e455eb30001e9fc6d");
        dataDTO.setOwnerOpenUserId(ownerOpenUserId);

        AllocateVo allocateVo = new AllocateVo();
        allocateVo.setData(dataDTO);
        allocateVo.setCorpAccessToken(corpAccessToken);
        allocateVo.setCorpId(corpId);
        allocateVo.setCurrentOpenUserId(currentOpenUserId);


        String requestBody = JSON.toJSONString(allocateVo);

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType,requestBody );
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/allocate")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (Exception e) {
//            logger.error("requestLeadList Failed Cause By {}", e);
            throw e;
        }

    }


    public String queryOpenUserIdByName(String name) throws IOException {

        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO.setFieldName("name");
        filtersDTO.setFieldValues(Collections.singletonList(name));
        filtersDTO.setOperator("EQ");
        List<QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO> filters = new ArrayList<>();
        filters.add(filtersDTO);

        QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO ordersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO();
        ordersDTO.setFieldName("create_time");
        ordersDTO.setIsAsc(false);
        List<QueryVo.DataDTO.SearchQueryInfoDTO.OrdersDTO> orders = new ArrayList<>();
        orders.add(ordersDTO);
        QueryVo.DataDTO.SearchQueryInfoDTO searchQueryInfoDTO = new QueryVo.DataDTO.SearchQueryInfoDTO();
        searchQueryInfoDTO.setFilters(filters);
        searchQueryInfoDTO.setOrders(orders);
        searchQueryInfoDTO.setLimit(100);
        searchQueryInfoDTO.setOffset(0);
        List<String> fileldProjection = new ArrayList();
        fileldProjection.add("user_id");
        searchQueryInfoDTO.setFieldProjection(fileldProjection);

        QueryVo.DataDTO dataDTO = new QueryVo.DataDTO();
        dataDTO.setDataObjectApiName("PersonnelObj");
        dataDTO.setSearchQueryInfo(searchQueryInfoDTO);

        QueryVo queryVo = new QueryVo();
        queryVo.setCorpAccessToken(corpAccessToken);
        queryVo.setCorpId(corpId);
        queryVo.setCurrentOpenUserId("FSUID_DF19B419FD388B884C7B2BDD179E0149");
        queryVo.setData(dataDTO);

        String requestBody = JSON.toJSONString(queryVo);

        String userId = null;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, requestBody);
            Request request = new Request.Builder()
                    .url("https://open.fxiaoke.com/cgi/crm/v2/data/query")
                    .method("POST", body)
                    .addHeader("Content-Type", "application/json")
                    .build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            JSONObject jsonObject = JSONObject.parseObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray dataList = data.getJSONArray("dataList");
            JSONObject jsonObject1 = dataList.getJSONObject(0);
            userId = (String) jsonObject1.get("user_id");
            return userId;
        } catch (Exception e) {
            log.error("queryOpenUserIdByName 失败(可能是crm没有录入所属人，如孙文鑫) {}", e);

        }
        return userId;
    }

}
