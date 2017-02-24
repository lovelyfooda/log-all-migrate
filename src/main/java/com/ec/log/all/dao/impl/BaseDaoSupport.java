package com.ec.log.all.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.ec.log.all.util.DataConversionConstants;

/**
 * @ClassName: BaseDaoSupport
 * @Description: crm4个库切换支持类
 * @author longqingping
 * @date 2015年12月14日 下午2:46:49
 */
@Component
public class BaseDaoSupport {

    @Resource(name = "sqlSessionBase")
    private org.mybatis.spring.SqlSessionTemplate sqlSessionBase;
    @Resource(name = "sqlSessionCrm0")
    private org.mybatis.spring.SqlSessionTemplate sqlSessionCrm0;
    @Resource(name = "sqlSessionCrm1")
    private org.mybatis.spring.SqlSessionTemplate sqlSessionCrm1;
    @Resource(name = "sqlSessionCrm2")
    private org.mybatis.spring.SqlSessionTemplate sqlSessionCrm2;
    @Resource(name = "sqlSessionCrm3")
    private org.mybatis.spring.SqlSessionTemplate sqlSessionCrm3;
    @Resource(name = "sqlSessionStatic")
    private org.mybatis.spring.SqlSessionTemplate sqlSessionStatic;

    public org.mybatis.spring.SqlSessionTemplate getSqlSessionTemplate(String database) {
        if (DataConversionConstants.DB_BASE.equals(database)) {
            return sqlSessionBase;
        } else if (DataConversionConstants.DB_CRM0.equals(database)) {
            return sqlSessionCrm0;
        } else if (DataConversionConstants.DB_CRM1.equals(database)) {
            return sqlSessionCrm1;
        } else if (DataConversionConstants.DB_CRM2.equals(database)) {
            return sqlSessionCrm2;
        } else if (DataConversionConstants.DB_CRM3.equals(database)) {
            return sqlSessionCrm3;
        } else if (DataConversionConstants.DB_STATIC.equals(database)) {
        	return sqlSessionStatic;
        }else {
            return null;
        }
    }

}