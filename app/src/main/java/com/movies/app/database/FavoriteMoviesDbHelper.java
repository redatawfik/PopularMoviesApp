package com.movies.app.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.movies.app.database.FavoriteMoviesContract.FavoriteMoviesEntry;


public class FavoriteMoviesDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FavoriteMovies.db";

    public FavoriteMoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FavoriteMoviesEntry.TABLE_NAME + " (" +
                        FavoriteMoviesEntry.COLUMN_MOVIE_Id + " INTEGER," +
                        FavoriteMoviesEntry.COLUMN_MOVIE_TITLE + " TEXT," +
                        FavoriteMoviesEntry.COLUMN_MOVIE_YEAR + " TEXT," +
                        FavoriteMoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT," +
                        FavoriteMoviesEntry.COLUMN_MOVIE_OVERVIEW + " TEXT," +
                        FavoriteMoviesEntry.COLUMN_MOVIE_POSTER_URL + " TEXT, " +
                        FavoriteMoviesEntry.COLUMN_MOVIE_BACKDROP_URL + " TEXT )";

        db.execSQL(SQL_CREATE_ENTRIES);
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FavoriteMoviesEntry.TABLE_NAME;

        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


}
