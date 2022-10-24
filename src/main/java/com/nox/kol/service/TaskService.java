///*
// * Copyright 2012-2015 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *	  https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.nox.kol.service;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//
//@Component
//public class TaskService {
//
//
//    @Autowired
//    private SCRMAPIService SCRMAPIService;
//
//    @Autowired
//    private CRMAPIService CRMAPIService;
//
//    @Autowired
//    private DuplicateCheckService duplicateCheckService;
//
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//	private static final Logger log = LoggerFactory.getLogger(TaskService.class);
//
//	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
//
////	@Scheduled(fixedRate = 5000)
////	public void reportCurrentTime() {
////		log.info("The time is now {}", dateFormat.format(new Date()));
////	}
//    @Scheduled(fixedRate = 6800)
//    public void getCorpAccessToken() {
//        String result =null;
//        try {
//            result=  CRMAPIService.getCorpAccessToken();
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            String corpAccessToken = (String) jsonObject.get("corpAccessToken");
//            String corpId = (String) jsonObject.get("corpId");
//
//            redisTemplate.opsForValue().set("corpAccessToken",corpAccessToken);
//            redisTemplate.opsForValue().set("corpId",corpId);
//
//        } catch (Exception e) {
//            System.out.println(e.toString());
//        }
//
//    }
//
//
//	@Scheduled(fixedRate = 30*60*1000)
//	public void getNewLeads(){
//        String result =null;//存储返回的json字符串
//        int flag=0;//标志位 如果查到lds_id就变1
//        int flag2=0;//查询最新id的标志位
//        ArrayList<JSONObject> arrayList = new ArrayList();//存储最新数据
//        long l = Long.parseLong((String) redisTemplate.opsForValue().get("latestLeadid"));
//        int page=1;
////        外层循环请求HTTP
////        内层循环一次请求的list。
////        设置标志位0。
////        如果id小于了就变1
////        break退出查询。
//        while(flag==0) {
//            try {
//                result = SCRMAPIService.requestLeadListPage(page);
//                page++;
//                JSONObject jsonObject = JSONObject.parseObject(result);
//                JSONObject data = jsonObject.getJSONObject("data");
//                JSONArray list = data.getJSONArray("data");
//
//                int size = list.size();
//                for (Object o : list) {
//                    JSONObject objecti = (JSONObject) o;
//                    String lds_idi = (String) objecti.get("lds_id");
//                    long li = Long.parseLong(lds_idi);//遍历list里每一个线索的id
//
//                    System.out.println(li);
//                    System.out.println(l);
//                    if (li == l) {
//                        flag=1;
//                        break;
//                    }
//                    arrayList.add(objecti);
//                }
//                if(flag2==0) {
//                    JSONObject object = (JSONObject) list.get(0);
//                    String lds_id = (String) object.get("lds_id");
//                    redisTemplate.opsForValue().set("latestLeadid",lds_id);
//                    flag2=1;
//                }
//            } catch (Exception e) {
//                System.out.println(e.toString());
//            }
//        }
//        Collections.reverse(arrayList);
//
//        if(arrayList.size()!=0){
//            //有新线索
//            for (JSONObject leads : arrayList) {
//                String lds_id = (String) leads.get("lds_id");
//                String mobile = (String) leads.get("mobile");
//                String email = (String) leads.get("email");
//                String lds_belonger = (String) leads.get("lds_belonger");
//                //线索手机号查重
//                if (!"".equals(mobile)) {//不为空进行查重
//                    int checkTheLeadsPhoneNumber = duplicateCheckService.checkTheLeadsPhoneNumber(lds_id, mobile);
//                    if (checkTheLeadsPhoneNumber == 1) {
//                        //线索手机号重复
//                        continue;
//                    }
//                }
//                //线索邮箱查重
//                int checkTheLeadsEmail = duplicateCheckService.checkTheLeadsEmail(lds_id, email);
//                if (checkTheLeadsEmail == 1) {
//                    //线索邮箱重复
//                    continue;
//                }
//                //联系人手机号查重
//                //联系人电话查重
//                //联系人邮箱查重
//                //客户名称查重
//                //联系人邮箱域名查重
//                //线索邮箱域名查重
//                //线索公司名称查重
//
//
//                //2.1 创建一个新线索
//                String objectIds;
//                try {
//                    objectIds = CRMAPIService.createLeadsObj(leads);
//                } catch (IOException e) {
//                    log.error("创建新线索 " + leads.get("lds_id") + " 失败");
//                    throw new RuntimeException(e);
//                }
//
//                //根据lds_belonger查openUserId
//                String openUserId = null;
//                try {
//                    openUserId = CRMAPIService.queryOpenUserIdByName(lds_belonger);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//                ;
//                //2.2 给leads分配owner
//                try {
//                    result = CRMAPIService.allocateOwner(objectIds, openUserId);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//                //2.3 致趣线索映射到crm新线索
//                try {
//                    String result1 = SCRMAPIService.updateLead(lds_id, "member_27105", openUserId);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//            }
//
//        }
//
//	}
//}
