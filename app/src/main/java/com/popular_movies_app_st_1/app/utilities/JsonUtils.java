package com.popular_movies_app_st_1.app.utilities;


import android.util.Log;

import com.popular_movies_app_st_1.app.model.Movie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    private static final String RESULTS = "results";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String POSTER_PATH = "poster_path";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String BACKDROP_PATH = "backdrop_path";


    private static Movie parseMovieJson(String json) {

        Movie movie = null;

        try {

            JSONObject reader = new JSONObject(json);
            String originalTitle = reader.getString(ORIGINAL_TITLE);
            String overview = reader.getString(OVERVIEW);
            String posterPath = reader.getString(POSTER_PATH);
            String releaseDate = reader.getString(RELEASE_DATE);
            double voteAverage = reader.getDouble(VOTE_AVERAGE);

            String backdropPath = reader.getString(BACKDROP_PATH);


            movie = new Movie(originalTitle,
                    overview,
                    posterPath,
                    voteAverage,
                    releaseDate,
                    backdropPath);

        } catch (Exception e) {
            Log.e(TAG, "Error in parsing json" + e);
        }

        return movie;
    }

    public static ArrayList<Movie> getMoviesArrayList(String json) {

       ArrayList<Movie>  moviesArrayList = null;

        try {

            JSONObject reader = new JSONObject(json);
            JSONArray moviesList = reader.getJSONArray(RESULTS);

            if (moviesList != null) {

                moviesArrayList = new ArrayList<>(moviesList.length());


                for (int i = 0; i < moviesList.length(); i++) {
                    moviesArrayList.add( parseMovieJson(moviesList.get(i).toString()));
                }

            }
        } catch (Exception e) {
            Log.e(TAG, e + "");
        }


        return moviesArrayList;
    }


}
