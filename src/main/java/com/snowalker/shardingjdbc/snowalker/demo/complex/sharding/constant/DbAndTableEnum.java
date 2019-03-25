package com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.constant;

/**
 * @author jiwc
 * @date 2019/2/19
 * @desc 库表规则枚举类
 */
public enum DbAndTableEnum {

    /**
     * 用户信息表 UD+db+table+01+yyMMddHHmmssSSS+机器id+序列号id
     * 例如：UD000000011902261230103345300002 共 2+6+2+15+2+5=32位
     */

    T_USER("t_user", "user_id", "01", "01", "UD", 2, 2, 4, 4, 16, "用户数据表枚举"),

    T_NEW_ORDER("t_new_order", "order_id", "01", "01", "OD", 2,2, 4, 4, 8, "订单数据表枚举");

    /**分片表名*/
    private String tableName;
    /**分片键*/
    private String shardingKey;
    /**系统标识*/
    private String bizType;
    /**主键规则版本*/
    private String idVersion;
    /**表名字母前缀*/
    private String charsPrefix;
    /**分片键值中纯数字起始下标索引，第一位是0,第二位是1，依次类推*/
    private int numberStartIndex;
    /**数据库索引位开始下标索引*/
    private int dbIndexBegin;
    /**表索引位开始下标索引*/
    private int tbIndexBegin;
    /**分布所在库数量*/
    private int dbCount;
    /**分布所在表数量-所有库中表数量总计*/
    private int tbCount;
    /**描述*/
    private String desc;

    DbAndTableEnum(String tableName, String shardingKey, String bizType, String idVersion, String charsPrefix,
                   int numberStartIndex, int dbIndexBegin, int tbIndexBegin, int dbCount, int tbCount, String desc) {
        this.tableName = tableName;
        this.shardingKey = shardingKey;
        this.bizType = bizType;
        this.idVersion = idVersion;
        this.charsPrefix = charsPrefix;
        this.numberStartIndex = numberStartIndex;
        this.dbIndexBegin = dbIndexBegin;
        this.tbIndexBegin = tbIndexBegin;
        this.dbCount = dbCount;
        this.tbCount = tbCount;
        this.desc = desc;
    }

    public String getTableName() {
        return tableName;
    }

    public String getShardingKey() {
        return shardingKey;
    }

    public String getBizType() {
        return bizType;
    }

    public String getIdVersion() {
        return idVersion;
    }

    public String getCharsPrefix() {
        return charsPrefix;
    }

    public int getNumberStartIndex() {
        return numberStartIndex;
    }

    public int getDbIndexBegin() {
        return dbIndexBegin;
    }

    public int getTbIndexBegin() {
        return tbIndexBegin;
    }

    public int getDbCount() {
        return dbCount;
    }

    public int getTbCount() {
        return tbCount;
    }

    public String getDesc() {
        return desc;
    }
}
