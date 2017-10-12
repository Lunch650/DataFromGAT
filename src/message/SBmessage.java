package message;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Lunch on 2017/9/11.
 * 用于处理返回报文
 */
public class SBmessage {

    private Element rootElement;
    private Logger logger = Logger.getLogger(SBmessage.class);
    

    public String getRootElement(String jgbw){
        SAXReader saxReader = new SAXReader();
        Document document;
        jgbw = replaceStr(jgbw);
        jgbw = jgbw.replaceAll("\\(FW","<FW").replaceAll("\\(LXR","<LXR");
        jgbw = jgbw.
                replaceAll("\\(ZX","<ZX").
                replaceAll("\\(BW","<BW").
                replaceAll("\\(SP","<SP").
                replaceAll("\\(LX","<LX").
                replaceAll("\\(DZ","<DZ").
                replaceAll("\\(TX","<TX");
        try{
            document = saxReader.read(new ByteArrayInputStream(jgbw.getBytes()));
            this.rootElement = document.getRootElement();
        }catch (DocumentException e){
            logger.error("getRootElement函数读取报文失败");
            System.out.println(jgbw);
            e.printStackTrace();
            return "null";
        }
        return "";
    }

    /**
     * 返回请求报文中的Code值
     * @return String Code值
     */
    public String jgbwCode() {
        try {
            return this.rootElement.
                    element("ZXBW_BZ").
                    element("SPmessage").
                    element("FWTGZTDM").getText();
        }catch (NullPointerException e){
            return "-1";
        }
    }
    
    /**
     * 返回报文中总数据条数
     * @return int 数据条数 如果读取报文出错，则返回0
     */
    public String jgbwTotalNum(){
        try {
            return
                    this.rootElement.
                            element("ZXBW_BZ").
                            element("SPmessage").
                            element("FWTG_BZ").
                            element("Items").
                            element("Item").
                            element("Value").
                            element("Row").
                            element("Data").getText();
        }catch (NullPointerException e){
            return "0";
        }
    }


    /**
     * 获取返回报文中Row标签个数
     * @return 如果返回报文出错则返回0
     */
    public int jgbwCountRow(){
        try {
            return
                    this.rootElement.
                            element("ZXBW_BZ").
                            element("SPmessage").
                            element("FWTG_BZ").
                            element("Items").
                            element("Item").
                            element("Value").
                            elements("Row").size();
        }catch (NullPointerException e){
            return -1;
        }
    }
    
    /**
     * 解析返回报文中的数据
     * @return List 返回包含数据的集合 如果报文出错则返回null
     */
    public List<List<String>> jgbwData(){
        List<List<String>> jgbwDataList = new ArrayList<>();
            //获取返回报文中数据
        try{
            List jgbwValue = this.rootElement.
                    element("ZXBW_BZ").
                    element("SPmessage").
                    element("FWTG_BZ").
                    element("Items").
                    element("Item").
                    element("Value").
                    elements("Row");
            if(jgbwValue.size() > 2) {
                jgbwDataList = getjgbwData(jgbwValue);
            }
        }catch (Exception e){
            jgbwDataList = null;
        }
        return jgbwDataList;
    }

    /**
     * 返回报文中的数据
     * @return List 返回包含数据的集合 如果报文出错则返回null
     */
    public List<List<String>> standardjgbwData(){
        List<List<String>> jgbwDataList = new ArrayList<>();
        //获取返回报文中数据
        try{
            List jgbwValue = this.rootElement.
                    element("FWTG_BZ").
                    element("Items").
                    element("Item").
                    element("Value").
                    elements("Row");
            if(jgbwValue.size() > 2) {
                jgbwDataList = getjgbwData(jgbwValue);
            }
        }catch (Exception e){
            jgbwDataList = null;
        }
        return jgbwDataList;
    }

    /**
     * 获取报文中Normal标签中的数据
     * @param jgbwValue 返回报文中jgbwValue标签中的全部内容
     * @return 返回数据
     */
    private List<List<String>> getjgbwData(List jgbwValue){
        List<List<String>> jgbwDataList = new ArrayList<>();
        for (int i = 2; i < jgbwValue.size(); i++) {
            Element e = (Element) jgbwValue.get(i);
            List dataValue = e.elements("Data");
            List<String> dataRow = new ArrayList<>();
            for (Object n : dataValue) {
                Element m = (Element) n;
                dataRow.add(m.element("Normal").getText());
            }
            jgbwDataList.add(dataRow);
        }
        return jgbwDataList;
    }
    
    /**
     * 返回请求报文中的Code值
     * @return String Code值
     */
    public String standardjgbwCode() {
        try {
            return this.rootElement.
                    element("FWTGZTDM").getText();
        }catch (NullPointerException e){
            return "-1";
        }
    }
    
    
    /**
     * 返回报文中总数据条数
     * @return int 数据条数 如果读取报文出错，则返回0
     */
    public String standardjgbwTotalNum(){
        try {
            return
                    this.rootElement.
                            element("FWTG_BZ").
                            element("Items").
                            element("Item").
                            element("Value").
                            element("Row").
                            element("Data").getText();
        }catch (NullPointerException e){
            return "0";
        }
    }

    /**
     * 获取返回报文中Row标签个数
     * @return 如果返回报文出错则返回0
     */
    public int standardjgbwCountRow(){
        try {
            return
                    this.rootElement.
                            element("FWTG_BZ").
                            element("Items").
                            element("Item").
                            element("Value").
                            elements("Row").size();
        }catch (NullPointerException e){
            return -1;
        }
    }
    

    //<Normal><2007>018></Normal>
    private static String replaceStr(String jgbw) {
        String str = null;
        String temp;
        String reg;
        StringBuffer sb;
        Pattern p;
        Matcher m;

        for(int i = 0 ; i< 2 ; i ++){
            // 替换掉全部的<
            if(i == 0){
                jgbw = jgbw.replaceAll("&", "").replaceAll("<<", "<");
                reg = "[^\n]<[^/]";
                sb = new StringBuffer();
                p = Pattern.compile(reg);
                m = p.matcher(jgbw);
                while (m.find()) {
                    temp = m.group(0).replace("<", "(");
                    m.appendReplacement(sb, temp);
                }
                m.appendTail(sb);
                str = sb.toString();

            }
        }

        return str;
    }
}
