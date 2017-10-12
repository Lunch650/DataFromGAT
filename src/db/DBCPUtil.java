package db;

import org.apache.commons.dbcp2.BasicDataSourceFactory;
import org.apache.log4j.Logger;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DBCP连接工具
 * Created by Lunch on 2017/9/28-9:44.
 */
public class DBCPUtil {
    
    private static Logger logger = Logger.getLogger(DBCPUtil.class);
    private static DataSource DS;
    private static final String configFile = "dbcp.properties";
    
    public DBCPUtil(){
        initDbcp();
    }
    
    private static void initDbcp(){
        
        Properties pops = new Properties();
        try {
            InputStream in = DBCPUtil.class.getClassLoader().getResourceAsStream(configFile);
            pops.load(in);
            DS = BasicDataSourceFactory.createDataSource(pops);
        } catch (IOException e) {
            logger.error("DBCP配置文件读取失败");
        } catch (NullPointerException e){
            logger.error("配置文件不存在");
        } catch (Exception e){
            e.printStackTrace();
            logger.error("连接池创建失败");
        }
    }
    
    public Connection getConn(){
        Connection conn = null;
        if (DS != null){
            try {
                conn = DS.getConnection();
            } catch (SQLException e) {
                logger.error("数据库连接失败");
            }
            try {
                conn.setAutoCommit(false);
            } catch (SQLException e) {
                logger.error("创建连接失败");
            }
        }
        return conn;
    }
}
