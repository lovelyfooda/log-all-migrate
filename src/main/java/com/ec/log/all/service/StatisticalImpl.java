package com.ec.log.all.service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ec.commons.command.ServerShutdownOption;
import com.ec.log.all.bean.ParamBean;
import com.ec.log.all.component.ProgramProperties;
import com.ec.log.all.dao.impl.StatisticalDaoImpl;
import com.ec.log.all.entrance.ProgramEntrance;
import com.ec.log.all.util.JsonUtil;

/**
 * @ClassName: StatisticalImpl
 * @Description: 统计服务实现类
 * @author longqingping
 * @date 2016年2月1日 上午10:07:19
 */
@Service
public class StatisticalImpl implements IStatistical {

    @Autowired
    private StatisticalDaoImpl statisticalDao;
    @Autowired
    private ProgramProperties programProperties;
    @Autowired
    private IProgramService programService;
    @Autowired
    private BusinessProcess businessProcess;
    
    private ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<String, Integer>();

    private BlockingQueue<DataWrap> insertQueue;// 存储mysql读取出来的数据
    private HandlerTask task;

    private Logger mainLog = LogManager.getLogger("dataConversionMainLog");
    private Logger shar1Log = LogManager.getLogger("dataConversionShard1Log");
    private Logger shar2Log = LogManager.getLogger("dataConversionShard2Log");

    @PostConstruct
    private void initThreadPool() {
        insertQueue = new ArrayBlockingQueue<DataWrap>(programProperties.getDataQueueSize());
        task = new HandlerTask();
        task.setName("HandlerTask");
        task.start();
    }

    @Override
    public void startFullSync() {
        executeFullSync();
    }

    private void executeFullSync() {
        // 查询mysql数据总量
        // getMysqlDataCount();
        mainLog.info("需要导入的表配置总数[{}],配置详情[{}]", programProperties.getTables().size(), JsonUtil.toJson(programProperties.getTables()));
        List<ParamBean> params = programProperties.getTables();
        mainLog.info("加载table: " + params.size());
        for (ParamBean p : params) {
            executeTableType(p);            
            //finish signal
            businessProcess.finishSignal();
        }

        // 设置让mongoThread线程池退出while循环的信号
        // setStopSignal();

        mainLog.info("所有需要导入的表配置导入结束");

        // 关闭进程
//        ProgramEntrance.appMain.shutdownServer(ServerShutdownOption.GRACEFUL);
    }

