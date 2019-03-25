package com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.entity;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/3/23 10:48
 * @className UserInfoEntity
 * @desc 用户实体
 */
public class UserInfoEntity {

    private Integer id;

    private String userId;

    private String userName;

    public Integer getId() {
        return id;
    }

    public UserInfoEntity setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public UserInfoEntity setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public UserInfoEntity setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    @Override
    public String toString() {
        return "UserInfoEntity{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}
