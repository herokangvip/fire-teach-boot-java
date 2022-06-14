package com.example.demo.dao;

import com.example.demo.domain.AwardRecord;

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
}