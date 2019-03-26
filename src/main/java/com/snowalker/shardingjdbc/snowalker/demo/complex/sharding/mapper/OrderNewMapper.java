package com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.mapper;

import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.entity.OrderNewInfoEntity;
import com.snowalker.shardingjdbc.snowalker.demo.entity.OrderInfo;

import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/3/23 10:52
 * @className OrderNewMapper
 * @desc 订单 new Mapper
 */
public interface OrderNewMapper {

    List<OrderNewInfoEntity> queryOrderInfoList(OrderNewInfoEntity orderInfo);

    OrderNewInfoEntity queryOrderInfoByOrderId(OrderNewInfoEntity orderInfo);

    int addOrder(OrderNewInfoEntity orderInfo);
}
