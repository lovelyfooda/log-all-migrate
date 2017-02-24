package com.ec.log.all.bean;

/**
 * @ClassName: ParamBean
 * @Description: 作为mybatis参数bean
 * @author longqingping
 * @date 2016年2月1日 上午10:20:38
 */
public class ParamBean implements Cloneable {

    private String dataBase;// 数据库名称 base库还是CRM库
    private String dbName;// 数据库名称
    private String tableName;// 表名称
    private String column;// 分页主键列名
    //
    private String pageValue;// 分页筛选条件值
    private int limitNum;// 每页数据大小

    public ParamBean() {
    }

    public ParamBean(String dbName, String tableName) {
        this.dbName = dbName;
        this.tableName = tableName;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getPageValue() {
        return pageValue;
    }

    public void setPageValue(String pageValue) {
        this.pageValue = pageValue;
    }

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public String getDataBase() {
        return dataBase;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    @Override
    public String toString() {
        return "ParamBean [dataBase=" + dataBase + ", dbName=" + dbName + ", tableName=" + tableName + ", column=" + column + ", pageValue="
                + pageValue + ", limitNum=" + limitNum + "]";
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
