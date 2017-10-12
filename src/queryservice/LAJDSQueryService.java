package queryservice;

import message.SRmessage;

import java.util.HashMap;

/**
 * 社区警务_重点人口查询服务.
 * Created by Lunch on 2017/9/25-11:09.
 */
public class LAJDSQueryService extends BaseQueryService{
    
    public HashMap<String, Object> LAJDSArgs= new HashMap<>();
    
    public LAJDSQueryService(){
        SRmessage srm = new SRmessage();
        LAJDSArgs.put("name", "立案决定书");
        LAJDSArgs.put("wsUrl", "***");
        LAJDSArgs.put("FWBS", "SZAG52000022000000792");
        LAJDSArgs.put("FWDYID", "S0000-00001155");
        LAJDSArgs.put("defaultDate", "2016-03-21 00:00:00");//库中没有数据时的自定义时间
        LAJDSArgs.put("tableName","LAJDS");//该服务对应的数据库表名
        LAJDSArgs.put("queryField","HCK_RKSJ");//增量字段名
        LAJDSArgs.put("page", 1);//页码
        LAJDSArgs.put("dataNum",100);//每次查询数据条数,最大不能超过100
        String queryColumn = srm.assembleQueryColumn((String)LAJDSArgs.get("tableName"));
        LAJDSArgs.put("queryColumn", queryColumn);
    }

    /**
     * 将请求报文存入到hashmap参数中
     */
    public void pushqqbw(){LAJDSArgs.put("qqbw", qqbw(LAJDSArgs));}

    /**
     * 将返回报文存入到hashmap参数中
     */
    public void pushjgbw(){LAJDSArgs.put("jgbw", jgbw(LAJDSArgs));}
}
