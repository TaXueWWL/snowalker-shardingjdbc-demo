package com.snowalker.shardingjdbc.snowalker.demo.keygenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author snowalker
 * @version 1.0
 * @date 2019/3/3 22:55
 * @className KeyGenerator
 * @desc
 */
public class KeyGenerator {

    synchronized public static long getKey(int shardKeySrc) {
        String key = new SimpleDateFormat("MMddHHmmss").format(new Date(System.currentTimeMillis()));
//        key = new StringBuilder().append(key + Math.abs(keyGenerator.generateKey().intValue())).toString();
        key = key + shardKeySrc;
        return Long.valueOf(key);
    }

    public static void main(String[] args) {
        System.out.println(getKey(1));
    }
}
