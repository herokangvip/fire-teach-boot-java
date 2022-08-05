package com.example.demo.dao.mongo;

import com.example.demo.domain.mongo.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserInfoDao extends MongoRepository<UserInfo, String> {

    UserInfo findByUserName(String name);
}
