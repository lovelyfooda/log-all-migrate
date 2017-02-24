package com.ec.log.all.service;

/**
 * @ClassName: IStatistical
 * @Description: 统计服务接口类
 * @author longqingping
 * @date 2016年2月1日 上午10:06:58
 */
public interface IStatistical {

    /**
     *
     * @Title: startFullSync
     * @Description: 数据全量方法
     * @return void
     * @throws
     */
    public void startFullSync();

    /**
     *
     * @Title: getTablesDataCount
     * @Description: 获取mysql所有配置项的数据总量
     * @return void
     * @throws
     */
    public void getMysqlDataCount();

}
