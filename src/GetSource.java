import java.util.*;

/**
 * @Author by Lunch
 * @Data 2017/7/23 上午12:50
 */

class DataSource{

  public DataSource(String args[]){
    Map queryArgs = new HashMap();
    queryArgs.put("name",args[0]);
    queryArgs.put("FWBS",args[1]);
    queryArgs.put("FWID",args[2]);
    queryArgs.put("query",args[3]);
    String qqbw = "new"+queryArgs.get("name")+
            queryArgs.get("FWBS")+
            queryArgs.get("FWID");
  }
  public DataSource(String args[],boolean oldVersion){
    Map queryArgs = new HashMap();
    queryArgs.put("name",args[0]);
    queryArgs.put("FWBS",args[1]);
    queryArgs.put("FWID",args[2]);
    queryArgs.put("query",args[3]);
    String qqbw = "new"+queryArgs.get("name")+
            queryArgs.get("FWBS")+
            queryArgs.get("FWID");
  }
}