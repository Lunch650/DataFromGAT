package queryservice;

import message.SRmessage;

import java.util.HashMap;

/**
 * 社区警务_重点人口查询服务.
 * Created by Lunch on 2017/9/25-11:09.
 */
public class ZDRKQueryService extends BaseQueryService{
    
    public HashMap<String, Object> ZDRKArgs= new HashMap<>();
    
    public ZDRKQueryService(){
        SRmessage srm = new SRmessage();
        ZDRKArgs.put("name", "重点人口(社区警务)数据查询服务");
        ZDRKArgs.put("wsUrl", "***s");
        ZDRKArgs.put("FWBS", "SZNB52000022000000659");
        ZDRKArgs.put("FWDYID", "S0000-00000301");
        ZDRKArgs.put("defaultDate", "2016-10-25 16:00:00");//库中没有数据时的自定义时间
        ZDRKArgs.put("tableName","SQJW_ZDRK");//该服务对应的数据库表名
        ZDRKArgs.put("queryField","BZK_GXSJ_DATE");//增量字段名
        ZDRKArgs.put("page", 1);//页码
        ZDRKArgs.put("dataNum",100);//每次查询数据条数,最大不能超过100
        String queryColumn = srm.assembleQueryColumn((String)ZDRKArgs.get("tableName"));
        ZDRKArgs.put("queryColumn", queryColumn);
    }

    /**
     * 将请求报文存入到hashmap参数中
     */
    public void pushqqbw(){ZDRKArgs.put("qqbw", qqbw(ZDRKArgs));}

    /**
     * 将返回报文存入到hashmap参数中
     */
    public void pushjgbw(){ZDRKArgs.put("jgbw", jgbw(ZDRKArgs));}
}
