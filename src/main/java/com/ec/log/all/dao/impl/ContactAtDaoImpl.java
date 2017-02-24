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
public class ContactAtDaoImpl {

    @Autowired
    private BaseDaoSupport BaseDaoSupport;

    public List<String> updateContact(String database, Map<String, Object> map) {
        return BaseDaoSupport.getSqlSessionTemplate(database).selectList("com.ec.data.dao.impl.ContactAtDaoImpl.updateContact", map);
    }
    
    public List<String> insertContact(String database, Map<String, Object> map) {
        return BaseDaoSupport.getSqlSessionTemplate(database).selectList("com.ec.data.dao.impl.ContactAtDaoImpl.insertContact", map);
    }    

}
