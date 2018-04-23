package com.flow.origo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.flow.origo.ElasticConnector.elasticConnector;
import static com.flow.origo.HTMLParser.*;
import static com.flow.origo.JDBConnector.jdbcHandler;

public class Main {
    public static void main(String[] args) {

        List<String> listOfPages;
        List<DataObject> objectList = new ArrayList<>();


        listOfPages = listGetter(args[0].toLowerCase());
        for (String link : listOfPages) {
            try {
                objectList.add(pageParser(link));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        // single case test
        /*
        try {
            objectList.add(pageParser("http://www.origo.hu/techbazis/20180405-innobie-eddie-microsoft-imagine-cup.html"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

        jdbcHandler(objectList, args[0]);
        elasticConnector(objectList, args[0]);
    }
}
