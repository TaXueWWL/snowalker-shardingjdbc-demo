package com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.strategy;

import com.alibaba.fastjson.JSON;
import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.constant.DbAndTableEnum;
import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.constant.ShardingConstant;
import com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.util.StringUtil;
import io.shardingsphere.api.algorithm.sharding.ListShardingValue;
import io.shardingsphere.api.algorithm.sharding.ShardingValue;
import io.shardingsphere.api.algorithm.sharding.complex.ComplexKeysShardingAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/3/23 10:55
 * @className SnoWalkerComplexShardingDB
 * @desc 自定义复合分片规则--数据源分片规则
 */
public class SnoWalkerComplexShardingDB implements ComplexKeysShardingAlgorithm {

    private static final Logger log = LoggerFactory.getLogger(SnoWalkerComplexShardingDB.class);

    /**
     * @param availableTargetNames 可用数据源集合
     * @param shardingValues   分片键
     * @return sharding results for data sources or tables's names
     */
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, Collection<ShardingValue> shardingValues) {

        log.info("availableTargetNames:" + JSON.toJSONString(availableTargetNames) + ",shardingValues:" + JSON.toJSONString(shardingValues));
        //进入通用复杂分片算法-抽象类-数据库路由：availableTargetNames=["ds0","ds1","ds2","ds3"],
        //shardingValues=[{"columnName":"user_id","logicTableName":"order_info","values":["UD000000011902261230103345300002"]},
        // {"columnName":"order_id","logicTableName":"order_info","values":["OD020000011902261234512595300002"]}]
        List<String> shardingResults = new ArrayList<>();

        for (ShardingValue var : shardingValues) {

            ListShardingValue<String> listShardingValue = (ListShardingValue<String>)var;
            List<String> shardingValue = (List<String>)listShardingValue.getValues();

            log.info("shardingValue:" + JSON.toJSONString(shardingValue));

            //根据列名获取索引规则，得到索引值
            String index = getIndex(listShardingValue.getLogicTableName(),
                                    listShardingValue.getColumnName(),
                                    shardingValue.get(0));

            //循环匹配数据源
            for (String name : availableTargetNames) {
                //获取逻辑数据源索引后缀
                String nameSuffix = name.substring(ShardingConstant.LOGIC_DB_PREFIX_LENGTH);
                if (nameSuffix.equals(index)) {
                    shardingResults.add(name);
                    break;
                }
            }

            //匹配到一种路由规则就可以退出
            if (shardingResults.size() > 0) {
                break;
            }
        }

        return shardingResults;
    }

    /**
     * 根据分片键计算分片节点
     * @param logicTableName
     * @param columnName
     * @param shardingValue
     * @return
     */
    public String getIndex(String logicTableName, String columnName, String shardingValue) {
        String index = "";
        if (StringUtils.isBlank(shardingValue)) {
            throw new IllegalArgumentException("分片键值为空");
        }
        //截取分片键值-下标循环主键规则枚举类，匹配主键列名得到规则
        for (DbAndTableEnum targetEnum : DbAndTableEnum.values()) {

            /**目标表路由
             * 如果逻辑表命中，判断路由键是否与列名相同
             */
            if (targetEnum.getTableName().equals(logicTableName)) {
                //目标表的目标主键路由-例如：根据订单id查询订单信息
                if (targetEnum.getShardingKey().equals(columnName)) {
                    index = getDbIndexBySubString(targetEnum, shardingValue);
                }else{
                    //目标表的非目标主键路由-例如：根据内部用户id查询订单信息-内部用户id路由-固定取按照用户表库表数量
                    //兼容且仅限根据外部id路由 查询用户信息
                    index = getDbIndexByMod(targetEnum, shardingValue);
                }
                break;
            }
        }
        if (StringUtils.isBlank(index)) {
            String msg = "从分片键值中解析数据库索引异常：logicTableName=" + logicTableName + "|columnName=" + columnName + "|shardingValue=" + shardingValue;
            throw new IllegalArgumentException(msg);
        }
        return index;
    }

    /**
     * 内部用户id使用取模方式对目标表库表数量取模获取分片节点
     * @param shardingValue
     * @return
     */
    public String getDbIndexByMod(DbAndTableEnum targetEnum,String shardingValue) {
        String index = String.valueOf(StringUtil.getDbIndexByMod(shardingValue,targetEnum.getDbCount(),targetEnum.getTbCount()));
        return index;
    }

    /**
     * 该表主键使用下标方式截取数据库索引
     * @param targetEnum
     * @param shardingValue
     * @return
     */
    public String getDbIndexBySubString(DbAndTableEnum targetEnum, String shardingValue) {
        int indexBegin = targetEnum.getDbIndexBegin();
        int indexEnd = targetEnum.getDbIndexBegin() + ShardingConstant.DB_SUFFIX_LENGTH;
        return StringUtil.deleteZero(shardingValue.substring(indexBegin, indexEnd));
    }

}
