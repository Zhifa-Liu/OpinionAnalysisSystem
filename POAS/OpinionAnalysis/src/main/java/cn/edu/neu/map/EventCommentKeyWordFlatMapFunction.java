package cn.edu.neu.map;

import cn.edu.neu.bean.Article;
import cn.edu.neu.bean.ArticleComment;
import cn.edu.neu.bean.EventCommentKeyword;
import cn.edu.neu.tool.EventGetUtil;
import cn.edu.neu.tool.NlpUtil;
import cn.edu.neu.tool.StringHandleUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import java.util.*;

/**
 * @author 32098
 */
public class EventCommentKeyWordFlatMapFunction implements FlatMapFunction<Article, EventCommentKeyword> {
    @Override
    public void flatMap(Article article, Collector<EventCommentKeyword> collector) throws Exception {
        List<String> eventNames =  EventGetUtil.getArticleEvents(article.getText());
        for (String eventName : eventNames) {
            for (ArticleComment comment : article.getComments()) {
                if(comment.getCommentText()==null || "".equals(comment.getCommentText())){
                    continue;
                }
                EventCommentKeyword commentKeyword = new EventCommentKeyword();
                List<String> words = NlpUtil.segmentSentence(comment.getCommentText());
                Set<String> set = new HashSet<>(words);
                List<String> keywords = new ArrayList<>();
                List<Integer> wordCounts = new ArrayList<>();
                if(set.size() == words.size() && words.size()!=0){
                    for (String word : words) {
                        keywords.add(word);
                        wordCounts.add(1);
                    }
                }else{
                    Map<String, Integer> map = new HashMap<>();
                    for (String temp : words) {
                        Integer count = map.get(temp);
                        map.put(temp, (count == null) ? 1 : count + 1);
                    }
                    map.forEach((k, v)->{keywords.add(k); wordCounts.add(v);});
                }
                commentKeyword.setEventName(eventName);
                commentKeyword.setCommentIds(Collections.singletonList(comment.getCommentId()));
                commentKeyword.setKeyword(keywords);
                commentKeyword.setWordCount(wordCounts);
                commentKeyword.setTime(StringHandleUtil.getStandardTime(article.getGetTime()));
                collector.collect(commentKeyword);
            }
        }
    }
}
