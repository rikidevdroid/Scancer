package com.example.amir.scancer.models;

import com.google.gson.annotations.SerializedName;

public class News {

    @SerializedName("title")
    private String title;
    @SerializedName("urlToImage")
    private String urlToImage;

    @SerializedName("url")
    private String url;

    public String getTitle() {
        return title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getUrl() {
        return url;
    }
}
