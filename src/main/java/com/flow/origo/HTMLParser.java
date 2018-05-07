package com.flow.origo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HTMLParser {
    public Article pageParser(String page) throws IOException {
        Document data;
        String title;
        String author;
        String pubDate;
        String additionalTags;
        String content;

        StringBuilder contentBuild = new StringBuilder();
        StringBuilder additionalTagsBuild = new StringBuilder();

        Article object;

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
            pubDate = "1000.01.01. 00:00";
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


        object = new Article(title, author, pubDate, additionalTags, content);
        return object;

    }

    public List<String> articleListGetter(String tag) {
        List<String> pageList = new ArrayList<>();
        Document conn = null;
        try {
            conn = Jsoup.connect("http://cimkezes.origo.hu/cimkek/" + removeAccents(tag) + "/index.html").get();
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
            extConn = Jsoup.connect("http://cimkezes.origo.hu/cimkek/" + removeAccents(tag) + "/index.html?tag=" + replaceAccents(tag) + "&hits=10&offset=" + osc).get();
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
                extConn = Jsoup.connect("http://cimkezes.origo.hu/cimkek/" + removeAccents(tag) + "/index.html?tag=" + replaceAccents(tag) + "&hits=10&offset=" + osc).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pageList;
    }


    public String removeAccents(String in) {
        return in
                .replace(" ","-")
                .replace("á","a")
                .replace("é","e")
                .replace("í","i")
                .replace("ó","o")
                .replace("ö","o")
                .replace("ő","o")
                .replace("ú","u")
                .replace("ü","u")
                .replace("ű","u");
    }

    public String replaceAccents(String in) {
        return in
                .replace(" ","%20")
                .replace(".",".+")
                .replace(":","%3A")
                .replace("á","%E1")
                .replace("é","%E9")
                .replace("í","%ED")
                .replace("ó","%F3")
                .replace("ö","%F6")
                .replace("ő","%F5")
                .replace("ú","%FA")
                .replace("ü","%FC")
                .replace("ű","%FB");
    }
}
