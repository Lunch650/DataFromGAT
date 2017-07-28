import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.xml.namespace.QName;
import java.util.*;

/**
 * Author : Lunch
 * Data : 2017/7/23 上午12:50
 */

class DataSource{
  public HashMap<String,String> queryArgs = new HashMap<>();
  private String qqbw;
  private int totalNum;

  public DataSource(HashMap queryMap) {
    //将报文参数加入到请求报文
    queryArgs = queryMap;
    qqbw =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<SRmessage>\n" +
                    "  <FWQQ_BWBH></FWQQ_BWBH>\n" +
                    "  <FWQQZ_ZCXX>\n" +
                    "    <FWQQZMC>镇宁智能实战应用平台</FWQQZMC>\n" +
                    "    <FWQQZBS>A00052042311000000001</FWQQZBS>\n" +
                    "    <FWQQZRKDZLB>\n" +
                    "      <FWQQZ_IPDZ>10.164.0.69</FWQQZ_IPDZ>\n" +
                    "      <FWQQZ_ZXDKHM>8080</FWQQZ_ZXDKHM>\n" +
                    "    </FWQQZRKDZLB>\n" +
                    "    <FWQQZ_LXFS>\n" +
                    "      <LXR_XM>梁纯</LXR_XM>\n" +
                    "      <LXR_DH>18334028890</LXR_DH>\n" +
                    "      <LXR_GMSFHM>522526198706050036</LXR_GMSFHM>\n" +
                    "    </FWQQZ_LXFS>\n" +
                    "  </FWQQZ_ZCXX>\n" +
                    "  <FWBS>" + queryArgs.get("FWBS") + "</FWBS>\n" +
                    "  <FW_ZXBS></FW_ZXBS>\n" +
                    "  <FWQQ_RQSJ>20170718161550</FWQQ_RQSJ>\n" +
                    "  <FWQQ_BZ>\n" +
                    "    <FWDYID>" + queryArgs.get("FWDYID") + "</FWDYID>\n" +
                    "    <Items>\n" +
                    "      <Item>\n" +
                    "        <Data>" + queryArgs.get("query") + "</Data>\n" +
                    "        <Data>1</Data>\n" +
                    "        <Data>10</Data>\n" +
                    "        <Data>0</Data>\n" +
                    "      </Item>\n" +
                    "      <Item>\n" +
                    "        <Row>\n" + queryArgs.get("queryColumn")+
                    "        </Row>\n" +
                    "      </Item>\n" +
                    "    </Items>\n" +
                    "  </FWQQ_BZ>\n" +
                    "  <XXCZRY_XM>梁纯</XXCZRY_XM>\n" +
                    "  <XXCZRY_GMSFHM>522526198706050036</XXCZRY_GMSFHM>\n" +
                    "  <XXCZRY_GAJGJGDM>520423110000</XXCZRY_GAJGJGDM>\n" +
                    "  <XXCZRY_ZWJBDM></XXCZRY_ZWJBDM>\n" +
                    "  <XXCZRY_GWQJDM></XXCZRY_GWQJDM>\n" +
                    "  <XXCZRY_XZQHDM></XXCZRY_XZQHDM>\n" +
                    "  <XXCZRY_MMDJDM></XXCZRY_MMDJDM>\n" +
                    "  <XXCZRY_JZDM></XXCZRY_JZDM>\n" +
                    "  <YBQQ_BZDM></YBQQ_BZDM>\n" +
                    "  <JZQX></JZQX>\n" +
                    "</SRmessage>\n";
  }


  private String getResource() throws Exception{
    //接口地址，修改成实际地址
    String wsUrl = queryArgs.get("wsUrl");
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