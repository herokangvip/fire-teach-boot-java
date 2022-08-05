package com.example.demo.dao.mongo;

import com.example.demo.domain.mongo.AddressInfo;
import com.example.demo.domain.mongo.UserInfo;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class UserInfoService {
    @Resource
    private UserInfoDao userInfoDao;
    @Resource
    private UserInfoService userInfoService;

    @Transactional(propagation = Propagation.REQUIRED,
            transactionManager = "mongoTransactionManager",
            rollbackFor = Exception.class)
    public void testTransactional1() {
        try {
            UserInfo userInfo = userInfoDao.findById("62ebcaa37b4bc32600c3ef77").get();
            userInfo.setUserName("111111");
            userInfoDao.save(userInfo);
            userInfoService.testTransactional2();
            int a = 1 / 0;


        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        System.out.println("-");
    }


    //@Transactional(propagation = Propagation.REQUIRED, transactionManager = "mongoTransactionManager")
    @Transactional(propagation = Propagation.REQUIRES_NEW, transactionManager = "mongoTransactionManager")
    public void testTransactional2() {
        try {
            AddressInfo addressInfo = AddressInfo.builder().addressName("上海市").number(111).build();
            UserInfo hello = UserInfo.builder().userName("tom").age(100)
                    .addressInfo(addressInfo)
                    .build();
            UserInfo save = userInfoDao.save(hello);
            ObjectId objectId = new ObjectId(save.getId());
            Date date = objectId.getDate();
            System.out.println("=");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        System.out.println("-");
    }
}
