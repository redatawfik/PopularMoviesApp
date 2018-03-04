package com.popular_movies_app_st_1.app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailersResponse {

    @SerializedName("results")
    private List<Trailer> results;


    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
