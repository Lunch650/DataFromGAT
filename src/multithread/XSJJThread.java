package multithread;

import dao.DBManager;
import message.SBmessage;
import org.apache.log4j.Logger;
import queryservice.XSJJQueryService;

import static java.lang.Thread.sleep;


/**
 * 刑释解教执行线程.
 * Created by Lunch on 2017/9/26-10:56.
 */
public class XSJJThread implements Runnable {
    
    private static Logger logger = Logger.getLogger(XSJJThread.class);
    
    @Override
    public void run(){
        
        logger.info("开始调用刑释解教查询服务");
        XSJJQueryService xsjj = new XSJJQueryService();
        SBmessage sbmessage = new SBmessage();
        xsjj.assembleQuery(xsjj.XSJJArgs);
        xsjj.pushqqbw();
        while (true){
            if (!xsjj.jgbw(xsjj.XSJJArgs).equals("null")) {
                //如果有报文返回
                logger.info("请求成功");
                xsjj.pushjgbw();
                if (!sbmessage.getRootElement((String) xsjj.XSJJArgs.get("jgbw")).equals("null")) {
                    if (!sbmessage.jgbwCode().equals("04")) {
                        //如果返回报文中代码不为网络错误04
                        if (sbmessage.jgbwCountRow() > 1) {
                            //如果返回报文中Row标签数量大于1
                            if ((sbmessage.jgbwData() != null) && (sbmessage.jgbwData().size() != 0)) {
                                //如果返回报文中存在Data标签包含的数据内容
                                DBManager dbm = new DBManager();
                                dbm.insertData(sbmessage.jgbwData(), (String) xsjj.XSJJArgs.get("tableName"));
                                logger.info("当前数据库"
                                        + xsjj.XSJJArgs.get("tableName")
                                        + "表中共有" + dbm.queryCount((String) xsjj.XSJJArgs.get("tableName"))
                                        + "条数据");
                                int totalPage = xsjj.pages(sbmessage.jgbwTotalNum(), xsjj.XSJJArgs.get("dataNum"));
                                int currentPage = (Integer) xsjj.XSJJArgs.get("page");
                                if (currentPage < totalPage) {
                                    //如果当前页码不是本次查询中的最后一页
                                    logger.info("本次请求共有" + totalPage + "页数据");
                                    logger.info("现在开始请求第" + (currentPage + 1) + "页数据");
                                    xsjj.XSJJArgs.put("page", currentPage + 1);
                                    xsjj.pushqqbw();
                                } else {
                                    logger.info("本次数据插入完成,现调用自增长函数");
                                    xsjj.XSJJArgs.put("page", 1);
                                    xsjj.increaseQuery(xsjj.XSJJArgs);
                                    xsjj.pushqqbw();
                                }
                            } else {
                                logger.error("无法从" + xsjj.XSJJArgs.get("query") + "返回报文中获取Data标签内数据,页码为:" + xsjj.XSJJArgs.get("page"));
                            }
                        } else if (sbmessage.jgbwCountRow() == -1) {
                            logger.error("本次请求" + xsjj.XSJJArgs.get("query") + "出错，内容中不包含Row标签,页码为:" + xsjj.XSJJArgs.get("page"));
                            xsjj.XSJJArgs.put("page", (Integer) xsjj.XSJJArgs.get("page") + 1);
                            xsjj.pushqqbw();
                        } else {
                            logger.info("本次请求" + xsjj.XSJJArgs.get("query") + "中不包含数据,页码为:" + xsjj.XSJJArgs.get("page") + "。现调用自增长函数");
                            xsjj.XSJJArgs.put("page", 1);
                            xsjj.increaseQuery(xsjj.XSJJArgs);
                            xsjj.pushqqbw();
                        }
                    } else {
                        logger.error("本次请求" + xsjj.XSJJArgs.get("query") + "出错，返回错误代码04,页码为:" + xsjj.XSJJArgs.get("page"));
                    }
                } else {
                    logger.error(
                            "报文解析失败，重新请求中:查询语句"
                                    + xsjj.XSJJArgs.get("query")
                                    + "请求页数:第"
                                    + xsjj.XSJJArgs.get("page")
                                    + "页"
                    );
                }
            }else {
                logger.error(
                        "省厅数据请求失败，重新请求中:查询语句"
                                + xsjj.XSJJArgs.get("query")
                                + "请求页数:第"
                                + xsjj.XSJJArgs.get("page")
                                + "页"
                );
            }
        }
    }
}
