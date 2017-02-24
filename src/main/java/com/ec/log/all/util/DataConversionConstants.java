package com.ec.log.all.util;

/**
 * @ClassName: DataConversionConstants
 * @Description: 常量池
 * @author longqingping
 * @date 2016年2月1日 上午11:15:09
 */
public class DataConversionConstants {

    // flag the datasource
    public static String DB_BASE = "db_base";
    public static String DB_CRM0 = "db_crm0";
    public static String DB_CRM1 = "db_crm1";
    public static String DB_CRM2 = "db_crm2";
    public static String DB_CRM3 = "db_crm3";
    public static String DB_STATIC = "db_static";

    // flag the database type
    public static String DB_BASE_TYPE = "0";
    public static String DB_CRM_TYPE = "1";

    // full sync status
    public static int STATUS_START = 1;// 开始全量
    public static int STATUS_MIGRATING = 2;// 全量中
    public static int STATUS_FINISHED = 3;// 全量完成

}
