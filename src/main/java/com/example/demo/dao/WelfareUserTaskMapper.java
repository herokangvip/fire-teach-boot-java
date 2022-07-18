package com.example.demo.dao;


import com.example.demo.domain.WelfareUserTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WelfareUserTaskMapper {
    int deleteByPrimaryKey(WelfareUserTask record);

    int insert(WelfareUserTask record);

    int insertSelective(WelfareUserTask record);

    WelfareUserTask selectByPrimaryKey(WelfareUserTask record);

    int updateByPrimaryKeySelective(WelfareUserTask record);

    int updateByPrimaryKey(WelfareUserTask record);


    List<WelfareUserTask> selectByUserIdAndTaskIdsAndSign(@Param("userId") String userId,
                                                          List<Long> idList,
                                                          @Param("sign") String sign);
}