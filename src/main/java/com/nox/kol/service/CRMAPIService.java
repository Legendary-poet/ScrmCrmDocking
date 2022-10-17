package com.nox.kol.service;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nox.kol.constant.ErrorConstant;
import com.nox.kol.exception.KOLException;
import com.nox.kol.vo.QueryVo;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
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
    public String checkTheLeadsPhoneNumber() throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        params.put("corpAccessToken", corpAccessToken);
        params.put("corpId", corpId);
        params.put("currentOpenUserId", currentOpenUserId);

        String checkMobile = "15801251936";
        String data="{\r\n    \"dataObjectApiName\": \"LeadsObj\",\r\n    \"search_query_info\": {\r\n      \"limit\": 100,\r\n      \"offset\": 0,\r\n      \"filters\": [\r\n        {\r\n          \"field_name\": \"mobile\",\r\n          \"field_values\": [\r\n            \"15801251936\"\r\n          ],\r\n          \"operator\": \"EQ\"\r\n        },\r\n        {\r\n          \"field_name\": \"life_status\",\r\n          \"field_values\": [\r\n            \"normal\"\r\n          ],\r\n          \"operator\": \"EQ\"\r\n        }\r\n      ],\r\n      \"orders\": [\r\n        {\r\n          \"fieldName\": \"create_time\",\r\n          \"isAsc\": false\r\n        }\r\n      ],\r\n      \"fieldProjection\": [\r\n        \"_id\",\r\n        \"name\",\r\n        \"mobile\",\r\n        \"email\",\r\n        \"owner\"\r\n      ]\r\n    }\r\n  }";
        data.replace("15801251936",checkMobile);
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
     * 线索查重,根据邮箱，后匹配查询线索
     * @return
     * @throws IOException
     */
    public String checkTheLeadsEndEmail() throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        params.put("corpAccessToken", corpAccessToken);
        params.put("corpId", corpId);
        params.put("currentOpenUserId", currentOpenUserId);

        String checkEmail = "noxgroup.com";//TODO : 这个参数从scrm线索里获取
        String data="{\r\n        \"dataObjectApiName\": \"LeadsObj\",\r\n        \"search_query_info\": {\r\n            \"limit\": 100,\r\n            \"offset\": 0,\r\n            \"filters\": [\r\n                {\r\n                    \"field_name\": \"email\",\r\n                    \"field_values\": [\r\n                        \"noxgroup.com\"\r\n                    ],\r\n                    \"operator\": \"ENDWITH\"\r\n                },\r\n                {\r\n                    \"field_name\": \"life_status\",\r\n                    \"field_values\": [\r\n                        \"normal\"\r\n                    ],\r\n                    \"operator\": \"EQ\"\r\n                }\r\n            ],\r\n            \"orders\": [\r\n                {\r\n                    \"fieldName\": \"create_time\",\r\n                    \"isAsc\": false\r\n                }\r\n            ],\r\n            \"fieldProjection\": [\r\n                \"_id\",\r\n                \"name\",\r\n                \"mobile\",\r\n                \"email\",\r\n                \"owner\"\r\n            ]\r\n        }\r\n    }";
        data.replace("noxgroup.com",checkEmail);
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
    public String checkTheLeadsCompany() throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        params.put("corpAccessToken", corpAccessToken);
        params.put("corpId", corpId);
        params.put("currentOpenUserId", currentOpenUserId);

        String checkCompany = "多点";//TODO : 这个参数从scrm线索里获取
        String data="{\r\n        \"dataObjectApiName\": \"LeadsObj\",\r\n        \"search_query_info\": {\r\n            \"limit\": 100,\r\n            \"offset\": 0,\r\n            \"filters\": [\r\n                {\r\n                    \"field_name\": \"name\",\r\n                    \"field_values\": [\r\n                        \"建业\"\r\n                    ],\r\n                    \"operator\": \"LIKE\"\r\n                },\r\n                {\r\n                    \"field_name\": \"life_status\",\r\n                    \"field_values\": [\r\n                        \"normal\"\r\n                    ],\r\n                    \"operator\": \"EQ\"\r\n                }\r\n            ],\r\n            \"orders\": [\r\n                {\r\n                    \"fieldName\": \"create_time\",\r\n                    \"isAsc\": false\r\n                }\r\n            ],\r\n            \"fieldProjection\": [\r\n                \"_id\",\r\n                \"name\",\r\n                \"mobile\",\r\n                \"email\",\r\n                \"owner\"\r\n            ]\r\n        }\r\n    }";
        data.replace("建业",checkCompany);
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
    public String checkTheLeadsEmail() throws IOException {
        HashMap<String, Object> params = new HashMap<>();
        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        params.put("corpAccessToken", corpAccessToken);
        params.put("corpId", corpId);
        params.put("currentOpenUserId", currentOpenUserId);

        String checkEmail = "noxgroup.com";//TODO : 这个参数从scrm线索里获取
        String data="{\r\n        \"dataObjectApiName\": \"LeadsObj\",\r\n        \"search_query_info\": {\r\n            \"limit\": 100,\r\n            \"offset\": 0,\r\n            \"filters\": [\r\n                {\r\n                    \"field_name\": \"email\",\r\n                    \"field_values\": [\r\n                        \"noxgroup.com\"\r\n                    ],\r\n                    \"operator\": \"EQ\"\r\n                },\r\n                {\r\n                    \"field_name\": \"life_status\",\r\n                    \"field_values\": [\r\n                        \"normal\"\r\n                    ],\r\n                    \"operator\": \"EQ\"\r\n                }\r\n            ],\r\n            \"orders\": [\r\n                {\r\n                    \"fieldName\": \"create_time\",\r\n                    \"isAsc\": false\r\n                }\r\n            ],\r\n            \"fieldProjection\": [\r\n                \"_id\",\r\n                \"name\",\r\n                \"mobile\",\r\n                \"email\",\r\n                \"owner\"\r\n            ]\r\n        }\r\n    }";
        data.replace("noxgroup.com",checkEmail);
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
    public String checkTheContactEmail() throws IOException {
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
        fieldValues1.add("noxgroup.com");
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
    public String checkTheContactEndEmail() throws IOException {

        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO.setFieldName("email");
        filtersDTO.setFieldValues(Collections.singletonList("noxgroup.com"));
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
     * 联系人查重,根据手机号，等值查询联系人的手机号
     * @return
     * @throws IOException
     */
    public String checkTheContactMobile() throws IOException {

        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO.setFieldName("mobile1");
        filtersDTO.setFieldValues(Collections.singletonList("+8615801251936"));
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
     * 联系人查重,根据手机号，等值查询联系人的电话
     * @return
     * @throws IOException
     */
    public String checkTheContactTel() throws IOException {

        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO.setFieldName("tel1");
        filtersDTO.setFieldValues(Collections.singletonList("+8615801251900"));
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
    public String checkTheAccountName() throws IOException {

        String corpAccessToken = (String) redisTemplate.opsForValue().get("corpAccessToken");
        String corpId = (String) redisTemplate.opsForValue().get("corpId");


        QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO filtersDTO = new QueryVo.DataDTO.SearchQueryInfoDTO.FiltersDTO();
        filtersDTO.setFieldName("name");
        filtersDTO.setFieldValues(Collections.singletonList("多点"));
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
}
