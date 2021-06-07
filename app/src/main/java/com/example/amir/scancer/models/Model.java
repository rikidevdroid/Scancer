package com.example.amir.scancer.models;

import com.google.gson.annotations.SerializedName;

public class Model {
    @SerializedName("msg")
    private String msg;
    @SerializedName("test")
    private String test;

    public String getMessage() {
        return msg;
    }

    public String getTest() {
        return test;
    }

}
