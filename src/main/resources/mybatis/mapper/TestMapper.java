package  mybatis.mapper
import pojo.Test;

public interface TestMapper{
  //查询所有内容
  public List<Test> selectTest() throws Exception;

  //insert
  public void insertTest(Test) throws Exception;

  //查询表字段名
  public String showColumns() throws Exception;

}