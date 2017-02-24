package com.ec.log.all.service;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: BusinessProcess
 * @Description: 业务处理接口
 * @author longqingping
 * @date 2017年1月14日 上午11:32:56
 */
public interface BusinessProcess {

    /**
     *
     * @Title: handlerData
     * @Description: 处理数据
     * @return void
     * @throws
     */
    public void handlerData(List<Map<String, Object>> data);

    /**
     *
     * @Title: signal
     * @Description: 当接收到信号的时候，表明已经完成了一个月表
     * @return void
     * @throws
     */
    public void signal(String tableName);

    /**
     *
     * @Title: finishSignal
     * @Description: 完成所有表数据的扫描工作
     * @return void
     * @throws
     */
    public void finishSignal();

}
