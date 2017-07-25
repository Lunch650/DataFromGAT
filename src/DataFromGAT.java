import queryservice.XSJJQueryService;

/**
 * Author : Lunch
 * Data : 2017/7/23 上午12:57
 */
public class DataFromGAT {
  public static void main(String[] args){

    String XSJJQuery = "SFZHM LIKE '520423%'";
    XSJJQueryService xsjj = new XSJJQueryService(XSJJQuery);
    DataSource XSJJ = new DataSource(xsjj.XSJJArgs);
    try{
      XSJJ.parsejgbw();
    }catch (Exception e){
      e.printStackTrace();
    }
  }
}
