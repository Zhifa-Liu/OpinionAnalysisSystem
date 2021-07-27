package cn.edu.neu.filter;

import cn.edu.neu.bean.Article;
import cn.edu.neu.tool.EventGetUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.FilterFunction;

/**
 * @author 32098
 */
public class JsonFilterFunction implements FilterFunction<String> {

    @Override
    public boolean filter(String s) throws Exception {
        try {
            // 可能会发生异常的语句
            JSONObject json = JSONObject.parseObject(s);
            // 10万+
            int attitudesCount = (int) json.get("attitudes_count");
            int commentsCount = (int) json.get("comments_count");
            int reportsCount = (int) json.get("reports_count");
            int fansCount = (int) json.get("fans_count");
//            if(EventGetUtil.getArticleEvents(String.valueOf(json.get("text"))).size()==0){
//                System.out.println("#\t"+ s);
//                return false;
//            }
        } catch(Exception e) {
            // 处理异常语句
            // System.err.println("Error (Invalid String: " + e.getMessage() + ")：" + s);
            return false;
        }
        return true;
    }
}

