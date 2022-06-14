package com.example.demo.domain;

import java.util.Date;

public class AwardRecord {
    /**
     * 主键
     */
    private Long id;

    /**
     * 活动id
     */
    private String actId;

    /**
     * 原业务id
     */
    private String sourceBusId;

    /**
     * MD5后的业务id
     */
    private String busId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 奖品类型：1提现，2微信券，3sp券，4商家券
     */
    private Integer awardType;

    /**
     * 奖品详情json
     */
    private String awardBizData;

    /**
     * 领取请求
     */
    private String receiveRequest;

    /**
     * 领取响应
     */
    private String receiveResponse;

    /**
     * 状态，0待领取，1已领取，2领取成功 ，3领取失败，4补偿超最大次数
     */
    private Integer receiveStatus;

    /**
     * 发送次数
     */
    private Integer retryTimes;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * stockId
     */
    private String stockId;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 主键
     * @return id 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 主键
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 活动id
     * @return act_id 活动id
     */
    public String getActId() {
        return actId;
    }

    /**
     * 活动id
     * @param actId 活动id
     */
    public void setActId(String actId) {
        this.actId = actId == null ? null : actId.trim();
    }

    /**
     * 原业务id
     * @return source_bus_id 原业务id
     */
    public String getSourceBusId() {
        return sourceBusId;
    }

    /**
     * 原业务id
     * @param sourceBusId 原业务id
     */
    public void setSourceBusId(String sourceBusId) {
        this.sourceBusId = sourceBusId == null ? null : sourceBusId.trim();
    }

    /**
     * MD5后的业务id
     * @return bus_id MD5后的业务id
     */
    public String getBusId() {
        return busId;
    }

    /**
     * MD5后的业务id
     * @param busId MD5后的业务id
     */
    public void setBusId(String busId) {
        this.busId = busId == null ? null : busId.trim();
    }

    /**
     * 用户id
     * @return user_id 用户id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 用户id
     * @param userId 用户id
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * 奖品类型：1提现，2微信券，3sp券，4商家券
     * @return award_type 奖品类型：1提现，2微信券，3sp券，4商家券
     */
    public Integer getAwardType() {
        return awardType;
    }

    /**
     * 奖品类型：1提现，2微信券，3sp券，4商家券
     * @param awardType 奖品类型：1提现，2微信券，3sp券，4商家券
     */
    public void setAwardType(Integer awardType) {
        this.awardType = awardType;
    }

    /**
     * 奖品详情json
     * @return award_biz_data 奖品详情json
     */
    public String getAwardBizData() {
        return awardBizData;
    }

    /**
     * 奖品详情json
     * @param awardBizData 奖品详情json
     */
    public void setAwardBizData(String awardBizData) {
        this.awardBizData = awardBizData == null ? null : awardBizData.trim();
    }

    /**
     * 领取请求
     * @return receive_request 领取请求
     */
    public String getReceiveRequest() {
        return receiveRequest;
    }

    /**
     * 领取请求
     * @param receiveRequest 领取请求
     */
    public void setReceiveRequest(String receiveRequest) {
        this.receiveRequest = receiveRequest == null ? null : receiveRequest.trim();
    }

    /**
     * 领取响应
     * @return receive_response 领取响应
     */
    public String getReceiveResponse() {
        return receiveResponse;
    }

    /**
     * 领取响应
     * @param receiveResponse 领取响应
     */
    public void setReceiveResponse(String receiveResponse) {
        this.receiveResponse = receiveResponse == null ? null : receiveResponse.trim();
    }

    /**
     * 状态，0待领取，1已领取，2领取成功 ，3领取失败，4补偿超最大次数
     * @return receive_status 状态，0待领取，1已领取，2领取成功 ，3领取失败，4补偿超最大次数
     */
    public Integer getReceiveStatus() {
        return receiveStatus;
    }

    /**
     * 状态，0待领取，1已领取，2领取成功 ，3领取失败，4补偿超最大次数
     * @param receiveStatus 状态，0待领取，1已领取，2领取成功 ，3领取失败，4补偿超最大次数
     */
    public void setReceiveStatus(Integer receiveStatus) {
        this.receiveStatus = receiveStatus;
    }

    /**
     * 发送次数
     * @return retry_times 发送次数
     */
    public Integer getRetryTimes() {
        return retryTimes;
    }

    /**
     * 发送次数
     * @param retryTimes 发送次数
     */
    public void setRetryTimes(Integer retryTimes) {
        this.retryTimes = retryTimes;
    }

    /**
     * 创建时间
     * @return create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 创建时间
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 修改时间
     * @return update_time 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 修改时间
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * stockId
     * @return stock_id stockId
     */
    public String getStockId() {
        return stockId;
    }

    /**
     * stockId
     * @param stockId stockId
     */
    public void setStockId(String stockId) {
        this.stockId = stockId == null ? null : stockId.trim();
    }

    /**
     * 金额
     * @return amount 金额
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * 金额
     * @param amount 金额
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}