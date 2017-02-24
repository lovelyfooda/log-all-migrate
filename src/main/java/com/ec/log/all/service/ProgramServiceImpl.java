package com.ec.log.all.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ec.log.all.component.ProgramProperties;
import com.ec.log.all.util.DataConversionConstants;

/**
 * @ClassName: ProgramServiceImpl
 * @Description: TODO
 * @author longiqngping
 * @date 2016年3月30日 下午2:37:43
 */
@Service
public class ProgramServiceImpl implements IProgramService {

    @Autowired
    private ProgramProperties programProperties;

    @Override
    public List<String> filterTables(List<String> tables, String tableTypeName) {
        // 这里根据表名匹配过滤掉不需要的表
        Iterator<String> iteTables = tables.iterator();
        while (iteTables.hasNext()) {
            String table = iteTables.next();
            if (!table.contains(tableTypeName)) {
                iteTables.remove();
            }
        }
        return tables;
    }

    @Override
    public String getTableSearchDB(String type) {
        String database = "";
        if (DataConversionConstants.DB_BASE_TYPE.equals(type)) {// base库
            database = DataConversionConstants.DB_BASE;
        } else if (DataConversionConstants.DB_CRM_TYPE.equals(type)) {// crm库
            database = this.programProperties.getDatabaseType();
        }
        return database;
    }

}
