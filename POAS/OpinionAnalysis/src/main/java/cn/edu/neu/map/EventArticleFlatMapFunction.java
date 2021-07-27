package cn.edu.neu.map;

import cn.edu.neu.bean.Article;
import cn.edu.neu.bean.EventArticle;
import cn.edu.neu.tool.EventGetUtil;
import cn.edu.neu.tool.StringHandleUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import java.util.List;

/**
 * @author 32098
 *
 */
public class EventArticleFlatMapFunction implements FlatMapFunction<Article, EventArticle> {
    private long count;

    @Override
    public void flatMap(Article article, Collector<EventArticle> collector) throws Exception {
        List<String> eventNames =  EventGetUtil.getArticleEvents(article.getText());
        count++;
        System.out.println(count);
        for (String eventName : eventNames) {
            EventArticle eventArticle = new EventArticle();
            eventArticle.setEventName(eventName);
            eventArticle.setField(article.getField());
            eventArticle.setArticleId(article.getBlogId()!=null?article.getBlogId():"unknown");
            eventArticle.setArticleAuthor(article.getUserName()!=null?article.getUserName():"unknown");
            eventArticle.setAuthorId(article.getUserId()!=null?article.getUserId():"unknown");
            eventArticle.setAuthorGender(article.getGender()!=null?article.getGender():"unknown");
            eventArticle.setAuthorType(article.getUserType()!=null?article.getUserType():"unknown");
            if(article.getCreateDate()!=null){
                eventArticle.setArticleTime(StringHandleUtil.getStandardTime(article.getCreateDate()));
            }else {
                eventArticle.setArticleTime("unknown");
            }
            eventArticle.setArticleText(article.getText()!=null?article.getText():"none");
            eventArticle.setAttitudesCount(article.getAttitudesCount());
            eventArticle.setCommentsCount(article.getCommentsCount());
            eventArticle.setReportsCount(article.getReportsCount());
            eventArticle.setArticleHot(0.2*article.getAttitudesCount()+0.3*article.getCommentsCount()+0.5*article.getReportsCount());
            eventArticle.setArticleRegion(StringHandleUtil.getProvince(article.getLocation()!=null?article.getLocation():"unknown"));
            eventArticle.setSpiderTime(StringHandleUtil.getStandardTime(article.getGetTime()));
            collector.collect(eventArticle);
        }
    }
}


