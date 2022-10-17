package com.nox.kol.service;

import com.alibaba.fastjson.JSONObject;
import com.nox.kol.constant.ErrorConstant;
import com.nox.kol.exception.KOLException;
import com.nox.kol.utils.MD5Utils;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;

@Service
public class SCRMAPIService {
    private final static Logger logger = LoggerFactory.getLogger(SCRMAPIService.class);

    @Value("${zhiqu_api_request_params_main_id}")
    private String mainId;

    @Value("${zhiqu_api_request_params_source}")
    private String source;

    @Value("${zhiqu_api_request_params_secret}")
    private String secret;

    @Value("${zhiqu_api_request_params_appId}")
    private String appId;

    @Value("${zhiqu_api_request_params_src}")
    private String src;

    @Value("${zhiqu_api_request_params_pky}")
    private String pky;


    /***
     * 构建token
     * 步骤如下：
     * 第一步  按照key排序,并将'=','&'去掉 后 结果为：
     *     condition[relation]andcondition[params][0][relation]andcondition[params][0][params][0][name]mobilecondition[params][0][params][0][search_type]all_incondition[params][0][params][0][label]手机号condition[params][0][params][0][field_type]3condition[params][0][params][0][value][0]19520023443mid2351pkynT23rPqpisMw8b4Isrczq_scrmtim843802182
     *
     * 第二步 进行URLcode编码 http_build_query($params)) 结果为
     *
     *     condition%5Brelation%5Dandcondition%5Bparams%5D%5B0%5D%5Brelation%5Dandcondition%5Bparams%5D%5B0%5D%5Bparams%5D%5B0%5D%5Bname%5Dmobilecondition%5Bparams%5D%5B0%5D%5Bparams%5D%5B0%5D%5Bsearch_type%5Dall_incondition%5Bparams%5D%5B0%5D%5Bparams%5D%5B0%5D%5Blabel%5D%E6%89%8B%E6%9C%BA%E5%8F%B7condition%5Bparams%5D%5B0%5D%5Bparams%5D%5B0%5D%5Bfield_type%5D3condition%5Bparams%5D%5B0%5D%5Bparams%5D%5B0%5D%5Bvalue%5D%5B0%5D19520023443mid2351pkynT23rPqpisMw8b4Isrczq_scrmtim843802182
     *
     *
     * 第三步 md5($tokenString) 对字符串md5结果为
     *
     *     a0b052efb944f194249832af8eade97d
     * @param
     * @return
     */
//    public String buildToken(Map<String, Object> params) {
//        String token = null;
//
//        String sb = "";
//        String[] key = new String[params.size()];
//
//        int index = 0;
//        for (String k : params.keySet()) {
//            key[index] = k;
//            index++;
//        }
//        System.out.println(Arrays.toString(key));
//        Arrays.sort(key);
//        for (String s : key) {
//                sb += s + "=" + params.get(s) + "&";
//
//        }
//        sb = sb.substring(0, sb.length() - 1);
//
//
//        String httpStr = sb;
//
//
//        try {
//
//            httpStr="condition[relation]andcondition[params][0][relation]andcondition[params][0][params][0][name]namecondition[params][0][params][0][search_type]likecondition[params][0][params][0][label]姓名condition[params][0][params][0][field_type]3condition[params][0][params][0][value][0]Sushmamid=32088&page=1&page_size=10&pky=duodiankeji202208&src=duodiankeji&tim=1665630257";
//            System.out.println("s0  "+httpStr);
//            String encode = URLEncoder.encode(httpStr, "UTF-8");
//            System.out.println("s1  "+encode);
//            String s1 = encode.replaceAll("%3D", "=").replaceAll("%26", "&");
//            System.out.println("s1  "+encode);
//            String s2=  s1.replaceAll("=", "").replaceAll("&", "");
//            System.out.println("s2  "+s2);
//            token = MD5Utils.generateMD5(s2);
//            System.out.println("md5  "+token);
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//
//        }
//        return token;
//    }

