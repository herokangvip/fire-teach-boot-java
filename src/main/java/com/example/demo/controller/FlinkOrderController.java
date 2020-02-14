package com.example.demo.controller;

import com.example.demo.dao.OrderGroupMapper;
import com.example.demo.dao.OrderTotalMapper;
import com.example.demo.domain.OrderGroup;
import com.example.demo.domain.OrderTotal;
import com.example.demo.domain.vo.OrderTotalVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/flink")
public class FlinkOrderController {
    /**
     * 电商监控大屏首页
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @Autowired
    private OrderGroupMapper orderGroupMapper;

    /**
     * 电商监控大屏，按省份聚合的订单数量
     */
    @RequestMapping("/orderGroupData")
    @ResponseBody
    public String orderGroupData() {
        List<OrderGroup> list = orderGroupMapper.select(20200208);
        if (list != null && list.size() > 0) {
            //北京、上海、天津、广州、深圳
            Map<String, OrderGroup> map = new HashMap<>();
            for (OrderGroup group : list) {
                String province = group.getProvince();
                if (!map.containsKey(province)) {
                    map.put(province, group);
                }
            }
            List<Long> nums = new LinkedList<>();
            nums.add(map.get("北京").getTotalNum());
            nums.add(map.get("上海").getTotalNum());
            nums.add(map.get("天津").getTotalNum());
            nums.add(map.get("广州").getTotalNum());
            nums.add(map.get("深圳").getTotalNum());
            String s = nums.toString();
            return "{\"series\":[{\"name\":\"订单数\",\"type\":\"bar\",\"data\":" + s + "}]}";
        }
        return "{\"series\":[{\"name\":\"订单数\",\"type\":\"bar\",\"data\":[0,0,0,0,0]}]}";
    }

    /**
     * 电商监控大屏，按省份聚合的订单金额
     */
    @RequestMapping("/orderGroupMoneyData")
    @ResponseBody
    public String orderGroupMoneyData() {
        List<OrderGroup> list = orderGroupMapper.select(20200208);
        //List<OrderGroup> list = null;
        if (list != null && list.size() > 0) {
            //北京、上海、天津、广州、深圳
            Map<String, OrderGroup> map = new HashMap<>();
            for (OrderGroup group : list) {
                String province = group.getProvince();
                if (!map.containsKey(province)) {
                    map.put(province, group);
                }
            }
            List<Double> nums = new LinkedList<>();
            nums.add(map.get("北京").getTotalMoney());
            nums.add(map.get("上海").getTotalMoney());
            nums.add(map.get("天津").getTotalMoney());
            nums.add(map.get("广州").getTotalMoney());
            nums.add(map.get("深圳").getTotalMoney());
            String s = nums.toString();
            return "{\"series\":[{\"name\":\"订单金额\",\"type\":\"bar\",\"data\":" + s + "}]}";
        }
        return "{\"series\":[{\"name\":\"订单金额\",\"type\":\"bar\",\"data\":[0,0,0,0,0]}]}";
    }


    @Autowired
    private OrderTotalMapper orderTotalMapper;

    /**
     * 电商监控大屏，总订单数与总下单金额
     */
    @RequestMapping("/orderTotalData")
    @ResponseBody
    public OrderTotalVo orderTotalData() {
        OrderTotal select = orderTotalMapper.select(20200208);
        OrderTotalVo orderTotalVo = new OrderTotalVo();
        if (select == null) {
            orderTotalVo.setTotalNum("0");
            orderTotalVo.setTotalMoney("0");
            return orderTotalVo;
        }
        orderTotalVo.setTotalNum(select.getTotalNum() + "");
        orderTotalVo.setTotalMoney(select.getTotalMoney() + "");
        return orderTotalVo;
    }
}
