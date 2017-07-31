import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import pojo.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Author : Lunch
 * Data : 2017/7/23 上午12:57
 */
public class DataFromGAT {
  public static void main(String[] args){
    InputStream inputStream = null;
    try {
      inputStream = Resources.getResourceAsStream("mybatis/mybatis-config.xml");
    }catch (Exception e) {
      e.printStackTrace();
    }
      SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
      SqlSession session = factory.openSession();
    try{
      //session.insert("mybatis.mapper.testMapper.insertTest",user);
      List<String> test = session.selectList("mybatis.mapper.testMapper.showColumns");
      session.commit();
      System.out.print(test);
    }catch (Exception e){
      e.printStackTrace();
    }finally {
      session.close();

    }


/*    String XSJJQuery = "SFZHM LIKE '520423%'";
    XSJJQueryService xsjj = new XSJJQueryService(XSJJQuery,false);
    *//*DataSource XSJJ = new DataSource(xsjj.XSJJArgs);*//*
    try{
      System.out.print(xsjj.XSJJArgs.get("qqbw"));
    }catch (Exception e){
      e.printStackTrace();*/
  }
}
