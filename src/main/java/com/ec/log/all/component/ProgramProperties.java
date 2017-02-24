package com.ec.log.all.component;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ec.log.all.bean.ParamBean;

/**
 * @ClassName: ProgramProperties
 * @Description: 程序属性值实体
 * @author longqingping
 * @date 2016年3月28日 下午5:11:25
 */
@Component
public class ProgramProperties {

    @Value("${properties.pageSize}")
    private int pageSize;// 每次查询mysql数据量
    @Value("${properties.dbtables}")
    private String dbtables;// 所有需要导数据的表信息
    // thread properties
    @Value("${data.queue.size}")
    private int dataQueueSize;
    // 标识当前进程对应的数据源base还是crm
    @Value("${properties.dbtype}")
    private String databaseType;

    private List<ParamBean> tables = new ArrayList<ParamBean>();

    @PostConstruct
    public void init() {
        for (String t : this.dbtables.split(";")) {
            String[] values = t.split(",");
            ParamBean param = new ParamBean();
            param.setDataBase(values[0]);
            param.setDbName(values[1]);
            param.setTableName(values[2]);
            param.setColumn(values[3]);
            tables.add(param);
        }
    }

    public List<ParamBean> getTables() {
        return tables;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getDbtables() {
        return dbtables;
    }

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public int getDataQueueSize() {
        return dataQueueSize;
    }

    public void setDataQueueSize(int dataQueueSize) {
        this.dataQueueSize = dataQueueSize;
    }
}
