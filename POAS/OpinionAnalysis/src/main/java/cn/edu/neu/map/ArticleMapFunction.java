package cn.edu.neu.map;

import cn.edu.neu.bean.Article;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * @author 32098
 */
public class ArticleMapFunction implements MapFunction<String, Article> {
    @Override
    public Article map(String s) throws Exception {
        // System.out.println(s);
        try{
            return JSONObject.parseObject(s).toJavaObject(Article.class);
        }catch (Exception e){
            return null;
        }
    }
}
