package com.example.demo;

import com.example.demo.dao.AwardRecordMapper;
import com.example.demo.dao.UserMapper;
import com.example.demo.dao.WelfareUserTaskMapper;
import com.example.demo.dao.mongo.UserInfoDao;
import com.example.demo.dao.mongo.UserInfoService;
import com.example.demo.domain.User;
import com.example.demo.domain.WelfareUserTask;
import com.example.demo.domain.mongo.AddressInfo;
import com.example.demo.domain.mongo.UserInfo;
import com.example.demo.service.UserService;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class MongoDbTests {

    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private UserInfoService userInfoService;


    @Test
    public void context1() {
        AddressInfo addressInfo = AddressInfo.builder().addressName("北京海淀区西北旺").number(111).build();
        UserInfo hello = UserInfo.builder().userName("小明").age(100)
                .addressInfo(addressInfo)
                .build();
        UserInfo save = userInfoDao.save(hello);
        ObjectId objectId = new ObjectId(save.getId());
        Date date = objectId.getDate();
        System.out.println("=");
    }

    @Test
    public void context2() {
        UserInfo userInfo = userInfoDao.findById("62eb3d9c851ad83bde0ff619").get();
        ObjectId objectId = new ObjectId(userInfo.getId());
        Date date = objectId.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        System.out.println(format);
        System.out.println("=");
    }

    @Test
    public void context3() {
        UserInfo userInfo = userInfoDao.findByUserName("helddlo");

        System.out.println("=");
    }

    @Test
    public void context4() {
        String jsonCommand = "db.userInfo.find()";
        Document document = this.mongoTemplate.executeCommand(jsonCommand);
        System.out.println("-");
    }


    /**
     * 测试事务
     */
    @Test
    public void contextTransactional() {
        userInfoService.testTransactional1();
    }

    @Test
    public void contextTransactional2() {
        try {
            UserInfo userInfo = new UserInfo();
            userInfo.setId("62ebb5ac9cecd96bde1e9d0c");
            userInfo.setUserName("回滚2");
            userInfoDao.save(userInfo);
            //int a = 1/0;
        } catch (Exception e) {
            System.out.println("============事务回滚");
            e.printStackTrace();
            throw e;
        }
        System.out.println("-");
    }


    @Test
    public void contextTransactional3() {
        /*try {
            Thread.sleep(8000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException("----");
        }*/
        UserInfo userInfo = userInfoDao.findById("62ebb5ac9cecd96bde1e9d0c").get();
        userInfo.setUserName("33==");

        userInfoDao.save(userInfo);
        System.out.println("-");
    }

}
