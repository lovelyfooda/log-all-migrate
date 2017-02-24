package com.ec.log.all.service;

import java.util.List;

/**
 * @ClassName: IProgramService
 * @Description: 作为程序业务处理的服务支撑接口
 * @author longqingping
 * @date 2016年3月30日 上午11:05:09
 */
public interface IProgramService {

    /**
     *
     * @Title: filterTables
     * @Description: 过滤掉不符合条件的表名
     * @param tables
     *            一个库里面的所有表名
     * @param tableTypeName
     *            表的类型表名称
     * @param fiterKey
     *            过滤集合获取所需要的key
     * @return
     * @return List<String>
     * @throws
     */
    public List<String> filterTables(List<String> tables, String tableTypeName);

    /**
     *
     * @Title: getTableSearchDB
     * @Description: 得到表的类型表所在的数据源名称
     * @param type
     *            表的类型表所在的数据源类型
     * @return
     * @return String
     * @throws
     */
    public String getTableSearchDB(String type);

}