    public static String sign(final Map<String, Object> data) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keyArray.length; i++) {
            // 参数值为空，则不参与签名
            if (String.valueOf(data.get(keyArray[i])).length() > 0) {
                sb.append(keyArray[i]).append("=").append(data.get(keyArray[i]));
                if (i < keyArray.length - 1) {
                    sb.append("&");
                }
            }
        }
        return sb.toString();
    }

    public String buildToken(TreeMap<String, Object> params) {
        String token = null;
        if (params.isEmpty()) {
            throw new KOLException(ErrorConstant.ERROR_NUM_10022, ErrorConstant.ERROR_MESSAGE_10022_STR);
        }
        Set<String> keySet = params.keySet();
        Iterator<String> iterator = keySet.iterator();
        StringBuilder buffer = new StringBuilder();
        while (iterator.hasNext()) {
            String key = iterator.next();
            if (key.equals("field_data")) {
                Map<String, Object> field_data = (Map<String, Object>) params.get(key);
                Set<String> dataKeys = field_data.keySet();
                Iterator<String> iterator1 = dataKeys.iterator();
                while (iterator1.hasNext()) {
                    String dataKey = iterator1.next();
                    buffer.append("field_data" + "[").append(dataKey).append("]").append("=").append(field_data.get(dataKey).toString());

                    if (iterator1.hasNext()) {
                        buffer.append("&");
                    }
                }
            } else {
                buffer.append(key).append("=").append(params.get(key).toString());
                if (iterator.hasNext()) {
                    buffer.append("&");
                }
            }

        }
        String httpStr = buffer.toString();
        httpStr=  httpStr.replace("=", "").replace("&", "");
        try {
//            String encode = URLEncoder.encode(httpStr, "UTF-8");
            String encode = URLEncoder.encode(httpStr, "UTF-8");
            System.out.println(httpStr);
            System.out.println(encode);
            token = MD5Utils.generateMD5(encode);
//            token = MD5Utils.generateMD5("mid32088page1per_page50pkyduodiankeji202208srcduodiankejitim1552992261updated_at%5Bstart_time%5D2022-10-13updated_at%5Bend_time%5D2022-10-14");
            System.out.println(token);
        } catch (Exception e) {
            logger.error(e.getMessage());

        }
        return token;
    }


    /**
     * 根据页数查新线索列表
     * @return
     * @throws IOException
     */
    public String requestLeadListPage(int page) throws IOException{
        Map<String, Object> params = new HashMap<>();

        params.put("src", src);
        params.put("pky", pky);
        params.put("mid", mainId);
        params.put("tim", (System.currentTimeMillis() / 1000));
//        System.out.println((System.currentTimeMillis() / 1000));

        params.put("page", page);
        params.put("page_size", 200);

        TreeMap<String, Object> tokenBuildMap = new TreeMap<>();
        Set<String> paramsKey = params.keySet();
        Iterator<String> iterator = paramsKey.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            tokenBuildMap.put(key, params.get(key));
        }
        String token = buildToken(tokenBuildMap);
//        System.out.println(token);

        params.put("tok", token);
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            String body = JSONObject.toJSONString(params);
            RequestBody requestBody = RequestBody.create(MediaType.parse("json"), body);
            Request request = new Request.Builder()
                    .url("https://leads.scrmtech.com/api/website/new-leads/new-leads-list")
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
     * 请求新线索列表
     * @return
     * @throws IOException
     */
    public String requestLeadList() throws IOException{
        Map<String, Object> params = new HashMap<>();

        params.put("src", src);
        params.put("pky", pky);
        params.put("mid", mainId);
        params.put("tim", (System.currentTimeMillis() / 1000));
//        System.out.println((System.currentTimeMillis() / 1000));

        params.put("page", 1);
        params.put("page_size", 50);

        TreeMap<String, Object> tokenBuildMap = new TreeMap<>();
        Set<String> paramsKey = params.keySet();
        Iterator<String> iterator = paramsKey.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            tokenBuildMap.put(key, params.get(key));
        }
        String token = buildToken(tokenBuildMap);
