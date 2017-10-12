package multithread;

import dao.DBManager;
import message.SBmessage;
import org.apache.log4j.Logger;

import queryservice.ZDRKQueryService;


/**
 * 社区警务重点人口查询服务执行线程
 * Created by Lunch on 2017/9/26-10:58.
 */
public class ZDRKThread implements Runnable {

    private static Logger logger = Logger.getLogger(ZDRKThread.class);

    @Override
    public void run() {

        logger.info("开始调用社区警务重点人口查询服务");
        ZDRKQueryService zdrk = new ZDRKQueryService();
        SBmessage sbmessage = new SBmessage();
        zdrk.assembleQuery(zdrk.ZDRKArgs);
        zdrk.pushqqbw();
        while (true) {
            if (!zdrk.jgbw(zdrk.ZDRKArgs).equals("null")) {
                //如果有报文返回
                logger.info("请求成功");
                zdrk.pushjgbw();
                if (!sbmessage.getRootElement((String) zdrk.ZDRKArgs.get("jgbw")).equals("null")) {
                    if (!sbmessage.jgbwCode().equals("04")) {
                        //如果返回报文中代码不为网络错误04
                        if (sbmessage.jgbwCountRow() > 1) {
                            //如果返回报文中Row标签数量大于1
                            if ((sbmessage.jgbwData() != null) && (sbmessage.jgbwData().size() != 0)) {
                                //如果返回报文中存在Data标签包含的数据内容
                                DBManager dbm = new DBManager();
                                dbm.insertData(sbmessage.jgbwData(), (String) zdrk.ZDRKArgs.get("tableName"));
                                logger.info("当前数据库"
                                        + zdrk.ZDRKArgs.get("tableName")
                                        + "表中共有" + dbm.queryCount((String) zdrk.ZDRKArgs.get("tableName"))
                                        + "条数据");
                                int totalPage = zdrk.pages(sbmessage.jgbwTotalNum(), zdrk.ZDRKArgs.get("dataNum"));
                                int currentPage = (Integer) zdrk.ZDRKArgs.get("page");
                                if (currentPage < totalPage) {
                                    //如果当前页码不是本次查询中的最后一页
                                    logger.info("本次请求共有" + totalPage + "页数据");
                                    logger.info("现在开始请求第" + (currentPage + 1) + "页数据");
                                    zdrk.ZDRKArgs.put("page", currentPage + 1);
                                    zdrk.pushqqbw();
                                } else {
                                    logger.info("本次数据插入完成,现调用自增长函数");
                                    zdrk.ZDRKArgs.put("page", 1);
                                    zdrk.increaseQuery(zdrk.ZDRKArgs);
                                    zdrk.pushqqbw();
                                }
                            } else {
                                logger.error("无法从" + zdrk.ZDRKArgs.get("query") + "返回报文中获取Data标签内数据,页码为:" + zdrk.ZDRKArgs.get("page"));
                            }
                        } else if (sbmessage.jgbwCountRow() == -1) {
                            logger.error("本次请求" + zdrk.ZDRKArgs.get("query") + "出错，内容中不包含Row标签,页码为:" + zdrk.ZDRKArgs.get("page"));
                            zdrk.ZDRKArgs.put("page", (Integer) zdrk.ZDRKArgs.get("page") + 1);
                            zdrk.pushqqbw();
                        } else {
                            logger.info("本次请求" + zdrk.ZDRKArgs.get("query") + "中不包含数据,页码为:" + zdrk.ZDRKArgs.get("page") + "。现调用自增长函数");
                            zdrk.ZDRKArgs.put("page", 1);
                            zdrk.increaseQuery(zdrk.ZDRKArgs);
                            zdrk.pushqqbw();
                        }
                    } else {
                        logger.error("本次请求" + zdrk.ZDRKArgs.get("query") + "出错，返回错误代码04,页码为:" + zdrk.ZDRKArgs.get("page"));
                    }
                } else {
                    logger.error(
                            "报文解析失败，重新请求中:查询语句"
                                    + zdrk.ZDRKArgs.get("query")
                                    + "请求页数:第"
                                    + zdrk.ZDRKArgs.get("page")
                                    + "页"
                    );
                }
            } else {
                logger.error(
                        "省厅数据请求失败，重新请求中:查询语句"
                                + zdrk.ZDRKArgs.get("query")
                                + "请求页数:第"
                                + zdrk.ZDRKArgs.get("page")
                                + "页"
                );
            }
        }
    }
}
