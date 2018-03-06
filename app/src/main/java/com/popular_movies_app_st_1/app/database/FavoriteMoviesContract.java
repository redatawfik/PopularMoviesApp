package com.popular_movies_app_st_1.app.database;


import android.net.Uri;

public class FavoriteMoviesContract {

    public static final String AUTHORITY = "com.popular_movies_app_st_1.app";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITE_MOVIES = "favoriteMovies";


    private FavoriteMoviesContract() {
    }

    public static class FavoriteMoviesEntry {
        public static final String TABLE_NAME = "favoriteMovies";

        public static final String COLUMN_MOVIE_Id = "id";
        public static final String COLUMN_MOVIE_TITLE = "name";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "vot_range";
        public static final String COLUMN_MOVIE_YEAR = "year";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_POSTER_URL = "poster";
        public static final String COLUMN_MOVIE_BACKDROP_URL = "backdrop";


        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

    }
}
