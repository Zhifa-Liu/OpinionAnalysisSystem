package cn.edu.neu.map;

import cn.edu.neu.bean.Article;
import cn.edu.neu.bean.EventKeyword;
import cn.edu.neu.tool.EventGetUtil;
import cn.edu.neu.tool.NlpUtil;
import cn.edu.neu.tool.StringHandleUtil;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

import java.util.*;

/**
 * @author 32098
 */
public class EventKeywordFlatMapFunction implements FlatMapFunction<Article, EventKeyword> {

    @Override
    public void flatMap(Article article, Collector<EventKeyword> collector) throws Exception {
        List<String> eventNames =  EventGetUtil.getArticleEvents(article.getText());

        // 分词+词频统计：仅针对一篇文章
        List<String> words = NlpUtil.segmentSentence(article.getText());
        Map<String, Integer> map = new HashMap<>();
        for (String temp : words) {
            Integer count = map.get(temp);
            map.put(temp, (count == null) ? 1 : count + 1);
        }
        List<String> keywords = new ArrayList<>();
        List<Integer> wordCounts = new ArrayList<>();
        map.forEach((k, v)->{keywords.add(k); wordCounts.add(v);});

        for (String eventName : eventNames) {
            EventKeyword eventKeyword = new EventKeyword();
            eventKeyword.setEventName(eventName);
            eventKeyword.setArticleIds(Collections.singletonList(article.getBlogId()));
            eventKeyword.setKeyword(keywords);
            eventKeyword.setWordCount(wordCounts);
            eventKeyword.setTime(StringHandleUtil.getStandardTime(article.getGetTime()));
            collector.collect(eventKeyword);
        }
    }
}

