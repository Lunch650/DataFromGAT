package queryservice;
import dao.DBManager;
import message.SRmessage;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.log4j.Logger;


import javax.xml.namespace.QName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Lunch on 2017/7/25.
 * 查询服务基类
 */
class BaseQueryService {
    private SRmessage srm = new SRmessage();
    private Logger logger = Logger.getLogger(BaseQueryService.class);
    
    private String getResource(String qqbw, String url) throws Exception{
        //使用RPC方式调用WebService
        RPCServiceClient serviceClient = new RPCServiceClient();
        Options options = serviceClient.getOptions();
        // 设置20秒超时
        options.setTimeOutInMilliSeconds(20000L);
        // 指定调用WebService的URL
        EndpointReference targetEPR = new EndpointReference(url);
        options.setTo(targetEPR);
        //指定接口方法的参数值
        Object[] opAddEntryArgs = new Object[] { qqbw };
        //指定方法返回值的数据类型的Class对象
        Class[] classes = new Class[] { String.class };
        //指定调用的方法及WSDL文件的命名空间
        QName opAddEntry = new QName(
                "http://sinoService.sjfwpt.jzpt.sinobest.cn", 
                "queryService");
        //调用queryService方法并输出该方法的返回值,
        // 返回对象是一个Object的数组,拿数组的第一个值，转换强转即可
        return  serviceClient.invokeBlocking(opAddEntry,
            opAddEntryArgs, classes)[0].toString();
    }

    /**
     * @param args 将子类中的参数赋值到SRmessage类中的assembleqqbw方法，组装为请求报文
     * @return 放回最终的请求报文
     */
    String qqbw(HashMap<String, Object> args){return srm.assembleqqbw(args);}

    /**
     * 从第一次的返回报文中计算循环次数
     * @param jgbwNum 返回报文中的数据总数
     * @param dateNum 每次请求查询时的最大查询条数
     * @return int 返回循环次数
    */
    public int pages(String jgbwNum,Object dateNum){
        return (int)Math.ceil(Double.valueOf(jgbwNum)/(double)(int)dateNum);
    }
    
    /**
    * 得到返回报文
    * @return 若成功获取省厅报文则返回报文，获取失败则返回null
    */
    public String jgbw(HashMap<String,Object> args){
        String jgbw;
        try {
            jgbw = getResource((String)args.get("qqbw"), (String) args.get("wsUrl"));
        } catch (Exception e){
            e.printStackTrace();
            jgbw = "null";
        }
        return jgbw;
    }

    /**
     * 利用子类中的增量字段及表名组装查询语句并将查询语句放入到query中
     */
    public void assembleQuery(HashMap<String, Object> args){
        DBManager dbm = new DBManager();
        String lastDate = plusOneSecond(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                        dbm.lastDate(
                                (String)args.get("queryField"), 
                                (String)args.get("tableName"), 
                                (String)args.get("defaultDate")
                        )
                )
        );
        String newDate = plusHour(lastDate);
        args.put("newDate",newDate);
        args.put("query",
                args.get("queryField")+" BETWEEN TO_DATE('"
                        +lastDate
                        +"','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('"
                        +newDate
                        +"','YYYY-MM-DD HH24:MI:SS')");
    }

    /**
     * 查询语句自增长函数,组装新的查询语句并写入查询服务线程参数中
     * @param args 查询服务线程参数
     */
    public void increaseQuery(HashMap<String, Object> args){
        String lastDate = (String)args.get("newDate");
        String newDate = plusHour(lastDate);
        args.put("newDate",newDate);
        args.put("query",
                args.get("queryField")+" BETWEEN TO_DATE('"
                        +lastDate
                        +"','YYYY-MM-DD HH24:MI:SS') AND TO_DATE('"
                        +newDate
                        +"','YYYY-MM-DD HH24:MI:SS')");
    }
    
    /**
     * 获取当前库中最近日期的加八个小时
     * @param lastDate 最近的日期
     * @return String 加八个小时的字符串
     */
    private String plusHour(String lastDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(lastDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.HOUR_OF_DAY, 8);
            return format.format(cal.getTime());
        }catch (ParseException e){
            logger.info("时间格式转换失败");
            return null;
        }
    }
    
    /**
     * 获取当前库中最近日期的加一秒
     * @param lastDate 最近的日期
     * @return String 加一秒的字符串
     */
    private String plusOneSecond(String lastDate){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(lastDate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.SECOND, 1);
            return format.format(cal.getTime());
        }catch (ParseException e){
            logger.info("时间格式转换失败");
            return null;
        }
    }
}

