package com.flow.origo;

public class Article {
    private String title;
    private String author;
    private String date;
    private String additionalTags;
    private String content;

    public Article(String title, String author, String date, String additionalTags, String content) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.additionalTags = additionalTags;
        this.content = content;
    }

    public String getTitle() {
        title = title.replace("'", "\\'");
        return title;
    }

    public String getAuthor() {
        author = author.replace("'", "\\'");
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getAdditionalTags() {
        additionalTags = additionalTags.replace("'", "\\'");
        return additionalTags;
    }

    public String getContent() {
        content = content.replace("'", "\\'");
        return content;
    }

    @Override
    public String toString() {
        return "Article{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", additionalTags='" + additionalTags + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
