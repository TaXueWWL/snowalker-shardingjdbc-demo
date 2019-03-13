package com.snowalker.shardingjdbc.snowalker.demo.entity;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/3/3 22:42
 * @className OrderInfo
 * @desc
 */
public class OrderInfo {

    private String id;
    private Long userId;
    private Long orderId;
    private String userName;

    public String getId() {
        return id;
    }

    public OrderInfo setId(String id) {
        this.id = id;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public OrderInfo setUserId(Long userId) {
        this.userId = userId;
        return this;
    }

    public Long getOrderId() {
        return orderId;
    }

    public OrderInfo setOrderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public OrderInfo setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "id='" + id + '\'' +
                ", userId=" + userId +
                ", orderId=" + orderId +
                ", userName='" + userName + '\'' +
                '}';
    }
}
