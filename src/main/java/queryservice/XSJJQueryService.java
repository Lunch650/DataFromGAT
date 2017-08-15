package queryservice;


import java.util.HashMap;

/**
 * Created by Lunch on 2017/7/25.
 * 刑释解教查询服务类
 */
public class XSJJQueryService extends BaseQueryService {

    public HashMap<String, Object> XSJJArgs = new HashMap();

    public XSJJQueryService(String query, boolean oldVersion) {
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