    /**
     *
     * @Title: executeTableType
     * @Description: 对其中一个配置项进行数据导入
     * @param param
     * @return void
     * @throws
     */
    private void executeTableType(ParamBean param) {
        mainLog.info("开始表配置项[{}]的导入", JsonUtil.toJson(param));

        // 得到表的类型表所在的数据源名称
        String database = programService.getTableSearchDB(param.getDataBase());
        param.setDataBase(database);
        // 获取所有表名称与过滤
//        List<String> allTables = statisticalDao.getTables(database, param);
//        List<String> tables = programService.filterTables(allTables, param.getTableName());

        int size = 12;
        final String pre = "t_crm_contact_at";
        mainLog.info("查询到的要导入的表数量[{}]", size);

        for (int i = 1; i <= size; i ++) {
            String tableName = String.format("%s%d%02d", pre, 2016, i);
            mainLog.info("处理表: " + tableName);
            // 开始数据导出操作
            try {
                exportTableData(param, tableName);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
        mainLog.info("当前配置项导入结束");
    }

    private void exportTableData(ParamBean param, String realName) throws CloneNotSupportedException {
        ParamBean bean = (ParamBean) param.clone();
        bean.setPageValue("0");
        bean.setTableName(realName);
        bean.setLimitNum(programProperties.getPageSize());

        shar1Log.info("开始表[{}]数据导入", realName);
        shar2Log.info("开始表[{}]数据导入", realName);

        int dataCount = 0;// 统计此表的总数据量
        int index = 1;// 保存此表执行到的页数

        while (true) {
            shar2Log.info("表[{}]第[{}]页的数据开始导入", realName, index);
            try {
                // 这里catch一下，预防数据库错误数据导致的查询异常
                List<Map<String, Object>> list = statisticalDao.getList(bean.getDataBase(), bean);

                if (list.size() > 0) {
                    dataCount += list.size();

                    insertQueue.put(new DataWrap(list));
                    bean.setPageValue(list.get(list.size() - 1).get(bean.getColumn()).toString());
                }
                // 已经到最后一页
                if (list.size() < programProperties.getPageSize()) {
                    break;
                }
                index++;
                Thread.sleep(200);
            } catch (Exception e) {
                shar2Log.info("表[{}]第[{}]页的数据出现异常,跳出循环不再执行此表的查询", realName, index);
                shar2Log.error(e);
                break;
            }
        }
        // 这里发送一个信号，表示已经导完了一张月表了
        try {
            insertQueue.put(new DataWrap(realName));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        map.put(realName, dataCount);
        shar1Log.info("表[{}]数据总量[{}]", realName, dataCount);
        shar1Log.info("结束表[{}]数据导入", realName);

        shar2Log.info("表[{}]数据总量[{}]", realName, dataCount);
        shar2Log.info("结束表[{}]数据导入", realName);
    }

    private void closeThreadPool(ThreadPoolExecutor threadPool) {
        // 关掉线程池
        threadPool.shutdown();
        try {
            // 等待线程池终止
            threadPool.awaitTermination(24, TimeUnit.HOURS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getMysqlDataCount() {
        mainLog.info("======>开始mysql数据总量查询");
        mainLog.info("需要导入的表配置总数[{}],配置详情[{}]", programProperties.getTables().size(), JsonUtil.toJson(programProperties.getTables()));

        Iterator<ParamBean> ite = programProperties.getTables().iterator();
        while (ite.hasNext()) {
            ParamBean param = ite.next();
            String tableTypeName = param.getTableName();

            mainLog.info("======>开始表配置项[{}]的数据查询", JsonUtil.toJson(param));

            // 这里根据库名找出所有表
            String database = programService.getTableSearchDB(param.getDataBase());
            // 获取所有表名称与过滤
            List<String> allTables = statisticalDao.getTables(database, param);
            List<String> tables = programService.filterTables(allTables, tableTypeName);

            mainLog.info("查询到的要导入的表数量[{}]", tables.size());

            Iterator<String> iteInsert = tables.iterator();

            int allCount = 0;
            while (iteInsert.hasNext()) {
                String realName = iteInsert.next();
                ParamBean bean = null;
                try {
                    bean = (ParamBean) param.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                bean.setTableName(realName);
                int dataCount = this.statisticalDao.getCount(database, bean);
                allCount += dataCount;
                mainLog.info("表名[{}],数据总量[{}]", realName, dataCount);
            }
            mainLog.info("表类型[{}],所有表数据总量[{}]", tableTypeName, allCount);
            mainLog.info("======>当前配置项查询结束");
        }
        mainLog.info("======>结束mysql数据总量查询");
    }

    class HandlerTask extends Thread {

        private boolean sign = true;// 线程退出的信号

        @Override
        public void run() {
            shar2Log.info("消费队列线程启动");
            while (sign) {
                try {
                    DataWrap data = insertQueue.take();
                    if (data.isSign) {
                        // 这里通知信号
                    	businessProcess.signal(data.getTableName());
                    } else {
                    	businessProcess.handlerData(data.getData());
                    }
                    shar2Log.info("取出数据了");
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
            shar2Log.info("消费队列线程退出");
        }

        public void qutiSign() {
            sign = false;
        }

    }

    // 数据包装类
    class DataWrap {

        private boolean isSign;
        private String tableName;
        private List<Map<String, Object>> data;

        public DataWrap(List<Map<String, Object>> data) {
            this.isSign = false;
            this.data = data;
        }

        public DataWrap(String tableName) {
            this.isSign = true;
            this.tableName = tableName;
        }

        public boolean isSign() {
            return isSign;
        }

        public void setSign(boolean isSign) {
            this.isSign = isSign;
        }

        public List<Map<String, Object>> getData() {
            return data;
        }

        public void setData(List<Map<String, Object>> data) {
            this.data = data;
        }
        public String getTableName() {
			return tableName;
		}
        public void setTableName(String tableName) {
			this.tableName = tableName;
		}
    }

}
