package com.snowalker.shardingjdbc.snowalker.demo.complex.sharding.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * @date 2019/2/18
 * @desc
 */
public class StringUtil {

    /**
     * 指定字符串位数前缀补零
     * @param value
     * @param needLength
     * @return
     */
    public static String fillZero(String value, int needLength) {
        if (StringUtils.isBlank(value)) {
            StringBuilder result = new StringBuilder();
            for (int i=0;i<needLength;i++) {
                result.append("0");
            }
            return result.toString();
        }
        StringBuilder str = new StringBuilder("");
        for (int i = 0; i < needLength - value.length(); i++) {
            str.append("0");
        }
        return str.append(value).toString();
    }

    /**
     * 指定数字字符串位数前缀去零
     * @param value
     * @return
     */
    public static String deleteZero(String value) {
        Long number = Long.valueOf(value);
        return number.toString();
    }

    /**
     * 判断字符串是否是纯数字，从最后一位开始判断
     * @param str
     * @return
     */
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    /**
     * 转换字符串路由id为纯数字ascII
     * @param str
     * @return
     */
    public static String getAscII(String str) {
        if (str != null && !"".equals(str)) {
            StringBuilder indexSB = new StringBuilder();
            for (int i = 0; i < str.length(); ++i) {
                char[] strChar = str.substring(i, i + 1).toCharArray();
                char[] var4 = strChar;
                int var5 = strChar.length;
                for (int var6=0;var6<var5;++var6) {
                    char s = var4[var6];
                    indexSB.append((byte)s);
                }
            }
            return indexSB.toString();
        } else {
            return "0";
        }
    }

    /**
     * 先对指定对象取ASCII码后取模运算
     * @param obj
     * @param num
     * @return
     */
    public static long getModValue(Object obj,long num) {
        String str = getAscII(obj == null?"":obj.toString());
        BigDecimal bc = new BigDecimal(str);
        BigDecimal[] results = bc.divideAndRemainder(new BigDecimal(num));
        return (long)results[1].intValue();
    }

    /**
     * @param obj
     * @param dbCount
     * @param tbCount
     * @return
     */
    public static long getDbIndexByMod(Object obj,int dbCount,int tbCount) {
        long tbRange = getModValue(obj, tbCount);
        BigDecimal bc = new BigDecimal(tbRange);
        BigDecimal[] results = bc.divideAndRemainder(new BigDecimal(dbCount/tbCount));
        return (long)results[0].intValue();
    }

    /**
     *
     * @param obj
     * @param dbCount
     * @param tbCount
     * @return
     */
    public static long getTbIndexByMod(Object obj,int dbCount,int tbCount) {
        long tbRange = getModValue(obj, tbCount);
        BigDecimal bc = new BigDecimal(tbRange);
        BigDecimal[] results = bc.divideAndRemainder(new BigDecimal(tbCount/dbCount));
        return (long)results[1].intValue();
    }
}
