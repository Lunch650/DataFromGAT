package multithread;

import dao.DBManager;
import message.SBmessage;
import org.apache.log4j.Logger;
import queryservice.PADJBQueryService;


/**
 * 社区警务重点人口查询服务执行线程
 * Created by Lunch on 2017/9/26-10:58.
 */
public class PADJBThread implements Runnable {

    private static Logger logger = Logger.getLogger(PADJBThread.class);

    @Override
    public void run() {

        logger.info("开始调用破案登记表查询服务");
        PADJBQueryService padjb = new PADJBQueryService();
        SBmessage sbmessage = new SBmessage();
        padjb.assembleQuery(padjb.PADJBArgs);
        padjb.pushqqbw();
        while (true) {
            if (!padjb.jgbw(padjb.PADJBArgs).equals("null")) {
                //如果有报文返回
                logger.info("请求成功!查询语句为" + padjb.PADJBArgs.get("query"));
                padjb.pushjgbw();
                if (!sbmessage.getRootElement((String) padjb.PADJBArgs.get("jgbw")).equals("null")) {
                    if (!sbmessage.standardjgbwCode().equals("103")) {
                        if (!sbmessage.standardjgbwCode().equals("04")) {
                            //如果返回报文中代码不为网络错误04
                            if (sbmessage.standardjgbwCountRow() > 1) {
                                //如果返回报文中Row标签数量大于1
                                if ((sbmessage.standardjgbwData() != null) && (sbmessage.standardjgbwData().size() != 0)) {
                                    //如果返回报文中存在Data标签包含的数据内容
                                    DBManager dbm = new DBManager();
                                    dbm.insertData(sbmessage.standardjgbwData(), (String) padjb.PADJBArgs.get("tableName"));
                                    logger.info("当前数据库"
                                            + padjb.PADJBArgs.get("tableName")
                                            + "表中共有" + dbm.queryCount((String) padjb.PADJBArgs.get("tableName"))
                                            + "条数据");
                                    int totalPage = padjb.pages(sbmessage.standardjgbwTotalNum(), padjb.PADJBArgs.get("dataNum"));
                                    int currentPage = (Integer) padjb.PADJBArgs.get("page");
                                    if (currentPage < totalPage) {
                                        //如果当前页码不是本次查询中的最后一页
                                        logger.info("本次请求共有" + totalPage + "页,共"+sbmessage.standardjgbwTotalNum()+"条数据");
                                        logger.info("现在开始请求第" + (currentPage + 1) + "页数据");
                                        padjb.PADJBArgs.put("page", currentPage + 1);
                                        padjb.pushqqbw();
                                    } else {
                                        logger.info("本次数据插入完成,现调用自增长函数");
                                        padjb.PADJBArgs.put("page", 1);
                                        padjb.increaseQuery(padjb.PADJBArgs);
                                        padjb.pushqqbw();
                                    }
                                } else {
                                    logger.error("无法从" + padjb.PADJBArgs.get("query") + "返回报文中获取Data标签内数据,页码为:" + padjb.PADJBArgs.get("page"));
                                }
                            } else if (sbmessage.standardjgbwCountRow() == -1) {
                                logger.info("jgbwCountRow：" + sbmessage.standardjgbwCountRow());
                                logger.error("本次请求" + padjb.PADJBArgs.get("query") + "出错，内容中不包含Row标签,页码为:" + padjb.PADJBArgs.get("page"));
                                padjb.PADJBArgs.put("page", (Integer) padjb.PADJBArgs.get("page") + 1);
                                padjb.pushqqbw();
                            } else {
                                logger.info("本次请求" + padjb.PADJBArgs.get("query") + "中不包含数据,页码为:" + padjb.PADJBArgs.get("page") + "。现调用自增长函数");
                                padjb.PADJBArgs.put("page", 1);
                                padjb.increaseQuery(padjb.PADJBArgs);
                                padjb.pushqqbw();
                            }
                        } else {
                            logger.error("本次请求" + padjb.PADJBArgs.get("query") + "出错，返回错误代码04,页码为:" + padjb.PADJBArgs.get("page"));
                        }
                    } else{
                        logger.info("本次请求" + padjb.PADJBArgs.get("query") + "出错，返回错误代码103,页码为:" + padjb.PADJBArgs.get("page") + "。现跳过该页");
                        padjb.PADJBArgs.put("page", (Integer) padjb.PADJBArgs.get("page") + 1);
                        padjb.pushqqbw();
                    }
                } else {
                    logger.error(
                            "报文解析失败，重新请求中:查询语句"
                                    + padjb.PADJBArgs.get("query")
                                    + "请求页数:第"
                                    + padjb.PADJBArgs.get("page")
                                    + "页"
                    );
                }
            } else {
                logger.error(
                        "省厅数据请求失败，重新请求中:查询语句"
                                + padjb.PADJBArgs.get("query")
                                + "请求页数:第"
                                + padjb.PADJBArgs.get("page")
                                + "页"
                );
            }
        }
    }
}
