package com.flow.origo;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ElasticConnector {
    private final String PATH = "./src/main/resources/";
    private HTMLParser htmlParser = new HTMLParser();

    public void indexMapper(String tag) {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")
                ));

        File from = new File(PATH + "template.json");
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new FileReader(from));
            String line = bufferedReader.readLine();

            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        CreateIndexRequest createIndexRequest = new CreateIndexRequest(htmlParser.removeAccents(tag));
        createIndexRequest.mapping("doc",stringBuilder.toString(), XContentType.JSON);
        try {
            client.indices().create(createIndexRequest);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void elasticConnector(List<Article> input, String tag) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy.MM.dd. HH:mm");
        Date date = null;


        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")
                ));

        IndexRequest indexRequest = new IndexRequest(htmlParser.removeAccents(tag), "doc");
        for (Article article : input) {
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("title", article.getTitle());
            jsonMap.put("author", article.getAuthor());
            try {
                date = parser.parse(article.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            jsonMap.put("date", date);
            jsonMap.put("additionalTags", article.getAdditionalTags());
            jsonMap.put("content", article.getContent());
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
