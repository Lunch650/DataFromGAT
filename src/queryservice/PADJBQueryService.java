package queryservice;

import message.SRmessage;

import java.util.HashMap;

/**
 * 社区警务_重点人口查询服务.
 * Created by Lunch on 2017/9/25-11:09.
 */
public class PADJBQueryService extends BaseQueryService{
    
    public HashMap<String, Object> PADJBArgs= new HashMap<>();
    
    public PADJBQueryService(){
        SRmessage srm = new SRmessage();
        PADJBArgs.put("name", "(刑事)破案登记表数据查询服务");
        PADJBArgs.put("wsUrl", "http://10.160.25.92:9001/basic/services/standardSinoService");
        PADJBArgs.put("FWBS", "SZAG52000022000000791");
        PADJBArgs.put("FWDYID", "S0000-00001156");
        PADJBArgs.put("defaultDate", "2016-03-21 00:00:00");//库中没有数据时的自定义时间
        PADJBArgs.put("tableName","PADJB");//该服务对应的数据库表名
        PADJBArgs.put("queryField","HCK_RKSJ");//增量字段名
        PADJBArgs.put("page", 1);//页码
        PADJBArgs.put("dataNum",100);//每次查询数据条数,最大不能超过100
        String queryColumn = srm.assembleQueryColumn((String)PADJBArgs.get("tableName"));
        PADJBArgs.put("queryColumn", queryColumn);
    }

    /**
     * 将请求报文存入到hashmap参数中
     */
    public void pushqqbw(){PADJBArgs.put("qqbw", qqbw(PADJBArgs));}

    /**
     * 将返回报文存入到hashmap参数中
     */
    public void pushjgbw(){PADJBArgs.put("jgbw", jgbw(PADJBArgs));}
}
