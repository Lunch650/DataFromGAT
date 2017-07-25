package queryservice;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import javax.xml.namespace.QName;
import java.util.HashMap;

/**
 * Created by Lunch on 2017/7/25.
 * 刑释解教查询服务类
 */
public class XSJJQueryService extends BaseQueryService{

    public HashMap<String,Object> XSJJArgs = new HashMap<>();

    public XSJJQueryService(String query){
        XSJJArgs.put("name","贵州省****");
        XSJJArgs.put("wsUrl","http://***");
        XSJJArgs.put("FWBS","SZ**");
        XSJJArgs.put("FWDYID","S0**");
        XSJJArgs.put("query",query);
        XSJJArgs.put("totalNum",-1);
        String queryColumn = "";
        String[] columns = {
                //"QSBZ",会导致报错
                "XM",
                "WHCD",
                "HCK_RKSJ",
                "JKZK",
                "BQSJZZSSXQ",
                "BQSJZZXZ",
                "BQSJJZDPCS",
                "HJDSSXQ",
                "HJDXZJD",
                "HJDPCS",
                "HJDXZ",
                "DNABH",
                "FXLB",
                "FXZM",
                "FXXQ",
                "CYRQ",
                "SFCJZPXX",
                "SFCJDNA",
                "SFCJZZWXX",
                "SFCJTMTZXX",
                "SFCJTBTSBJ",
                "SFCJPJQKXX",
                "SFCJFXQJJBQKXX",
                "SFQS",
                "QSSJ",
                "QSRY",
                "CJSJ",
                "CJRY",
                "XGSJ",
                "RYRQ",
                "SCSJ",
                "SCRY",
                "TXZT",
                "SFLJ",
                "LJBZ",
                "LJSJ",
                "LJQRRY",
                "XGRY",
                "ABBSFQS",
                "ABBQSYJ",
                "ABBQSRY",
                "ZYM",
                "SCLJSJ",
                "QSDW",
                "CYLX",
                "JHSTATUS",
                "ABBQSSJ",
                "RYID",
                "RYBH",
                "JSBM",
                "BMCH",
                "XB",
                "CSRQ",
                "MZ",
                "SFZHM",
                "HCK_GXSJ",
                "BQZY",
        };
        for (String column :
                columns) {
            queryColumn += "<Data>"+column+"</Data>\n";
        }
        XSJJArgs.put("queryColumn",queryColumn);
    }

    private String getResource() throws Exception{
        //接口地址，修改成实际地址
        String wsUrl = (String)XSJJArgs.get("wsUrl");
        //使用RPC方式调用WebService
        RPCServiceClient serviceClient = new RPCServiceClient();
        Options options = serviceClient.getOptions();
        //设置20秒超时
        options.setTimeOutInMilliSeconds(20000L);
        //指定调用WebService的URL
        EndpointReference targetEPR = new EndpointReference(wsUrl);
        options.setTo(targetEPR);
        //指定接口方法的参数值
        Object[] opAddEntryArgs = new Object[] { this.qqbw };
        //指定方法返回值的数据类型的Class对象
        Class[] classes = new Class[] { String.class };
        //指定调用的方法及WSDL文件的命名空间
        QName opAddEntry = new QName(
                "http://sinoService.sjfwpt.jzpt.sinobest.cn",
                "queryService");
        //调用queryService方法并输出该方法的返回值,
        //返回对象是一个Object的数组,拿数组的第一个值，转换强转即可
        String bw = serviceClient.invokeBlocking(opAddEntry,
                opAddEntryArgs, classes)[0].toString();
        return bw;
    }

    }

}

