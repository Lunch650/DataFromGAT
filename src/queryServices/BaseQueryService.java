package queryservice;


import java.util.HashMap;

/**
 * Created by Lunch on 2017/7/25.
 * 查询服务基类
 */
public abstract class BaseQueryService {

    HashMap<String,Object> baseArgMap= new HashMap<>();

    public void assembleQueryColumn(String[] columns){
        //将子类中的需求字段拼装成Data标签包含的字符串
        String queryColumn = "";
        for (String column : columns) {queryColumn += "<Data>" + column + "</Data>\n";}
        baseArgMap.put("queryColumn",queryColumn);
    }

    public String assembleqqbw(HashMap queryArgs){}



}
