package com.flow.origo;

import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticConnector {

    public static void elasticConnector(List<DataObject> input, String tag) {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")
                ));

        IndexRequest indexRequest = new IndexRequest(tag, "doc");

        for (DataObject object: input) {
            Map<String, String> jsonMap = new HashMap<>();
            jsonMap.put("title", object.getTitle());
            jsonMap.put("author", object.getAuthor());
            jsonMap.put("date", object.getDate());
            jsonMap.put("additionalTags", object.getAdditionalTags());
            jsonMap.put("content", object.getContent());
            try {
                client.index(indexRequest.source(jsonMap));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
