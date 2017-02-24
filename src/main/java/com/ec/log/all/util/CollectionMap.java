package com.ec.log.all.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionMap {
	private List<Map<Long, Set<Long>>> monthDistinctMap;
	private Set<Long> monthUserSet;
	private Map<Long, Integer> maxCrmMap;
	private Map<Long, Integer> maxTimeMap;
	
	private Map<Long, Set<Long>> addCrmMap;
	private Map<Long, Long> connectMap;
	private Set<Long> userIds;
	
	public Map<Long, Integer> getMaxCrmMap() {
		return maxCrmMap;
	}
	public Map<Long, Integer> getMaxTimeMap() {
		return maxTimeMap;
	}
	public Map<Long, Set<Long>> getAddCrmMap() {
		return addCrmMap;
	}
	public Set<Long> getUserIds() {
		return userIds;
	}
	public Map<Long, Long> getConnectMap() {
		return connectMap;
	}
	
	public CollectionMap() {
		monthDistinctMap = new ArrayList<>(31);
		monthUserSet = new HashSet<>();
		for (int i = 0; i < 31; i ++) {	//预存31天
			monthDistinctMap.add(new HashMap<Long, Set<Long>>());
		}
		maxCrmMap = new HashMap<>();
		maxTimeMap = new HashMap<>();
		addCrmMap = new HashMap<>();
		connectMap = new HashMap<>();
		userIds = new HashSet<>();
	}
	
	public void addUser(long userId) {
		if ( !userIds.contains(userId) ) {
			userIds.add(userId);
		}
	}
	
	public void connectCrm(long userId) {
		Long crmCnt = connectMap.get(userId);
		if ( crmCnt == null ) {
			crmCnt = 1L;
		}else {
			++ crmCnt;
		}
		connectMap.put(userId, crmCnt);
	}
	
	public void isnertCrm(long userId, long crmId) {
		Set<Long> crmSet = addCrmMap.get(userId);
		if ( crmSet == null ) {
			crmSet = new HashSet<>();
			addCrmMap.put(userId, crmSet);
		}
		if ( !crmSet.contains(crmId) ) {
			crmSet.add(crmId);
		}
	}
	
	public void dayCrm(long userId, long crmId, int day) {
		if ( day < 1 || day > 31 ) return ;
		Map<Long, Set<Long>> dayDisMap = monthDistinctMap.get(day - 1);
		Set<Long> crmSet = dayDisMap.get(userId);
		if ( crmSet == null ) {
			crmSet = new HashSet<>();
			dayDisMap.put(userId, crmSet);
		}
		if ( !crmSet.contains(crmId) ) {
			crmSet.add(crmId);
		}		
		if ( !monthUserSet.contains(userId) ) {
			monthUserSet.add(userId);
		}
	}
	
	//set single max
	private void maxCrm(long userId, int month) {
		Integer maxCnt = maxCrmMap.get(userId);
		if ( maxCnt == null ) {
			maxCnt = 0;
		}
		int _max = -1;
		int k = -1;
		//31天
		for (int i = 0; i < 31; i ++) {
			Map<Long, Set<Long>> dayCrmMap = monthDistinctMap.get(i);
			Set<Long> daySet = dayCrmMap.get(userId);
			int currCnt = 0;
			if ( daySet != null ) {
				currCnt = daySet.size();
			}
			if ( _max < currCnt ) {
				_max = currCnt;
				k = i;
			}
		}
		if ( _max > maxCnt ) {
			maxCrmMap.put(userId, _max);
			maxTimeMap.put(userId, month * 100 + (k+1));
		}
	}
	
	//set all userMax
	public void allUserMaxCrm(String tableName) {
		final Set<Long> userIds = monthUserSet;
		Integer month = Integer.parseInt(tableName.replaceAll("[^0-9]+(\\d+)", "$1")) % 100;
		for (Long userId : userIds) {
			maxCrm(userId, month);
		}
		clearMonthMap();
	}	
	
	//clear mongth
	private void clearMonthMap() {
		for (Map<Long, Set<Long>> set : monthDistinctMap) {
			set.clear();
		}
		monthUserSet.clear();
	}	
}
