package com.ec.log.all.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ec.log.all.bean.ParamBean;
import com.ec.log.all.util.DataConversionConstants;

/**
 * @ClassName: AddLabelDaoImpl
 * @Description: 添加标签接口持久层实现类
 * @author longqingping
 * @date 2015年12月14日 下午2:45:32
 */
@Repository
public class StatisticalDaoImpl {

    @Autowired
    private BaseDaoSupport BaseDaoSupport;

    public List<String> getTables(String database, ParamBean param) {
        return BaseDaoSupport.getSqlSessionTemplate(database).selectList("com.ec.data.dao.impl.StatisticalDaoImpl.getTables", param);
    }

    public List<Map<Object, Object>> getColumns(String database, ParamBean param) {
        return BaseDaoSupport.getSqlSessionTemplate(database).selectList("com.ec.data.dao.impl.StatisticalDaoImpl.getColumns", param);
    }

    public int getCount(String database, ParamBean param) {
        return BaseDaoSupport.getSqlSessionTemplate(database).selectOne("com.ec.data.dao.impl.StatisticalDaoImpl.getCount", param);
    }

    public List<Map<String, Object>> getList(String database, ParamBean param) {
        return BaseDaoSupport.getSqlSessionTemplate(database).selectList("com.ec.data.dao.impl.StatisticalDaoImpl.getList", param);
    }

    public int upStatus(Map<String, Integer> map) {
        return BaseDaoSupport.getSqlSessionTemplate(DataConversionConstants.DB_BASE).update("com.ec.data.dao.impl.StatisticalDaoImpl.updateStatus",
                map);
    }

}
