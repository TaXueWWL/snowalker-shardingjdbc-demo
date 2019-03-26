package com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.sequence.redis;

import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.constant.DbAndTableEnum;
import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.constant.ShardingConstant;
import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.sequence.SequenceGenerator;
import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/3/25 9:09
 * @className RedisSequenceGenerator
 * @desc 序列生成器Redis实现
 */
@Component(value = "redisSequenceGenerator")
public class RedisSequenceGenerator implements SequenceGenerator {

    /**序列生成器key前缀*/
    public static String LOGIC_TABLE_NAME = "sequence:redis:";

    public static int SEQUENCE_LENGTH = 5;

    public static int sequence_max = 90000;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /**
     * redis序列获取实现方法
     * @param targetEnum
     * @param dbIndex
     * @param tbIndex
     * @return
     */
    @Override
    public String getNextVal(DbAndTableEnum targetEnum, int dbIndex, int tbIndex) {

        //拼接key前缀
        String redisKeySuffix = new StringBuilder(targetEnum.getTableName())
                .append("_")
                .append("dbIndex")
                .append(StringUtil.fillZero(String.valueOf(dbIndex), ShardingConstant.DB_SUFFIX_LENGTH))
                .append("_tbIndex")
                .append(StringUtil.fillZero(String.valueOf(tbIndex), ShardingConstant.TABLE_SUFFIX_LENGTH))
                .append("_")
                .append(targetEnum.getShardingKey()).toString();

        String increKey = new StringBuilder(LOGIC_TABLE_NAME).append(redisKeySuffix).toString();
        long sequenceId = stringRedisTemplate.opsForValue().increment(increKey);
        //达到指定值重置序列号，预留后10000个id以便并发时缓冲
        if (sequenceId == sequence_max) {
            stringRedisTemplate.delete(increKey);
        }
        // 返回序列值，位数不够前补零
        return StringUtil.fillZero(String.valueOf(sequenceId), SEQUENCE_LENGTH);
    }
}