//        System.out.println(token);

        params.put("tok", token);
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            String body = JSONObject.toJSONString(params);
            RequestBody requestBody = RequestBody.create(MediaType.parse("json"), body);
            Request request = new Request.Builder()
                    .url("https://leads.scrmtech.com/api/website/new-leads/new-leads-list")
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
     * 请求新线索列表(加时间)
     * @return
     * @throws IOException
     */
    public String requestLeadListTime() throws IOException{
        Map<String, Object> params = new HashMap<>();

        params.put("src", src);
        params.put("pky", pky);
        params.put("mid", mainId);
        long l = System.currentTimeMillis() / 1000;
        params.put("tim", 1665630257);
        System.out.println(l);

        params.put("page", 1);
        params.put("page_size", 10);

//        Map<String, Object> params1 = new HashMap<>();
//        Map<String, Object> params2 = new HashMap<>();
//
//        params2.put("name","lds_create_time");
//        params2.put("search_type","between_and");
//        params2.put("label","创建时间");
//        params2.put("field_type","7");
//
//        params2.put("value","["+"2022-10-01"+","+ "2022-10-02"+"]");
//
//
//        params1.put("relation","and");
//        params1.put("params",params2);
//        params.put("conditon",params1);


        Map<String, Object> params1 = new HashMap<>();//params
        Map<String, Object> params2 = new HashMap<>();//params
        Map<String, Object> params3 = new HashMap<>();//value


        params3.put("name","name");
        params3.put("search_type","like");
        params3.put("label","姓名");
        params3.put("field_type","3");

        ArrayList array1 = new ArrayList();
        array1.add("Sushma");
        params3.put("value",array1);

        ArrayList array2 = new ArrayList();
        array2.add(params3);
        params2.put("params",array2);


        ArrayList array3 = new ArrayList();
        array3.add(params2);
        params1.put("params",array3);



        params.put("condition",params2);


//
//        Map<String, Object> params1 = new HashMap<>();//params
//        Map<String, Object> params2 = new HashMap<>();//params
//        Map<String, Object> params3 = new HashMap<>();//value
//
//        params3.put("name","name");
//        params3.put("search_type","like");
//        params3.put("label","姓名");
//        params3.put("field_type","3");
//
//        ArrayList array1 = new ArrayList();
//        array1.add("Sushma");
//        params3.put("value",array1);
//
//        ArrayList array2 = new ArrayList();
//        array2.add(params3);
//        params2.put("params",array2);
//
//
//        ArrayList array3 = new ArrayList();
//        array3.add(params2);
//        params1.put("params",array3);
//        params1.put("relation","and");
//
//        params.put("condition",params1);
//        params2.put("relation","and");



        TreeMap<String, Object> tokenBuildMap = new TreeMap<>();
        Set<String> paramsKey = params.keySet();
        Iterator<String> iterator = paramsKey.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            tokenBuildMap.put(key, params.get(key));
        }
        String token = buildToken(tokenBuildMap);
        System.out.println(token);

        params.put("tok", token);
