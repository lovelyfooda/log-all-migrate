package com.ec.log.all.bean;

import java.util.Date;
import java.util.Map;

import com.ec.log.all.util.TimeUtils;

public class ContactAt {
	private Date time;
	private int createTime;
	private long userId;
	private long crmId;
	private int operateType;
	private int style;
	
	public ContactAt() {}
	
	public ContactAt(Map<String, Object> obj) {
		Date date = (Date) obj.get("f_creat_time");
		time = date;
		createTime = Integer.parseInt(TimeUtils.dateFormat(date));
		userId = ((Number) obj.get("f_user_id")).longValue();
		crmId = ((Number) obj.get("f_crm_id")).longValue();
		operateType = ((Number) obj.get("f_operate_type")).intValue();
		style = ((Number) obj.get("f_style")).intValue();
	}
	
	public int getCreateTime() {
		return createTime;
	}
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getCrmId() {
		return crmId;
	}
	public void setCrmId(long crmId) {
		this.crmId = crmId;
	}
	public int getOperateType() {
		return operateType;
	}
	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}
	public int getStyle() {
		return style;
	}
	public void setStyle(int style) {
		this.style = style;
	}
	
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	public boolean checkType() {
		if ( style == 3 && (operateType >= 101 && operateType <= 102 || operateType >= 201 && operateType <= 209) ) {
			return true;
		}
		return false;
	}
}
