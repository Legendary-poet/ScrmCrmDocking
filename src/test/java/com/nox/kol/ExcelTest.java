package com.nox.kol;

import com.alibaba.fastjson.JSONArray;
import com.nox.kol.service.CRMAPIService;
import com.nox.kol.service.SCRMAPIService;
import com.nox.kol.service.UploadingExcel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.tags.EditorAwareTag;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest(classes = ScrmCrmDockingApplication.class)
@EnableAsync
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ExcelTest {
    @Autowired
    private com.nox.kol.service.CRMAPIService CRMAPIService;

    @Autowired
    private com.nox.kol.service.SCRMAPIService SCRMAPIService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UploadingExcel uploadingExcel;


//    @Test
//    public void test() throws IOException {
//        String string ="[{\"姓名\":\"51\",\"邮箱\":\"\",\"电话\":\"13528863293\",\"rowNum\":2,\"原BD\":\"\",\"线索取得日期\":\"2021/12/12\",\"地址\":\"\",\"标签-备注\":\"\",\"公司名\":\"深圳市倬盟网络科技有限公司\",\"行业\":\"\"},{\"姓名\":\"Betty\",\"邮箱\":\"\",\"电话\":\"13530260696\",\"rowNum\":3,\"原BD\":\"\",\"线索取得日期\":\"2021/12/13\",\"地址\":\"\",\"标签-备注\":\"\",\"公司名\":\"Topbox\",\"行业\":\"\"}]";
//        JSONArray array = JSONArray.parseArray(string);
//        uploadingExcel.uploadingLocalExcel(array);
//
//
//    }


    @Test
    public void dateToStamp() throws Exception {
        DateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        String str = null;
        str = "2021-12-12";
        try {
            date = format1.parse(str);
            System.out.println(date);
            long ts = date.getTime();
            System.out.println(ts);
        } catch (ParseException e) {

            e.printStackTrace();

        }


    }

}
