package com.flow.origo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<String> listOfPages;
        List<Article> objectList = new ArrayList<>();
        StringBuilder argBuilder = new StringBuilder();
        for (String arg: args) {
            argBuilder.append(arg).append(" ");
        }
        String arg = argBuilder.toString().trim();
        HTMLParser htmlParser = new HTMLParser();
        JDBConnector jdbConnector = new JDBConnector();
        ElasticConnector elasticConnector = new ElasticConnector();
        listOfPages = htmlParser.articleListGetter(arg.toLowerCase());
        for (String link : listOfPages) {
            try {
                objectList.add(htmlParser.pageParser(link));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        // For MySQL
        jdbConnector.jdbcHandler(objectList, arg);

        // For ElasticSearch
        elasticConnector.indexMapper(arg);
        elasticConnector.elasticConnector(objectList, arg);
    }
}
