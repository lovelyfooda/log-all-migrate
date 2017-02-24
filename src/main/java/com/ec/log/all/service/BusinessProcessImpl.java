package com.ec.log.all.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.ec.log.all.bean.ContactAt;
import com.ec.log.all.dao.impl.ContactAtDaoImpl;
import com.ec.log.all.util.CollectionMap;
import com.ec.log.all.util.DataConversionConstants;
import com.ec.log.all.util.Template;
import com.ec.log.all.util.TimeUtils;

@Component
public class BusinessProcessImpl implements BusinessProcess{
    private Logger shar2Log = LogManager.getLogger("dataConversionShard2Log");
    private Logger sqloutLog = LogManager.getLogger("sqloutLog");
    private CollectionMap collectMap = new CollectionMap();
    @Autowired
    private ContactAtDaoImpl contactAtDao;
	
	@Override
	public void handlerData(List<Map<String, Object>> data) {
		for (Map<String, Object> obj : data) {
			ContactAt contactAt = null;
			try {
				contactAt = new ContactAt(obj);
				dealtOne(contactAt);
			}catch (Exception e) {
				shar2Log.error("[contactAt] dealtOne obj: " + JSON.toJSONString(contactAt));
				shar2Log.error("[exception] dealt One error: ", e);
			}
		}
	}
	
	//处理一个月中的一个contactAt
	private void dealtOne(ContactAt contactAt) {
		final long userId = contactAt.getUserId();
		final long crmId = contactAt.getCrmId();
		//connet
		if ( Template.checkContactType(contactAt.getStyle(), contactAt.getOperateType()) ){
			collectMap.connectCrm(userId);
		}
		//年增加用户数 add crm
		if ( contactAt.checkType() ) {
			collectMap.isnertCrm(userId, crmId);
		}
		//累计月
		collectMap.dayCrm(userId, crmId, contactAt.getCreateTime()%100);		
		
		collectMap.addUser(userId);
	}
	
	@Override
	/**
	 * 月通知
	 */
	public void signal(String tableName) {
		try {
			collectMap.allUserMaxCrm(tableName);
		}catch (Exception e) {
			shar2Log.error("[month] dealt month error: ", e);
		}
	}

	@Override
	/**
	 * 最后通知, 写sql
	 */
	public void finishSignal() {
		shar2Log.info("all finish");
		final Set<Long> userIds = collectMap.getUserIds();
		final Map<Long, Integer> maxTimeMap = collectMap.getMaxTimeMap();
		final Map<Long, Set<Long>> addCrmMap = collectMap.getAddCrmMap();
		final Map<Long, Long> connectMap = collectMap.getConnectMap();
		
		final String dbStatic = DataConversionConstants.DB_STATIC;
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (Long userId : userIds) {
			Map<String, Object> map = new HashMap<>();
			Long connectNums = 0L;
			Integer newCrms = 0;
			String maxTimeStr = null;
			try {
				map.put("f_userid", userId);
				
				Set<Long> addCrmSet = addCrmMap.get(userId);
				newCrms = (addCrmSet == null) ? 0 : addCrmSet.size();
				map.put("f_new_client_num", newCrms);//全年连接客户总数
				
				Integer maxTimeCrms = maxTimeMap.get(userId);				
				maxTimeStr = (maxTimeCrms == null) ? null : String.format("%d-%02d-%02d", 2016, maxTimeCrms/100, maxTimeCrms%100);		
				if ( maxTimeStr != null ) {
					map.put("f_connect_day_time", format.parse(maxTimeStr));
				}else {
					map.put("f_connect_day_time", null);
				}

				connectNums = connectMap.get(userId);
				connectNums = (connectNums == null) ? 0L : connectNums;
				map.put("f_connect_count_num", connectNums);
			}catch (Exception e) {
				shar2Log.error("[insert before] insert before Exception: ", e);
			}
			try {
				if ( maxTimeStr != null ) {
					sqloutLog.info(String.format("INSERT INTO d_ec_all_statistics.t_user_report(f_userid, f_new_client_num, f_connect_day_time, f_connect_count_num) VALUES(%d, %d, '%s', %d) ON DUPLICATE KEY UPDATE f_new_client_num = %d, f_connect_day_time = '%s', f_connect_count_num = %d;", 
							userId, newCrms, maxTimeStr, connectNums,
							newCrms, maxTimeStr, connectNums));
				}else {
					sqloutLog.info(String.format("INSERT INTO d_ec_all_statistics.t_user_report(f_userid, f_new_client_num, f_connect_count_num) VALUES(%d, %d, %d) ON DUPLICATE KEY UPDATE f_new_client_num = %d, f_connect_count_num = %d;", 
							userId, newCrms, connectNums,
							newCrms, connectNums));
				}
				contactAtDao.insertContact(dbStatic, map);
			}catch (Exception e) {
				shar2Log.error("[db op] update exception: ", e);				
			}
		}
	}
}
