package com.flow.origo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HTMLParser {
    public static DataObject pageParser(String page) throws IOException {
        Document data = null;
        String title;
        String author;
        String pubDate;
        String additionalTags;
        String content;

        StringBuilder contentBuild = new StringBuilder();
        StringBuilder additionalTagsBuild = new StringBuilder();

        DataObject object;

        data = Jsoup.connect(page).get();

        try {
            title = data.getElementById("article-head").getElementsByTag("h1").first().text();
        } catch (Exception e) {
            title = "-";
        }

        try {
            author = data.body().getElementsByAttributeValue("rel", "author").text();
        } catch (Exception e) {
            author = "-";
        }

        try {
            pubDate = data.getElementById("article-date").text();
        } catch (Exception e) {
            pubDate = "1000-01-01 00:00";
        }

        try {
            for (String tags : data.getElementsByClass("article-tags").first().getElementsByTag("a").eachAttr("title")) {
                additionalTagsBuild.append(tags).append(" ");
            }
            additionalTags = additionalTagsBuild.toString().trim();
        } catch (Exception e) {
            additionalTags = "-";
        }

        try {
            for (String cont : data.getElementById("article-text").getElementsByTag("p").eachText()) {
                if (!cont.contains(data.getElementById("article-text").getElementsByTag("strong").first().text())) {
                    contentBuild.append(cont).append(" ");
                }
            }
            content = contentBuild.toString().trim();
        } catch (Exception e) {
            content = "-";
        }


        object = new DataObject(title, author, pubDate, additionalTags, content);
        return object;

    }

    public static List<String> listGetter(String tag) {
        List<String> pageList = new ArrayList<>();
        Document conn = null;
        try {
            conn = Jsoup.connect("http://cimkezes.origo.hu/cimkek/" + tag + "/index.html").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element header = conn.getElementById("wrap-left").getElementsByClass("top-item").first().getElementsByTag("a").first();
        pageList.add(header.attr("href"));
        List<Element> linkList = new ArrayList<>(conn.getElementById("wrap-main").getElementsByClass("news-title"));

        for (Element link: linkList) {
            pageList.add(link.attr("href"));
        }

        int osc = 10;
        Document extConn = null;
        try {
            extConn = Jsoup.connect("http://cimkezes.origo.hu/cimkek/" + tag + "/index.html?tag=" + tag + "&hits=10&offset=" + osc).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!(extConn.getElementById("wrap-main").selectFirst("p").text().contains("Az Ön által megadott"))) {
            List<Element> extList = new ArrayList<>(extConn.getElementById("wrap-main").getElementsByClass("news-title"));
            for (Element extLink: extList) {
                pageList.add(extLink.attr("href"));
            }
            osc += 10;
            try {
                extConn = Jsoup.connect("http://cimkezes.origo.hu/cimkek/" + tag + "/index.html?tag=" + tag + "&hits=10&offset=" + osc).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pageList;
    }
}
