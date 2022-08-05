package com.example.demo.dao.mongo;

import com.example.demo.domain.mongo.GeoInfo;
import com.example.demo.domain.mongo.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface GeoInfoDao extends MongoRepository<GeoInfo, String> {
}
