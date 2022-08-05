package com.example.demo.controller;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.OrderMapper;
import com.example.demo.dao.ProductMapper;
import com.example.demo.domain.AwardRecord;
import com.example.demo.domain.Order;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pub.dtm.client.DtmClient;
import pub.dtm.client.constant.Constants;
import pub.dtm.client.interfaces.dtm.DtmConsumer;
import pub.dtm.client.model.responses.DtmResponse;
import pub.dtm.client.tcc.Tcc;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/dtm")
public class DTMController {

    private static String svc = "http://127.0.0.1:8080/dtm";

    @Resource
    private OrderMapper orderMapper;
    @Resource
    private ProductMapper productMapper;


    @RequestMapping("/testTcc")
    public String testTcc() {
        //创建dtm clinet
        DtmClient dtmClient = new DtmClient("127.0.0.1:36789");
        //创建tcc事务
        Order order = new Order(Math.abs(RandomUtil.randomLong()), 1L);
        String gid = order.getOrderId() + "";
        try {
            dtmClient.tccGlobalTransaction(gid, tcc -> {
                submicOrder(tcc, order);
            });
        } catch (Exception e) {
            log.error("tccGlobalTransaction error", e);
            return "fail";
        }
        return "success";
    }

    @RequestMapping("/submitOrderTry")
    public Object submitOrderTry(@RequestBody Order order) {
        log.info("===log====提交订单预留资源");
        return DtmResponse.buildDtmResponse(Constants.SUCCESS_RESULT);
    }

    @RequestMapping("/submitOrderConfirm")
    public Object submitOrderConfirm(@RequestBody Order order) {
        log.info("===log====提交订单，确认");
        orderMapper.insert(order);
        return DtmResponse.buildDtmResponse(Constants.SUCCESS_RESULT);
    }

    @RequestMapping("/submitOrderCancel")
    public Object submitOrderCancel(@RequestBody Order order) {
        log.info("===log====提交订单回滚+释放资源");
        orderMapper.deleteByOrderId(order.getOrderId());
        return DtmResponse.buildDtmResponse(Constants.SUCCESS_RESULT);

    }

    //try不成功直接，cancel
    @RequestMapping("/dcrStockTry")
    public Object dcrStockTry(@RequestBody Order order) {
        try {
            long aa = 7070814398113417353L;
            log.info("===log====扣减库存预留资源");
            //int a = 1 / 0;
            return DtmResponse.buildDtmResponse(Constants.SUCCESS_RESULT);
        } catch (Exception e) {
            log.error("===log====扣减库存预留资源.error,", e);
        }
        return DtmResponse.buildDtmResponse(Constants.FAILURE_RESULT);
    }

    //confirm不成功会无限重试
    @RequestMapping("/dcrStockConfirm")
    public Object dcrStockConfirm(@RequestBody Order order) {
        try {
            log.info("===log====扣减库存，确认" + JSONObject.toJSONString(order));
            //int a = 1 / 0;
            Long productId = order.getProductId();
            productMapper.decrStockByProductId(productId);
            return DtmResponse.buildDtmResponse(Constants.SUCCESS_RESULT);
        } catch (Exception e) {
            log.error("===log====扣减库存，确认.error,", e);
        }
        return DtmResponse.buildDtmResponse(Constants.FAILURE_RESULT);
    }

    @RequestMapping("/dcrStockCancel")
    public Object dcrStockCancel(@RequestBody Order order) {
        log.info("===log====扣减库存回滚+释放资源");
        Long productId = order.getProductId();
        productMapper.incrStockByProductId(productId);
        return DtmResponse.buildDtmResponse(Constants.SUCCESS_RESULT);

    }


    private static void submicOrder(Tcc tcc, Order order) throws Exception {
        String submitOrderTry = svc + "/submitOrderTry";
        String submitOrderConfirm = svc + "/submitOrderConfirm";
        String submitOrderCancel = svc + "/submitOrderCancel";
        Response outResponse = tcc.callBranch(order, submitOrderTry, submitOrderConfirm, submitOrderCancel);
        log.info("======下单:{}", outResponse);
        String dcrStockTry = svc + "/dcrStockTry";
        String dcrStockConfirm = svc + "/dcrStockConfirm";
        String dcrStockCancel = svc + "/dcrStockCancel";
        Response inResponse = tcc.callBranch(order, dcrStockTry, dcrStockConfirm, dcrStockCancel);
        log.info("======扣减库存:{}", inResponse);
    }


}
