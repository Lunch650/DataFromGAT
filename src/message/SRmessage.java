package message;

import dao.DBManager;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Lunch on 2017/9/11.
 * 用于处理请求报文
 */
public class SRmessage {

    /**
     * 将数据库中的字段名组装成报文格式<DATA>column_name</DATA>
     * @param tableName 传递数据库表名
     * @return queryColumn 返回组合后的报文字段,
     */
    public String assembleQueryColumn(String tableName){
        DBManager conn = new DBManager();
        ArrayList<String> columns = conn.getColumn(tableName);
        String queryColumn = "";
        for (String column : columns) {queryColumn += "<Data>" + column + "</Data>\n";}
        return queryColumn;
    }

    /**
     * 将数据库中的字段名组装成报文格式
     * @param queryArgs 从子类中传递报文所需变量
     * @return qqbw 返回请求报文
     */
    public String assembleqqbw(HashMap queryArgs){
        return
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        +"<SRmessage>\n"
                        +"  <FWQQ_BWBH></FWQQ_BWBH>\n"
                        +"  <FWQQZ_ZCXX>\n"
                        +"    <FWQQZMC>镇宁智能实战应用平台</FWQQZMC>\n"
                        +"    <FWQQZBS>A00052042311000000001</FWQQZBS>\n"
                        +"    <FWQQZRKDZLB>\n"
                        +"      <FWQQZ_IPDZ>10.**</FWQQZ_IPDZ>\n"
                        +"      <FWQQZ_ZXDKHM>8080</FWQQZ_ZXDKHM>\n"
                        +"    </FWQQZRKDZLB>\n"
                        +"    <FWQQZ_LXFS>\n"
                        +"      <LXR_XM>梁*</LXR_XM>\n"
                        +"      <LXR_DH>183**</LXR_DH>\n"
                        +"      <LXR_GMSFHM>5225**</LXR_GMSFHM>\n"
                        +"    </FWQQZ_LXFS>\n"
                        +"  </FWQQZ_ZCXX>\n"
                        +"  <FWBS>"
                        + queryArgs.get("FWBS")
                        + "</FWBS>\n"
                        +"  <FW_ZXBS></FW_ZXBS>\n"
                        +"  <FWQQ_RQSJ>20170718161550</FWQQ_RQSJ>\n"
                        +"  <FWQQ_BZ>\n"
                        +"    <FWDYID>"
                        + queryArgs.get("FWDYID")
                        + "</FWDYID>\n"
                        +"    <Items>\n"
                        +"      <Item>\n"
                        +"        <Data>"
                        + queryArgs.get("query")
                        + "</Data>\n"
                        +"        <Data>"+queryArgs.get("page")+"</Data>\n"
                        +"        <Data>"+queryArgs.get("dataNum")+"</Data>\n"
                        +"        <Data>0</Data>\n"
                        +"      </Item>\n"
                        +"      <Item>\n"
                        +"        <Row>\n"
                        + queryArgs.get("queryColumn")
                        +"        </Row>\n"
                        +"      </Item>\n"
                        +"    </Items>\n"
                        +"  </FWQQ_BZ>\n"
                        +"  <XXCZRY_XM>梁*</XXCZRY_XM>\n"
                        +"  <XXCZRY_GMSFHM>5225**</XXCZRY_GMSFHM>\n"
                        +"  <XXCZRY_GAJGJGDM>520423110000</XXCZRY_GAJGJGDM>\n"
                        +"  <XXCZRY_ZWJBDM></XXCZRY_ZWJBDM>\n"
                        +"  <XXCZRY_GWQJDM></XXCZRY_GWQJDM>\n"
                        +"  <XXCZRY_XZQHDM></XXCZRY_XZQHDM>\n"
                        +"  <XXCZRY_MMDJDM></XXCZRY_MMDJDM>\n"
                        +"  <XXCZRY_JZDM></XXCZRY_JZDM>\n"
                        +"  <YBQQ_BZDM></YBQQ_BZDM>\n"
                        +"  <JZQX></JZQX>\n"
                        +"</SRmessage>\n";
    }

    /**
     * 将数据库中的字段名组装成报文格式
     * @param queryArgs,oldVersion queryArgs 从子类中传递报文所需变量 oldVersion true表示旧报文格式 false表示新报文格式
     * @return qqbwOld 返回请求报文
     */
    public String assembleqqbw(HashMap queryArgs,boolean oldVersion){
        return
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
                        "        <Data>"+queryArgs.get("page")+"</Data>\n" +
                        "        <Data>"+queryArgs.get("dataNum")+"</Data>\n" +
                        "        <Data>0</Data>\n" +
                        "        <Data>梁*</Data>\n" +
                        "        <Data>5225**</Data>\n" +
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

}
