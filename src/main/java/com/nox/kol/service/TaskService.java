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
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//
//@Component
//public class TaskService {
//
//    @Autowired
//    private SCRMAPIService SCRMAPIService;
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
//
//
//	@Scheduled(fixedRate = 10*60*1000)
//	public void getNewLeads(){
//        String result =null;//存储返回的json字符串
//        int flag=0;//标志位 如果查到lds_id就变1
//        int flag2=0;//查询最新id的标志位
//        ArrayList<String> arrayList = new ArrayList();//存储最新数据
//        long l = Long.parseLong((String) redisTemplate.opsForValue().get("latestLeadid"));
//        int page=1;
////        外层循环请求HTTP
////        内层循环一次请求的list。
////        设置标志位0。
////        如果id小于了就变1
////        break退出查询。
//
//        while(flag==0) {
//            try {
//                result = SCRMAPIService.requestLeadListPage(page);
//                page++;
//                JSONObject jsonObject = JSONObject.parseObject(result);
//                JSONObject data = jsonObject.getJSONObject("data");
//                JSONArray list = data.getJSONArray("data");
//
//
//                int size = list.size();
//                for (Object o : list) {
//                    JSONObject objecti = (JSONObject) o;
//                    String lds_idi = (String) objecti.get("lds_id");
//                    long li = Long.parseLong(lds_idi);//遍历list里每一个线索的id
//
////                    long l = Long.parseLong((String) redisTemplate.opsForValue().get("latestLeadid"));
//                    System.out.println(li);
//                    System.out.println(l);
//                    if (li == l) {
//                        flag=1;
//                        break;
//                    }
//                    //TODO: 存储线索
//                    arrayList.add(String.valueOf(objecti));
//                }
//
//                if(flag2==0) {
//                    JSONObject object = (JSONObject) list.get(0);
//                    String lds_id = (String) object.get("lds_id");
////                    long l = Long.parseLong(lds_id);
//                    redisTemplate.opsForValue().set("latestLeadid",lds_id);
//                    flag2=1;
////                    System.out.println(l);
//                }
//
//            } catch (Exception e) {
//                System.out.println(e.toString());
//            }
//        }
//        System.out.println(arrayList);
//        System.out.println("测试");
//
//
//	}
//}
