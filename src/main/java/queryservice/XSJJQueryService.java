package queryservice;


import java.util.HashMap;

/**
 * Created by Lunch on 2017/7/25.
 * 刑释解教查询服务类
 */
public class XSJJQueryService extends BaseQueryService {

    public HashMap<String, Object> XSJJArgs = new HashMap();

    public XSJJQueryService(String query, boolean oldVersion) {
        String[] columns = {
                //"QSBZ",派出所签收意见字段，会导致报错
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
        XSJJArgs.put("columns",columns);
        XSJJArgs.put("name", "贵州*");
        XSJJArgs.put("wsUrl", "http://#");
        XSJJArgs.put("FWBS", "SZNB*");
        XSJJArgs.put("FWDYID", "S*");
        XSJJArgs.put("query", query);
        XSJJArgs.put("totalNum", -1);
        String queryColumn = assembleQueryColumn((String[])XSJJArgs.get("columns"));
        XSJJArgs.put("queryColumn", queryColumn);
        if (!oldVersion) {
            XSJJArgs.put("qqbw",assembleqqbw(XSJJArgs));
        } else {
            XSJJArgs.put("qqbw",assembleqqbw(XSJJArgs,oldVersion));
        }

    }
}

