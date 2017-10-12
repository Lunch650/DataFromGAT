package multithread;

import dao.DBManager;
import message.SBmessage;
import org.apache.log4j.Logger;
import queryservice.LAJDSQueryService;

/**
 * 社区警务重点人口查询服务执行线程
 * Created by Lunch on 2017/9/26-10:58.
 */
public class LAJDSThread implements Runnable {

    private static Logger logger = Logger.getLogger(LAJDSThread.class);

    @Override
    public void run() {

        logger.info("开始调用立案登记表查询服务");
        LAJDSQueryService lajds = new LAJDSQueryService();
        SBmessage sbmessage = new SBmessage();
        lajds.assembleQuery(lajds.LAJDSArgs);
        lajds.pushqqbw();
        while (true) {
            if (!lajds.jgbw(lajds.LAJDSArgs).equals("null")) {
                //如果有报文返回
                logger.info("请求成功");
                lajds.pushjgbw();
                if (!sbmessage.getRootElement((String) lajds.LAJDSArgs.get("jgbw")).equals("null")) {
                    if (!sbmessage.standardjgbwCode().equals("103")){
                        //如果返回报文中代码不为请求JSON报文不符合规范类型103
                        if (!sbmessage.jgbwCode().equals("04")) {
                        //如果返回报文中代码不为网络错误04
                            if (sbmessage.jgbwCountRow() > 1) {
                            //如果返回报文中Row标签数量大于1
                                if ((sbmessage.jgbwData() != null) && (sbmessage.jgbwData().size() != 0)) {
                                    //如果返回报文中存在Data标签包含的数据内容
                                    DBManager dbm = new DBManager();
                                    dbm.insertData(sbmessage.jgbwData(), (String) lajds.LAJDSArgs.get("tableName"));
                                    logger.info("当前数据库" 
                                            + lajds.LAJDSArgs.get("tableName") 
                                            + "表中共有" + dbm.queryCount((String) lajds.LAJDSArgs.get("tableName")) 
                                            + "条数据");
                                    int totalPage = lajds.pages(sbmessage.jgbwTotalNum(), lajds.LAJDSArgs.get("dataNum"));
                                    int currentPage = (Integer) lajds.LAJDSArgs.get("page");
                                    if (currentPage < totalPage) {
                                        //如果当前页码不是本次查询中的最后一页
                                        logger.info("本次请求共有" + totalPage + "页数据");
                                        logger.info("现在开始请求第" + (currentPage + 1) + "页数据");
                                        lajds.LAJDSArgs.put("page", currentPage + 1);
                                        lajds.pushqqbw();
                                    } else {
                                        logger.info("本次数据插入完成,现调用自增长函数");
                                        lajds.LAJDSArgs.put("page", 1);
                                        lajds.increaseQuery(lajds.LAJDSArgs);
                                        lajds.pushqqbw();
                                    }
                                } else {
                                    logger.error("无法从" + lajds.LAJDSArgs.get("query") + "返回报文中获取Data标签内数据,页码为:" + lajds.LAJDSArgs.get("page"));
                                }
                            } else if (sbmessage.jgbwCountRow() == -1) {
                                logger.error("本次请求" + lajds.LAJDSArgs.get("query") + "出错，内容中不包含Row标签,页码为:" + lajds.LAJDSArgs.get("page"));
                                lajds.LAJDSArgs.put("page", (Integer) lajds.LAJDSArgs.get("page") + 1);
                                lajds.pushqqbw();
                            } else {
                                logger.info("本次请求" + lajds.LAJDSArgs.get("query") + "中不包含数据,页码为:" + lajds.LAJDSArgs.get("page") + "。现调用自增长函数");
                                lajds.LAJDSArgs.put("page", 1);
                                lajds.increaseQuery(lajds.LAJDSArgs);
                                lajds.pushqqbw();
                            }
                        } else {
                            logger.error("本次请求" + lajds.LAJDSArgs.get("query") + "出错，返回错误代码04,页码为:" + lajds.LAJDSArgs.get("page"));
                        }
                    } else{
                        logger.info("本次请求" + lajds.LAJDSArgs.get("query") + "出错，返回错误代码103,页码为:" + lajds.LAJDSArgs.get("page") + "。现跳过该页");
                        lajds.LAJDSArgs.put("page", (Integer) lajds.LAJDSArgs.get("page") + 1);
                        lajds.pushqqbw();
                    }
                } else {
                    logger.error(
                            "报文解析失败，重新请求中:查询语句" 
                                    + lajds.LAJDSArgs.get("query")
                                    + "请求页数:第"
                                    + lajds.LAJDSArgs.get("page")
                                    + "页"
                    );
                }
            } else {
                logger.error(
                        "省厅数据请求失败，重新请求中:查询语句" 
                                + lajds.LAJDSArgs.get("query")
                                + "请求页数:第"
                                + lajds.LAJDSArgs.get("page")
                                + "页"
                );
            }
        }
    }
}
