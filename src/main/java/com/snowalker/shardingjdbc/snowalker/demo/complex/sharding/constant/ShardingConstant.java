package com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.constant;

/**
 * @author jiwc
 * @date 2019/2/20
 * @desc 库表规则常量类
 */
public class ShardingConstant {

    /**逻辑数据库源前缀非索引部分长度-用于路由策略截取逻辑数据源匹配,即ds0,ds1,ds2...*/
    public static final int LOGIC_DB_PREFIX_LENGTH = 2;

    /**
     * 数据库后缀索引长度，例如: db_00 ~ db_31
     */
    public static final int DB_SUFFIX_LENGTH = 2;
    /**
     * 表后缀索引长度，例如：user_info_0000 ~ user_info_0127 , order_info_0000 ~ order_info_1024
     */
    public static final int TABLE_SUFFIX_LENGTH = 4;

}
