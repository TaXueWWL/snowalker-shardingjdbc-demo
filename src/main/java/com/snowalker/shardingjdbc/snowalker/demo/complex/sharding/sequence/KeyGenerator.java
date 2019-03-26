package com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.sequence;

import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.constant.DbAndTableEnum;
import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.constant.ShardingConstant;
import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.util.DateUtil;
import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.util.StringUtil;
import io.shardingsphere.core.keygen.DefaultKeyGenerator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/3/25 9:17
 * @className KeyGenerator
 * @desc 自定义分布式主键生成器
 */
@Component
public class KeyGenerator {

    @Resource(name = "redisSequenceGenerator")
    SequenceGenerator sequenceGenerator;

    /**默认集群机器总数*/
    private static final int DEFAULT_HOST_NUM = 64;

    /**
     * 根据路由id生成内部系统主键id，
     * 路由id可以是内部其他系统主键id，也可以是外部第三方用户id
     * @param targetEnum 待生成主键的目标表规则配置
     * @param relatedRouteId  路由id或外部第三方用户id
     * @return
     */
    public String generateKey(DbAndTableEnum targetEnum, String relatedRouteId) {

        if (StringUtils.isBlank(relatedRouteId)) {
            throw new IllegalArgumentException("路由id参数为空");
        }

        StringBuilder key = new StringBuilder();
        /** 1.id业务前缀*/
        String idPrefix = targetEnum.getCharsPrefix();
        /** 2.id数据库索引位*/
        String dbIndex = getDbIndexAndTbIndexMap(targetEnum, relatedRouteId).get("dbIndex");
        /** 3.id表索引位*/
        String tbIndex = getDbIndexAndTbIndexMap(targetEnum, relatedRouteId).get("tbIndex");
        /** 4.id规则版本位*/
        String idVersion = targetEnum.getIdVersion();
        /** 5.id时间戳位*/
        String timeString = DateUtil.formatDate(new Date());
        /** 6.id分布式机器位 2位*/
        String distributedIndex = getDistributedId(2);
        /** 7.随机数位*/
        String sequenceId = sequenceGenerator.getNextVal(targetEnum, Integer.parseInt(dbIndex), Integer.parseInt(tbIndex));
        /** 库表索引靠前*/
        return key.append(idPrefix)
                .append(dbIndex)
                .append(tbIndex)
                .append(idVersion)
                .append(timeString)
                .append(distributedIndex)
                .append(sequenceId).toString();
    }

    /**
     * 根据已知路由id取出库表索引，外部id和内部id均 进行ASCII转换后再对库表数量取模
     * @param targetEnum 待生成主键的目标表规则配置
     * @param relatedRouteId 路由id
     *                       取模求表 取商求库
     * @return
     */
    private Map<String, String> getDbIndexAndTbIndexMap(DbAndTableEnum targetEnum,String relatedRouteId) {
        Map<String, String> map = new HashMap<>();
        /** 获取库索引*/
        String preDbIndex = String.valueOf(StringUtil.getDbIndexByMod(relatedRouteId,targetEnum.getDbCount(),targetEnum.getTbCount()));
        String dbIndex = StringUtil.fillZero(preDbIndex, ShardingConstant.DB_SUFFIX_LENGTH);
        /** 获取表索引*/
        String preTbIndex = String
                .valueOf(StringUtil.getTbIndexByMod(relatedRouteId,targetEnum.getDbCount(),targetEnum.getTbCount()));
        String tbIndex = StringUtil
                .fillZero(preTbIndex,ShardingConstant.TABLE_SUFFIX_LENGTH);
        map.put("dbIndex", dbIndex);
        map.put("tbIndex", tbIndex);
        return map;
    }

    /**
     * 生成id分布式机器位
     * @return 分布式机器id
     * length与hostCount位数相同
     */
    private String getDistributedId(int length, int hostCount) {
        return StringUtil
                .fillZero(String.valueOf(getIdFromHostName() % hostCount), length);
    }

    /**
     * 生成id分布式机器位
     * @return 支持最多64个分布式机器并存
     */
    private String getDistributedId(int length) {
        return getDistributedId(length, DEFAULT_HOST_NUM);
    }

    /**
     * 适配分布式环境，根据主机名生成id
     * 分布式环境下，如：Kubernates云环境下，集群内docker容器名是唯一的
     * 通过 @See org.apache.commons.lang3.SystemUtils.getHostName()获取主机名
     * @return
     */
    private Long getIdFromHostName(){
        //unicode code point
        int[] ints = StringUtils.toCodePoints(SystemUtils.getHostName());
        int sums = 0;
        for (int i: ints) {
            sums += i;
        }
        return (long)(sums);
    }

    /**
     * ShardingSphere 默认雪花算法 {@link io.shardingsphere.core.keygen.DefaultKeyGenerator}
     * 生成18位随机id，并提供静态方法设置workId
     * @return
     */
    public Long getSnowFlakeId() {
        return new DefaultKeyGenerator().generateKey().longValue();
    }

    /**
     * 使用ShardingSphere 内置主键生成器 生成分布式主键id
     * 共16位：其中4位减号，12位数字+小写字母
     * 形如：3e9c3ac9-d7a1-43de-9db3-dec502e9ab3e
     */
    public String getUUID() {
        return UUID.randomUUID().toString();
    }

}
