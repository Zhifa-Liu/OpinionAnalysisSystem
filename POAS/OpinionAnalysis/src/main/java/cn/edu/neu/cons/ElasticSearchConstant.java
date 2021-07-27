package cn.edu.neu.cons;

import com.alibaba.fastjson.JSONObject;
import org.apache.flink.table.descriptors.Elasticsearch;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * @author 32098
 */
public class ElasticSearchConstant {
    public static final String ES_HOST = "master";
    public static final int ES_PORT = 9200;

    public static final String INDEX_EVENT = "event";

    public static void main(String[] args) throws IOException {
        // Create ES Client
        RestHighLevelClient esClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("master", 9200))
        );

        AcknowledgedResponse deleteIndexResponse = esClient.indices().delete(new DeleteIndexRequest(INDEX_EVENT), RequestOptions.DEFAULT);
        System.out.println(deleteIndexResponse.isAcknowledged());

        CreateIndexResponse createIndexRequest = esClient.indices().create(new CreateIndexRequest(INDEX_EVENT), RequestOptions.DEFAULT);
        System.out.println(createIndexRequest.isAcknowledged());

        PutMappingRequest putMappingRequest = new PutMappingRequest(INDEX_EVENT);
        XContentBuilder xContentBuilder = XContentFactory
                .jsonBuilder()
                .startObject()
                .startObject("properties")
                .startObject("eventName").field("type", "text").field("analyzer", "ik_smart").endObject()
                .startObject("eventHot").field("type", "double").endObject()
                .startObject("eventType").field("type", "text").field("index", "false").endObject()
                .startObject("startTime").field("type", "text").field("index", "false").endObject()
                .startObject("endTime").field("type", "text").field("index", "false").endObject()
                .endObject()
                .endObject();
        putMappingRequest.source(xContentBuilder);
        AcknowledgedResponse acknowledgedResponse = esClient.indices().putMapping(putMappingRequest, RequestOptions.DEFAULT);
        System.out.println(acknowledgedResponse.isAcknowledged());

        // Close ES Client
        esClient.close();
    }
}

