package com.example.demo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.mongo.HobbyInfoDao;
import com.example.demo.domain.mongo.HobbyInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class MongoDbMultiKeyTests {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private HobbyInfoDao hobbyInfoDao;


    @Test
    public void test1() {
        List<HobbyInfo> list = new ArrayList<>();
        HobbyInfo hobbyInfo = new HobbyInfo();
        hobbyInfo.setName("小明");
        HashSet<String> set = new HashSet<>();
        set.add("足球");
        set.add("烧烤");
        hobbyInfo.setHobbies(set);
        list.add(hobbyInfo);

        HobbyInfo hobbyInfo2 = new HobbyInfo();
        hobbyInfo2.setName("东方不败");
        HashSet<String> set2 = new HashSet<>();
        set2.add("绣花");
        set2.add("烧烤");
        hobbyInfo2.setHobbies(set2);
        list.add(hobbyInfo2);
        hobbyInfoDao.saveAll(list);
    }
    @Test
    public void test2() {
        //{habbit: "football"}
        Query query = new Query().addCriteria(Criteria.where("hobbies").is("足球"));
        List<HobbyInfo> hobbyInfos = mongoTemplate.find(query, HobbyInfo.class);
        System.out.println(JSONArray.toJSONString(hobbyInfos));
    }

}
