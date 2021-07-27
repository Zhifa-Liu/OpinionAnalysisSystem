package cn.edu.neu.service;

import cn.edu.neu.po.EventElasticSearch;
import cn.edu.neu.repos.EventRepository;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 32098
 */
@Service
public class EventSearchService {
    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;
    @Autowired
    private EventRepository eventRepository;

    public String searchByInput(String input){
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("master", 9200))
        );
        SearchRequest request = new SearchRequest();
        request.indices("event");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.size(1000);
        searchSourceBuilder.query(QueryBuilders.matchQuery("eventName", input));
        request.source(searchSourceBuilder);

//        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder().withMaxResults(1000).withQuery(QueryBuilders.matchQuery("eventName", input));
//        NativeSearchQuery nativeSearchQuery = builder.build();

        Map<String, EventElasticSearch> results = new HashMap<>();
        try {
            SearchResponse response = restHighLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHits hits = response.getHits();
            System.out.println("###:"+hits.getTotalHits());
            for (SearchHit hit : hits) {
                EventElasticSearch event = JSONObject.parseObject(hit.getSourceAsString(), EventElasticSearch.class);
                results.put(event.getEventName().trim(), event);
                System.out.println(event.getEventName());
            }
            System.out.println(results.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(JSONObject.toJSONString(results.values()));
        return JSONObject.toJSONString(results.values());
    }

    public String searchAllEvent(){
        Iterable<EventElasticSearch> results = eventRepository.findAll();
        return JSONObject.toJSONString(results);
    }
}
