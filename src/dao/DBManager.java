package dao;

import db.DBCPUtil;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Author:Lunch
 * Data:2017/8/7 上午12:12
 * 与数据库操作
 */
public class DBManager {
    
    private static Statement st = null;
    private static ResultSet rs = null;
    private Logger logger = Logger.getLogger(DBManager.class);
    

    /**
     * 获取数据库中字段名称
     *
     * @param tableName 传递表名
     * @return columns 返回包含字段内容的数组
     */
    public ArrayList<String> getColumn(String tableName) {
        ArrayList<String> columns = new ArrayList<>();
        ResultSetMetaData rmd;
        DBCPUtil db = new DBCPUtil();
        Connection conn = db.getConn();
        try {
            String sqlGetColumn = "SELECT * FROM " + tableName;
            rmd = conn.createStatement().executeQuery(sqlGetColumn).getMetaData();
            for (int i = 1; i <= rmd.getColumnCount(); i++) {
                columns.add(rmd.getColumnName(i));
            }
            logger.info("成功获取" + tableName + "表字段名称");
        } catch (SQLException e) {
            logger.info("数据库字段查询失败");
        }finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return columns;
    }

    /**
     * 将一页数据组成一个插入语句
     * @param dataRow 传递数据列表
     * @param tableName 插入的表名
     * @return INSERT语句
     */
    private static String assembleInsert(List<String>dataRow, String tableName){
        
        String singleInsert = "INSERT INTO " + tableName + " VALUES(";
        for (String value:
                dataRow) {
            singleInsert +=  assembleValue(value) ;
        }
        singleInsert = singleInsert.substring(0,singleInsert.length()-1);
        singleInsert += ")";
        return singleInsert;
    }
    /**
     * 插入数据
     *
     * @param dataRow 传递数据列表
     */
    public void insertData(List<List<String>> dataRow, String tableName){
// TODO: 2017/9/29 在数据库中写触发器，对于存在表主键的服务在插入之前判定数据是否已经存在于数据库中 
        DBCPUtil db = new DBCPUtil();
        Connection conn = db.getConn();
        
        try {
            st = conn.createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            for (List<String> data :
                    dataRow) {
                st.addBatch(assembleInsert(data, tableName));
            }
        }catch (SQLException e){
            logger.error("执行addBatch操作失败");
            e.printStackTrace();
        }
        try {
            st.executeBatch();
        } catch (SQLException e) {
            logger.error("INSERT操作出现问题");
            e.printStackTrace();
        }
        try {
            conn.commit();
        } catch (SQLException e) {
            logger.error("commit操作出错");
        }
        try {
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public int queryCount(String tableName){

        DBCPUtil db = new DBCPUtil();
        Connection conn = db.getConn();
        
        String sqlQueryCount = "SELECT COUNT(*) FROM " + tableName;
        int queryCount = 0;
        try {
            rs = conn.createStatement().executeQuery(sqlQueryCount);
            if (rs.next()) {
                queryCount = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return queryCount;
    }
    /**
     * 获取存入的数据中最近的一次日期
     * @param field 表名中增量字段
     * @param tableName 表名称
     * @param defaulDate 库中没有日期的时候自定义开始时间
     * @return 返回表中增量字段的最近一次日期，如果表为空，则返回自定义时间
     */
    public Timestamp lastDate(String field, String tableName, String defaulDate){
        
        DBCPUtil db = new DBCPUtil();
        Connection conn = db.getConn();
        
        String queryLastData = "SELECT MAX(" + field + ") FROM " + tableName;
        try{
            st = conn.createStatement();
        }catch (SQLException e){
            logger.info("lastData函数中查询语句创建失败");
        }
        try{
            rs = st.executeQuery(queryLastData);
            if(rs.next() && rs.getTimestamp(1) != null){ 
                return rs.getTimestamp(1);
            }else {
                try{
                    return Timestamp.valueOf(defaulDate);
                }catch (Exception e){
                    e.printStackTrace();
                    logger.info("自定义初始查询时间失败");
                }
            }
        }catch (SQLException e){
            logger.info("查询最近时间字段操作失败");
        }
        try {
            st.close();
            rs.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 查找返回数据date()类型字段并将其组装为to_date()格式便于insert插入
     * @param value 报文中数据
     * @return String 返回插入语句的字符串
     */
    private static String assembleValue(String value){
        String oracleTimePattern = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}[.]\\d";
        String TimePattern = "\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}";
        String datePattern = "\\d{4}-\\d{2}-\\d{2}";
        value = value.trim().replace("\'","");
        if(Pattern.matches(oracleTimePattern,value)){//判断该字段是否为YYYY-MM-DD HH:MM:SS.0格式
            value = value.substring(0,value.length()-2);
            value = "TO_DATE('"+ value + "','YYYY-MM-DD HH24:MI:SS'),";
        }else if(Pattern.matches(TimePattern,value)){//判断该字段是否为YYYY-MM-DD HH:MM:SS格式
            value = "TO_DATE('"+ value + "','YYYY-MM-DD HH24:MI:SS'),";
        }else if(Pattern.matches(datePattern,value)){//判断该字段是否为YYYY-MM-DD格式
            value = "TO_DATE('"+ value + "','YYYY-MM-DD'),";
        }else{
            value = "'" + value + "',";
        }
        return value;
    }
}
