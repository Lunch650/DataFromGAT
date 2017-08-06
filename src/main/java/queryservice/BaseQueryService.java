package queryservice;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;

import javax.xml.namespace.QName;
import java.util.HashMap;


/**
 * Created by Lunch on 2017/7/25.
 * 查询服务基类
 */
public abstract class BaseQueryService {
  String assembleQueryColumn(String[] columns){
    String queryColumn = "";
    for (String column : columns) {queryColumn += "<Data>" + column + "</Data>\n";}
    return queryColumn;
  }

  String assembleqqbw(HashMap queryArgs){

    String qqbw =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                    +"<SRmessage>\n"
                    +"  <FWQQ_BWBH></FWQQ_BWBH>\n"
                    +"  <FWQQZ_ZCXX>\n"
                    +"    <FWQQZMC>镇宁*</FWQQZMC>\n"
                    +"    <FWQQZBS>A000*</FWQQZBS>\n"
                    +"    <FWQQZRKDZLB>\n"
                    +"      <FWQQZ_IPDZ>10.164.0.69</FWQQZ_IPDZ>\n"
                    +"      <FWQQZ_ZXDKHM>8080</FWQQZ_ZXDKHM>\n"
                    +"    </FWQQZRKDZLB>\n"
                    +"    <FWQQZ_LXFS>\n"
                    +"      <LXR_XM>*纯</LXR_XM>\n"
                    +"      <LXR_DH>183*</LXR_DH>\n"
                    +"      <LXR_GMSFHM>52*</LXR_GMSFHM>\n"
                    +"    </FWQQZ_LXFS>\n"
                    +"  </FWQQZ_ZCXX>\n"
                    +"  <FWBS>"
                    + queryArgs.get("FWBS")
                    + "</FWBS>\n"
                    +"  <FW_ZXBS></FW_ZXBS>\n"
                    +"  <FWQQ_RQSJ>20170718161550</FWQQ_RQSJ>\n"
                    +"  <FWQQ_BZ>\n"
                    +"    <FWDYID>"
                    + queryArgs.get("FWDYID") + "</FWDYID>\n"
                    +"    <Items>\n"
                    +"      <Item>\n"
                    +"        <Data>"
                    + queryArgs.get("query")
                    + "</Data>\n"
                    +"        <Data>1</Data>\n"
                    +"        <Data>10</Data>\n"
                    +"        <Data>0</Data>\n"
                    +"      </Item>\n"
                    +"      <Item>\n"
                    +"        <Row>\n"
                    + queryArgs.get("queryColumn")
                    +"        </Row>\n"
                    +"      </Item>\n"
                    +"    </Items>\n"
                    +"  </FWQQ_BZ>\n"
                    +"  <XXCZRY_XM>*纯</XXCZRY_XM>\n"
                    +"  <XXCZRY_GMSFHM>52*</XXCZRY_GMSFHM>\n"
                    +"  <XXCZRY_GAJGJGDM>52*</XXCZRY_GAJGJGDM>\n"
                    +"  <XXCZRY_ZWJBDM></XXCZRY_ZWJBDM>\n"
                    +"  <XXCZRY_GWQJDM></XXCZRY_GWQJDM>\n"
                    +"  <XXCZRY_XZQHDM></XXCZRY_XZQHDM>\n"
                    +"  <XXCZRY_MMDJDM></XXCZRY_MMDJDM>\n"
                    +"  <XXCZRY_JZDM></XXCZRY_JZDM>\n"
                    +"  <YBQQ_BZDM></YBQQ_BZDM>\n"
                    +"  <JZQX></JZQX>\n"
                    +"</SRmessage>\n";
    return qqbw;
  }

  String assembleqqbw(HashMap queryArgs,boolean oldVersion){
    String qqbwOld =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<SinoMessage>\n" +
                    "  <ResourceID>"+queryArgs.get("FWDYID")+"</ResourceID>\n" +
                    "  <ServiceID>"+queryArgs.get("FWBS")+"</ServiceID>\n" +
                    "  <Method>\n" +
                    "    <Name>QUERY</Name>\n" +
                    "    <Security>\n" +
                    "      <Signature></Signature>\n" +
                    "      <Encrypt>10.164.0.69</Encrypt>\n" +
                    "    </Security>\n" +
                    "    <Items>\n" +
                    "      <Item>\n" +
                    "        <Data>"+queryArgs.get("query")+"</Data>\n" +
                    "        <Data>1</Data>\n" +
                    "        <Data>10</Data>\n" +
                    "        <Data>0</Data>\n" +
                    "        <Data>*纯</Data>\n" +
                    "        <Data>52*</Data>\n" +
                    "        <Data>52*</Data>\n" +
                    "      </Item>\n" +
                    "      <Item>\n" +
                    "        <Row>\n" + queryArgs.get("queryColumn")+
                    "        </Row>\n" +
                    "      </Item>\n" +
                    "    </Items>\n" +
                    "  </Method>\n" +
                    "</SinoMessage>\n";
    return qqbwOld;
  }

  public String getResource(String qqbw,String url) throws Exception{
    //接口地址，修改成实际地址
    String wsUrl = url;
    //使用RPC方式调用WebService
    RPCServiceClient serviceClient = new RPCServiceClient();
    Options options = serviceClient.getOptions();
    //设置20秒超时
    options.setTimeOutInMilliSeconds(20000L);
    //指定调用WebService的URL
    EndpointReference targetEPR = new EndpointReference(wsUrl);
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
    //返回对象是一个Object的数组,拿数组的第一个值，转换强转即可
    String bw = serviceClient.invokeBlocking(opAddEntry,
            opAddEntryArgs, classes)[0].toString();
    System.out.println(bw);
    return bw;
  }
}

