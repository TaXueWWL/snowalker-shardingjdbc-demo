package com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.entity;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/3/23 10:50
 * @className OrderNewInfoEntity
 * @desc 复合分片订单实体
 */
public class OrderNewInfoEntity {

    private Integer id;
    private String userId;
    private String orderId;
    private String userName;

    public Integer getId() {
        return id;
    }

    public OrderNewInfoEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public OrderNewInfoEntity setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderNewInfoEntity setOrderId(String orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public OrderNewInfoEntity setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Override
    public String toString() {
        return "OrderNewInfoEntity{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
