package com.example.demo.dao.mongo;

import com.example.demo.domain.mongo.HobbyInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HobbyInfoDao extends MongoRepository<HobbyInfo, String> {
}
