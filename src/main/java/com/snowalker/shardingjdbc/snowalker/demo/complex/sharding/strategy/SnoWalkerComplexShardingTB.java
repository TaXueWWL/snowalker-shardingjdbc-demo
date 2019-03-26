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
 * @className 自定义分片规则--表分片
 * @desc 通用复杂分片算法-表路由
 */
public class SnoWalkerComplexShardingTB implements ComplexKeysShardingAlgorithm {

    private static final Logger log = LoggerFactory.getLogger(SnoWalkerComplexShardingTB.class);

    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, Collection<ShardingValue> shardingValues) {
        log.info("availableTargetNames:" + JSON.toJSONString(availableTargetNames) + ",shardingValues:" + JSON.toJSONString(shardingValues));
        //进入通用复杂分片算法-抽象类-表路由：availableTargetNames=["order_info_0000","order_info_0001"],shardingValues=[{"columnName":"user_id","logicTableName":"order_info","values":["UD000000011902261230103345300002"]},{"columnName":"order_id","logicTableName":"order_info","values":["OD020000011902261234512595300002"]}]
        //availableTargetNames:["t_new_order_0000","t_new_order_0001"],
        // shardingValues:[{"columnName":"order_id","logicTableName":"t_new_order","values":["OD010001011903261549424993200011"]},{"columnName":"user_id","logicTableName":"t_new_order","values":["UD030001011903261549424973200007"]}]
        Collection<String> collection = new ArrayList<>();

        for (ShardingValue var : shardingValues) {
            ListShardingValue<String> listShardingValue = (ListShardingValue<String>)var;
            List<String> shardingValue = (List<String>)listShardingValue.getValues();
            // shardingValue:["OD010001011903261549424993200011"]
            log.info("shardingValue:" + JSON.toJSONString(shardingValue));

            //根据列名获取索引规则，得到索引值
            String index = getIndex(listShardingValue.getLogicTableName(),listShardingValue.getColumnName(),shardingValue.get(0));
            //循环匹配数据表源
            for (String availableTargetName : availableTargetNames){
                if (availableTargetName.endsWith("_"+index)) {
                    collection.add(availableTargetName);
                    break;
                }
            }
            //匹配到一种路由规则就可以退出
            if (collection.size() > 0) {
                break;
            }
        }
        return collection;
    }

    /**
     * 根据分片键计算分片节点
     * @param logicTableName
     * @param columnName
     * @param shardingValue
     * @return
     */
    public String getIndex(String logicTableName,String columnName,String shardingValue) {
        String index = "";
        if (StringUtils.isBlank(shardingValue)) {
            throw new IllegalArgumentException("分片键值为空");
        }
        //截取分片键值-下标循环主键规则枚举类，匹配主键列名得到规则
        for (DbAndTableEnum targetEnum : DbAndTableEnum.values()) {
            //目标表路由
            if (targetEnum.getTableName().equals(logicTableName)) {
                //目标表的目标主键路由-例如：根据订单id查询订单信息
                if (targetEnum.getShardingKey().equals(columnName)) {
                    index = getTbIndexBySubString(targetEnum, shardingValue);
                }else{
                    //目标表的非目标主键路由-例如：根据内部用户id查询订单信息-内部用户id路由-固定取按照用户表库表数量
                    //兼容且仅限根据外部id查询用户信息
                    index = getTbIndexByMod(targetEnum, shardingValue);
                }
                break;
            }
        }
        if (StringUtils.isBlank(index)) {
            String msg = "从分片键值中解析表索引异常：logicTableName=" + logicTableName + "|columnName=" + columnName + "|shardingValue=" + shardingValue;
            throw new IllegalArgumentException(msg);
        }
        return index;
    }

    /**
     * 内部用户id使用取模方式对订单表库表数量取模获取分片节点
     * @param shardingValue
     * @return
     */
    public String getTbIndexByMod(DbAndTableEnum targetEnum, String shardingValue) {
        String index = StringUtil.fillZero(String.valueOf(StringUtil.getTbIndexByMod(shardingValue,targetEnum.getDbCount(),targetEnum.getTbCount())), ShardingConstant.TABLE_SUFFIX_LENGTH);
        return index;
    }

    /**
     * 该表主键使用下标方式截取表索引
     * @param targetEnum
     * @param shardingValue
     * @return
     */
    public String getTbIndexBySubString(DbAndTableEnum targetEnum,String shardingValue) {
        return shardingValue.substring(targetEnum.getTbIndexBegin(), targetEnum.getTbIndexBegin() + ShardingConstant.TABLE_SUFFIX_LENGTH);
    }

}
