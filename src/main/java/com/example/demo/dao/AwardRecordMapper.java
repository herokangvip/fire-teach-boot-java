package com.example.demo.dao;

import com.example.demo.domain.AwardRecord;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface AwardRecordMapper {
    /**
     *
     * @mbg.generated 2022-06-14
     */
    int deleteByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2022-06-14
     */
    int insert(AwardRecord record);

    /**
     *
     * @mbg.generated 2022-06-14
     */
    int insertSelective(AwardRecord record);

    /**
     *
     * @mbg.generated 2022-06-14
     */
    AwardRecord selectByPrimaryKey(Long id);

    /**
     *
     * @mbg.generated 2022-06-14
     */
    int updateByPrimaryKeySelective(AwardRecord record);

    /**
     *
     * @mbg.generated 2022-06-14
     */
    int updateByPrimaryKey(AwardRecord record);

    int countByUserIdAndActivityCodeToday(@Param("userId") String userId,
                                          @Param("start") Date todayStartDateTime, @Param("end") Date todayEndDateTime);
}