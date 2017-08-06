package queryservice;

import com.mysql.cj.api.mysqla.result.Resultset;

import java.sql.*;

/**
 * Author:Lunch
 * Data:2017/8/7 上午12:12
 * 与数据库操作
 */
public class ConnDB {
  static final String JDBC_DRIVE = "com.mysql.jdbc.drive";
  static final String DB_URL = "jdbc:mysql://localhost/DataFromGAT?characterEncoding=utf8&useSSL=true";

  static final String USER = "lunch650";
  static final String PASS = "lunch650";

  public static void conn(){
    Connection conn = null;
    Statement stt = null;
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      System.out.println("try database");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
    }catch (Exception e){
      e.printStackTrace();
    }
    try {
      stt = conn.createStatement();
      String sql = "Show COLUMNS From test;";
      ResultSet resultset = stt.executeQuery(sql);
      while(resultset.next()){
        System.out.println(resultset.getString("Field"));
      }
      resultset.close();
      stt.close();
      conn.close();
    }catch (Exception e) {
      e.printStackTrace();
    }
  }
}
