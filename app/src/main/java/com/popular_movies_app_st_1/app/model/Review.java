package com.popular_movies_app_st_1.app.model;


import com.google.gson.annotations.SerializedName;

public class Review {


    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
}
