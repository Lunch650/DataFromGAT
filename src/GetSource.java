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
    this.qqbw =""//用*过滤掉敏感字符
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<SRmessage>\n" +
            "  <FWQQ_BWBH></FWQQ_BWBH>\n" +
            "  <FWQQZ_ZCXX>\n" +
            "    <FWQQZMC>镇宁****</FWQQZMC>\n" +
            "    <FWQQZBS></FWQQZBS>\n" +
            "    <FWQQZRKDZLB>\n" +
            "      <FWQQZ_IPDZ>***</FWQQZ_IPDZ>\n" +
            "      <FWQQZ_ZXDKHM>***</FWQQZ_ZXDKHM>\n" +
            "    </FWQQZRKDZLB>\n" +
            "    <FWQQZ_LXFS>\n" +
            "      <LXR_XM>梁*</LXR_XM>\n" +
            "      <LXR_DH>183***</LXR_DH>\n" +
            "      <LXR_GMSFHM>52***</LXR_GMSFHM>\n" +
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
            "  <XXCZRY_XM>梁*</XXCZRY_XM>\n" +
            "  <XXCZRY_GMSFHM>52***</XXCZRY_GMSFHM>\n" +
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

  public DataSource(HashMap queryMap,boolean oldVersion){
    this(queryMap);
    this.qqbw = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
              "<SinoMessage>\n" +
              "  <ResourceID>"+queryArgs.get("FWDYID")+"</ResourceID>\n" +
              "  <ServiceID>"+queryArgs.get("FWBS")+"</ServiceID>\n" +
              "  <Method>\n" +
              "    <Name>QUERY</Name>\n" +
              "    <Security>\n" +
              "      <Signature></Signature>\n" +
              "      <Encrypt>**</Encrypt>\n" +
              "    </Security>\n" +
              "    <Items>\n" +
              "      <Item>\n" +
              "        <Data>"+queryArgs.get("query")+"</Data>\n" +
              "        <Data>1</Data>\n" +
              "        <Data>10</Data>\n" +
              "        <Data>0</Data>\n" +
              "        <Data>梁*</Data>\n" +
              "        <Data>522***</Data>\n" +
              "        <Data>520423110000</Data>\n" +
              "      </Item>\n" +
              "      <Item>\n" +
              "        <Row>\n" + queryArgs.get("queryColumn")+
              "        </Row>\n" +
              "      </Item>\n" +
              "    </Items>\n" +
              "  </Method>\n" +
              "</SinoMessage>\n";
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
            "http://sinoService.***",
            "queryService");
    //调用queryService方法并输出该方法的返回值,
    //返回对象是一个Object的数组,拿数组的第一个值，转换强转即可
    String bw = serviceClient.invokeBlocking(opAddEntry,
            opAddEntryArgs, classes)[0].toString();
    return bw;
  }

  public void parsejgbw(){
    try{
      String jgbw = getResource();
      Document document = DocumentHelper.parseText(jgbw);
      List<Element> receiveRow = document.selectNodes("/SBmessage/ZXBW_BZ/SPmessage/FWTG_BZ/Items/Item/Value/Row/Data");
      this.totalNum = Integer.parseInt(receiveRow.get(0).getText());
      for (int i = 2; i < receiveRow.size(); i++) {
        System.out.println(receiveRow.get(i).elementText("Normal"));
      }
    }catch (Exception e){
      e.printStackTrace();
    }
  }


}
