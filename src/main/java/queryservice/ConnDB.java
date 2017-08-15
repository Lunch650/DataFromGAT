package queryservice;

import java.sql.*;

/**
 * Author:Lunch
 * Data:2017/8/7 上午12:12
 * 与数据库操作
 */
public class ConnDB {
  static final String JDBC_DRIVE = "com.mysql.cj.jdbc.drive";
  static final String DB_URL = "jdbc:mysql://localhost/DataFromGAT?characterEncoding=utf8&useSSL=true";

  static final String USER = "lunch650";
  static final String PASS = "lunch650";

  public static void getColumns(String tableName){
    Connection conn = null;
    Statement statement = null;
    String sql = "Show COLUMNS From"+tableName+";";
    ResultSet resultSet;
    String[] columns;
    try{
      Class.forName("com.mysql.cj.jdbc.Driver");
    }catch (ClassNotFoundException e){
      e.printStackTrace();
    }

    try {
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
    }catch (SQLException e){
      e.printStackTrace();
    }

    try{
      statement = conn.createStatement();
    }catch (Exception e) {
      e.printStackTrace();
    }

    try{
      resultSet = statement.executeQuery(sql);
      while (resultSet.next()) {
        System.out.println(resultSet.getString("Field"));
      }
      resultSet.close();
    }catch (SQLException e){
      e.getSQLState();
    }

    try{
      statement.close();
      conn.close();
    }catch (Exception e){
      e.printStackTrace();
    }

  }
}
