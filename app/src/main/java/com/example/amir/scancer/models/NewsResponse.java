package com.example.amir.scancer.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewsResponse {

    @SerializedName("status")
    public String status;
    @SerializedName("totalResults")
    public int totalResults;

    @SerializedName("articles")
    public List<News> news;

}
