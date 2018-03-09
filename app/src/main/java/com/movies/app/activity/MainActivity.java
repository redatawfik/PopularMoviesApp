package com.movies.app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.movies.app.BuildConfig;
import com.movies.app.R;
import com.movies.app.adapter.MovieAdapter;
import com.movies.app.database.FavoriteMoviesContract;
import com.movies.app.model.Movie;
import com.movies.app.model.MoviesResponse;
import com.movies.app.rest.ApiClient;
import com.movies.app.rest.ApiInterface;


import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String API_KEY = BuildConfig.API_KEY;

    @BindView(R.id.rv_movies)
    RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading)
    ProgressBar progressBar;
    @BindView(R.id.tv_error_message)
    TextView errorMessageTextView;


    private static final String TOP_RATED = "top_rated";
    private static final String MOST_POPULAR = "popular";
    private static final String UPCOMING = "upcoming";
    private static final String FAVORITE = "favorite";
    private String sortBy;
    private static final String SORT_ORDER_KEY = "sortOrderKey";

    private final Context context = this;
    private List<Movie> mMoviesList;
    private MovieAdapter mMovieAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        if (savedInstanceState != null && savedInstanceState.containsKey(SORT_ORDER_KEY)) {
            sortBy = savedInstanceState.getString(SORT_ORDER_KEY);
        } else {
            sortBy = MOST_POPULAR;
        }


        mMoviesList = new ArrayList<>();

        int numberOfColumn = 2;

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            numberOfColumn = 2;
        } else {
            numberOfColumn = 3;
        }


        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumn);
        mRecyclerView.setLayoutManager(layoutManager);

        mMovieAdapter = new MovieAdapter(mMoviesList, context, (MovieAdapter.MovieAdapterOnClickHandler) context);

        if (sortBy.equals(FAVORITE)) {
            readFavoriteMoviesFromSQLite();
            mRecyclerView.setAdapter(mMovieAdapter);
        }else{

            getResponseRetrofit();
        }


    }


    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        errorMessageTextView.setVisibility(View.VISIBLE);
    }

    private void showMoviesList() {
        mRecyclerView.setVisibility(View.VISIBLE);
        errorMessageTextView.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onClick(int position) {

        Intent intent = new Intent(this, DetailsActivity.class);

        intent.putExtra("movie", Parcels.wrap(mMoviesList.get(position)));
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.most_popular_action) {

            mMovieAdapter.setMoviesArray();
            sortBy = MOST_POPULAR;
            getResponseRetrofit();
            return true;

        } else if (itemId == R.id.top_rated_action) {

            mMovieAdapter.setMoviesArray();
            sortBy = TOP_RATED;
            getResponseRetrofit();
            return true;

        } else if (itemId == R.id.upcoming_action) {

            mMovieAdapter.setMoviesArray();
            sortBy = UPCOMING;
            getResponseRetrofit();
            return true;

        } else if (itemId == R.id.favorite_action) {
            sortBy = FAVORITE;
            mMovieAdapter.setMoviesArray();
            showMoviesList();
            readFavoriteMoviesFromSQLite();
            progressBar.setVisibility(View.INVISIBLE);
            mRecyclerView.setAdapter(mMovieAdapter);

            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }


    }


    private void getResponseRetrofit() {

        progressBar.setVisibility(View.VISIBLE);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MoviesResponse> call = apiService.getMoviesList(sortBy, API_KEY);

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                showMoviesList();

                mMoviesList.addAll(response.body().getResults());
                progressBar.setVisibility(View.INVISIBLE);
                mRecyclerView.setAdapter(mMovieAdapter);

            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {

                progressBar.setVisibility(View.INVISIBLE);
                showErrorMessage();

            }
        });
    }


    private void readFavoriteMoviesFromSQLite() {

        Cursor cursor = getContentResolver().query(
                FavoriteMoviesContract.FavoriteMoviesEntry.CONTENT_URI,
                null,
                null,
                null,
                null);

        if (cursor != null && cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {

                Movie movie = new Movie();

                movie.setId(cursor.getInt(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_Id)));
                movie.setTitle(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_TITLE)));
                movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_YEAR)));
                movie.setOverview(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_OVERVIEW)));
                movie.setVoteAverage(Double.parseDouble(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE))));
                movie.setPosterPath(cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_POSTER_URL)));
                movie.setBackdropPath((cursor.getString(cursor.getColumnIndex(FavoriteMoviesContract.FavoriteMoviesEntry.COLUMN_MOVIE_BACKDROP_URL))));

                mMoviesList.add(movie);

                cursor.moveToNext();
            }
        }

        if (cursor != null) {
            cursor.close();
        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SORT_ORDER_KEY, sortBy);
    }
}
