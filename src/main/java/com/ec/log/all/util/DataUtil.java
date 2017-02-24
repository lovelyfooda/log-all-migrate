package com.ec.log.all.util;

/**
 * @ClassName: DataUtil
 * @Description: TODO
 * @author Administrator
 * @date 2016年3月28日 下午7:08:36
 */
public class DataUtil {

    public static boolean isNull(String values) {
        if (null == values || "".equals(values)) {
            return true;
        }
        return false;
    }

    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int getRandomNumber() {
        return (int) ((Math.random() * 9 + 1) * 1000000);// 生成7位随机数
    }

}
