package queryservice;


import message.SRmessage;

import java.util.HashMap;

/**
 * Created by Lunch on 2017/7/25.
 * 刑释解教查询服务类
 */
public class XSJJQueryService extends BaseQueryService {
    
    public HashMap<String, Object> XSJJArgs = new HashMap<>();
    
    public XSJJQueryService() {
        SRmessage srm = new SRmessage();
        XSJJArgs.put("name", "刑释解教");
        XSJJArgs.put("wsUrl", "***");
        XSJJArgs.put("FWBS", "SZNB52000022000000613");
        XSJJArgs.put("FWDYID", "S0000-00000950");
        XSJJArgs.put("defaultDate", "2015-11-14 16:00:00");//库中没有数据时的自定义时间
        XSJJArgs.put("tableName","XSJJ");//该服务对应的数据库表名
        XSJJArgs.put("queryField","HCK_RKSJ");//增量字段名
        XSJJArgs.put("page", 1);//页码
        XSJJArgs.put("dataNum",100);//查询数据条数
        String queryColumn = srm.assembleQueryColumn((String)XSJJArgs.get("tableName"));
        XSJJArgs.put("queryColumn", queryColumn);
    }
  
  /**
   * 将请求报文存入到Hashmap参数中
   */
    public void pushqqbw(){XSJJArgs.put("qqbw", qqbw(XSJJArgs));}

  /**
   * 将返回报文存入到hashmap参数中
   */
    public void pushjgbw(){XSJJArgs.put("jgbw", jgbw(XSJJArgs));}
}




