package com.popular_movies_app_st_1.app.model;


import android.net.Uri;

import com.google.gson.annotations.SerializedName;

public class Trailer {

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    @SerializedName("id")
    private String id;


    @SerializedName("key")
    private String key;


    @SerializedName("name")
    private String name;


    public String getId() {
        return id;
    }

    public Uri getVideoUri() {
        return Uri.parse(YOUTUBE_BASE_URL + key);
    }

    public String getName() {
        return name;
    }
}
