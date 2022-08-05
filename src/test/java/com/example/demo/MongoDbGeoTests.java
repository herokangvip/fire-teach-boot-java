package com.example.demo;

import com.example.demo.dao.mongo.GeoInfoDao;
import com.example.demo.dao.mongo.UserInfoDao;
import com.example.demo.dao.mongo.UserInfoService;
import com.example.demo.domain.mongo.AddressInfo;
import com.example.demo.domain.mongo.GeoInfo;
import com.example.demo.domain.mongo.UserInfo;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class MongoDbGeoTests {

    @Resource
    private MongoTemplate mongoTemplate;

    @Resource
    private GeoInfoDao geoInfoDao;


    //geo
    @Test
    public void contextTransactional4() {
        Point point = new Point(111.314882, 21.163055);
        geoInfoDao.save(new GeoInfo("海淀", point));
        Point point1 = new Point(112.314882, 22.163055);
        geoInfoDao.save(new GeoInfo("昌平区", point1));
        Point point2 = new Point(113.314882, 23.163055);
        geoInfoDao.save(new GeoInfo("朝阳区", point2));
        System.out.println("-");
    }

    //geo
    @Test
    public void contextTransactional5() {
        Point point = new Point(114.314882, 22.163055);
        NearQuery query = NearQuery.near(point).maxDistance(new Distance(500, Metrics.KILOMETERS)).limit(2);
        GeoResults<GeoInfo> result = mongoTemplate.geoNear(query, GeoInfo.class);
        List<GeoResult<GeoInfo>> resultList = result.getContent();
        for(GeoResult<GeoInfo> geoResult : resultList){
            GeoInfo location = geoResult.getContent();
            System.out.println("坐标: [" + location.getLoc()[0] + ", " + location.getLoc()[1] + "], 距离: [" + geoResult.getDistance() + "]");
        }
    }

}