//        params.put("tok", "b1893564f63e8a818934e0db10d3f2cb");

        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            String body = JSONObject.toJSONString(params);
            System.out.println(body);
            RequestBody requestBody = RequestBody.create(MediaType.parse("json"), body);
            Request request = new Request.Builder()
                    .url("https://leads.scrmtech.com/api/website/new-leads/new-leads-list")
                    .post(requestBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            Response execute = call.execute();
            if (execute.code() != 200) {
                throw new KOLException(ErrorConstant.ERROR_NUM_11002, ErrorConstant.ERROR_11002_ERROR);
            }
            return execute.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e.toString());
            throw e;
        }

    }


    /**
     * 编辑线索自定义字段
     * @return
     * @throws IOException
     */
    public String updateLeadCustomField() throws IOException{
        Map<String, Object> params = new HashMap<>();

        params.put("src", src);
        params.put("pky", pky);
        params.put("mid", mainId);
        params.put("tim", (System.currentTimeMillis() / 1000));
        System.out.println((System.currentTimeMillis() / 1000));

        params.put("name", 1);
        params.put("options", 50);

        TreeMap<String, Object> tokenBuildMap = new TreeMap<>();
        Set<String> paramsKey = params.keySet();
        Iterator<String> iterator = paramsKey.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            tokenBuildMap.put(key, params.get(key));
        }
        String token = buildToken(tokenBuildMap);
        System.out.println(token);

        params.put("tok", token);
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            String body = JSONObject.toJSONString(params);
            RequestBody requestBody = RequestBody.create(MediaType.parse("json"), body);
            Request request = new Request.Builder()
                    .url("https://leads.scrmtech.com/api/website/new-leads/new-leads-list")
                    .post(requestBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            Response execute = call.execute();
            if (execute.code() != 200) {
                throw new KOLException(ErrorConstant.ERROR_NUM_11002, ErrorConstant.ERROR_11002_ERROR);
            }
            return execute.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e.toString());
            throw e;
        }

    }
    /**
     * 编辑销售线索
     * @return
     * @throws IOException
     */
    public String updateLead() throws IOException{
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> params1 = new HashMap<>();

        params.put("src", src);
        params.put("pky", pky);
        params.put("mid", mainId);
        params.put("tim", (System.currentTimeMillis() / 1000));
        System.out.println((System.currentTimeMillis() / 1000));

        params.put("leads_id", "17831058");
        params.put("3rd_party_id", "");
        params1.put("member_27105","123456");
//        params1.put("member_27105","12345");

        params.put("field_data", params1);


        TreeMap<String, Object> tokenBuildMap = new TreeMap<>();
        Set<String> paramsKey = params.keySet();
        Iterator<String> iterator = paramsKey.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            tokenBuildMap.put(key, params.get(key));
        }
        String token = buildToken(tokenBuildMap);
        System.out.println(token);

//        params.put("tok", token);
        params.put("tok", "2579ffab06769dca74f561e74731d46b");
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            String body = JSONObject.toJSONString(params);
            RequestBody requestBody = RequestBody.create(MediaType.parse("json"), body);
            Request request = new Request.Builder()
                    .url("https://leads.scrmtech.com/api/website/leads-api/edit-leads")
                    .post(requestBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            Response execute = call.execute();
            if (execute.code() != 200) {
                throw new KOLException(ErrorConstant.ERROR_NUM_11002, ErrorConstant.ERROR_11002_ERROR);
            }
            return execute.body().string();
        } catch (Exception e) {
            logger.error("requestLeadList Failed Cause By {}", e.toString());
            throw e;
        }

    }


    public String requestLead() throws IOException{
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> params1 = new HashMap<>();

        params.put("src", src);
        params.put("pky", pky);
        params.put("mid", mainId);
        params.put("tim", 1552992261);
//        System.out.println((System.currentTimeMillis() / 1000));

        params.put("page", 1);
        params.put("page_size", 10);
//        params.put("per_page", 10);
//
//        params1.put("start_time","2022-10-08");
//        params1.put("end_time","2022-10-09");
//        params.put("updated_at",params1);


        TreeMap<String, Object> tokenBuildMap = new TreeMap<>();
        Set<String> paramsKey = params.keySet();
        Iterator<String> iterator = paramsKey.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            tokenBuildMap.put(key, params.get(key));
        }
        String token = buildToken(tokenBuildMap);
//        System.out.println(token);

        params.put("tok", token);
        System.out.println(token);
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            String body = JSONObject.toJSONString(params);
            RequestBody requestBody = RequestBody.create(MediaType.parse("json"), body);
            Request request = new Request.Builder()
                    .url("https://leads.scrmtech.com/api/website/leads-api/leads-list")
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

}
