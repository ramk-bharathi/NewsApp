package me.ramk.newsapp.models;

/**
 * Created by Ram K Bharathi on 2/25/2018.
 */

public class News {
    private int id;
    private String photo_url;
    private String headline;
    private String short_description;
    private String content;
    private String news_state;
    private String news_district;

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNews_state() {
        return news_state;
    }

    public void setNews_state(String news_state) {
        this.news_state = news_state;
    }

    public String getNews_district() {
        return news_district;
    }

    public void setNews_district(String news_district) {
        this.news_district = news_district;
    }
}
