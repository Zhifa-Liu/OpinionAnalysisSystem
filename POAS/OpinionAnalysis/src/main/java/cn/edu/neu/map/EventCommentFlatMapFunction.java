package cn.edu.neu.map;

import cn.edu.neu.bean.Article;
import cn.edu.neu.bean.ArticleComment;
import cn.edu.neu.bean.EventComment;
import cn.edu.neu.tool.EventGetUtil;
import cn.edu.neu.tool.NlpUtil;
import cn.edu.neu.tool.StringHandleUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import java.util.List;

/**
 * @author 32098
 */
public class EventCommentFlatMapFunction implements FlatMapFunction<Article, EventComment> {
    @Override
    public void flatMap(Article article, Collector<EventComment> collector) throws Exception {
        List<String> eventNames =  EventGetUtil.getArticleEvents(article.getText());
        for (String eventName : eventNames) {
            for (ArticleComment comment : article.getComments()) {
                if(comment.getCommentText()==null || "".equals(comment.getCommentText())){
                    continue;
                }
                EventComment eventComment = new EventComment();
                eventComment.setEventName(eventName);
                eventComment.setCommentId(comment.getCommentId()!=null?comment.getCommentId():"unknown");
                eventComment.setCommentText(comment.getCommentText());
                eventComment.setCommentReply(comment.getCommentReply()!=null?comment.getCommentReply():0);
                eventComment.setCommentLike(comment.getCommentLike()!=null?comment.getCommentLike():0);
                eventComment.setCommentEmotion(NlpUtil.textEmotion(comment.getCommentText()));
                eventComment.setCommenterGender(comment.getCommenterGender()!=null?comment.getCommenterGender():"unknown");
                eventComment.setSpiderTime(StringHandleUtil.getStandardTime(article.getGetTime()));
                collector.collect(eventComment);
            }
        }
    }
}