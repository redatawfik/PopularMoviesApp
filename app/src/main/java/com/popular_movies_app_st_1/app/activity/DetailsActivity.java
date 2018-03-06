package com.popular_movies_app_st_1.app.activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.popular_movies_app_st_1.app.BuildConfig;
import com.popular_movies_app_st_1.app.NonScrollListView;
import com.popular_movies_app_st_1.app.R;
import com.popular_movies_app_st_1.app.adapter.ReviewsAdapter;
import com.popular_movies_app_st_1.app.adapter.TrailerAdapter;
import com.popular_movies_app_st_1.app.model.Movie;
import com.popular_movies_app_st_1.app.model.MovieTrailersResponse;
import com.popular_movies_app_st_1.app.model.Review;
import com.popular_movies_app_st_1.app.model.ReviewsResponse;
import com.popular_movies_app_st_1.app.model.Trailer;
import com.popular_movies_app_st_1.app.rest.ApiClient;
import com.popular_movies_app_st_1.app.rest.ApiInterface;
import com.squareup.picasso.Picasso;
import com.popular_movies_app_st_1.app.database.FavoriteMoviesContract.FavoriteMoviesEntry;

import org.parceler.Parcels;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity {

    private Movie mMovie;
    private List<Review> mReviewsList;
    private List<Trailer> mTrailersList;

    private final Context context = this;



    private static final String API_KEY = BuildConfig.API_KEY;


    @BindView(R.id.tv_movie_name)
    TextView title;
    @BindView(R.id.tv_overview)
    TextView overviewTextView;
    @BindView(R.id.iv_movie_poster)
    ImageView posterImageTextView;
    @BindView(R.id.tv_rate)
    TextView votRangeTextView;
    @BindView(R.id.tv_year)
    TextView releaseDateTextView;
    @BindView(R.id.iv_movie_backdrop)
    ImageView backdropImageView;
    @BindView(R.id.lv_trailers_list)
    NonScrollListView mTrailersListView;
    @BindView(R.id.lv_reviews_list)
    NonScrollListView mReviewsListView;
    @BindView(R.id.favorite_image)
    ImageView favoriteImage;
    @BindView(R.id.pb_videos_loading)
    ProgressBar videosLoadingProgressBar;
    @BindView(R.id.pb_reviews_loading)
    ProgressBar reviewsLoadingProgressBar;
    @BindView(R.id.v_error_image)
    ImageView vErrorImage;
    @BindView(R.id.v_error_text)
    TextView vErrorText;
    @BindView(R.id.r_error_image)
    ImageView rErrorImage;
    @BindView(R.id.r_error_text)
    TextView rErrorText;
    @BindView(R.id.backdrop_pb)
    ProgressBar backdropProgressBar;
    @BindView(R.id.no_trailers_message)
    TextView noTrailersMessage;
    @BindView(R.id.no_reviews_message)
    TextView noReviewsMessage;


    private ReviewsAdapter mReviewsAdapter;
    private TrailerAdapter mTrailerAdapter;

    private ApiInterface apiService;


    private Boolean favoriteImageClicked;
    private SharedPreferences sharedPreferences;
    private static final String MyPREFERENCES = "MyPrefs";
    private static String FAVORITE_STATE_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ButterKnife.bind(this);
        readFromIntent();

        sharedPreferences = this.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        //store movie state( favorite or not) in a key represent movie id
        FAVORITE_STATE_KEY = mMovie.getId().toString();
        favoriteImageClicked = sharedPreferences.getBoolean(FAVORITE_STATE_KEY, false);
        setFavoriteImageBackground();


        mReviewsList = new ArrayList<>();
        mTrailersList = new ArrayList<>();

        apiService = ApiClient.getClient().create(ApiInterface.class);

        getTrailersList();
        mTrailerAdapter = new TrailerAdapter(context, mTrailersList);
        mTrailersListView.setAdapter(mTrailerAdapter);


        getReviewsList();
        mReviewsAdapter = new ReviewsAdapter(context, mReviewsList);
        mReviewsListView.setAdapter(mReviewsAdapter);

        //Handle clicking of trailer to start the video in youtube
        mTrailersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = mTrailersList.get(position).getVideoUri();
                startYoutube(uri);
            }
        });

    }

    //Called when trailer clicked
    private void startYoutube(Uri uri) {

        Intent intent = new Intent(
                Intent.ACTION_VIEW,
                uri);

        startActivity(intent);
    }


    //Read from intent that start the activity to set instance variable mMovie;
    private void readFromIntent() {
        Intent intent = getIntent();
        mMovie = Parcels.unwrap(intent.getParcelableExtra("movie"));

        setDetailsActivityViews();
    }


    //Set views of the activity title, posterImage, backdropImage, votRange from movie object received from MainActivity
    private void setDetailsActivityViews() {

        title.setText(mMovie.getTitle());
        overviewTextView.setText(mMovie.getOverview());
        votRangeTextView.setText(votRangeString());
        releaseDateTextView.setText(mMovie.getReleaseDate());

        Picasso.with(this).load(mMovie.getPosterURL()).into(posterImageTextView);
        Picasso.with(this).load(mMovie.getBackdropURL()).into(backdropImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                backdropProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onError() {

            }
        });


    }

    //Convert vot range form Double to String
    //return vot range in form (X.X/10.0)
    private String votRangeString() {

        DecimalFormat df = new DecimalFormat("#.#");
        String newVotRange = df.format(mMovie.getVoteAverage());
        return newVotRange + "/";
    }


    private void getReviewsList() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<ReviewsResponse> call = apiService.getReviewsList(mMovie.getId(), API_KEY);

        call.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                reviewsLoadingProgressBar.setVisibility(View.GONE);
                List<Review> reviewsList = response.body().getResults();
                if (reviewsList.size() > 0 ) {
                    mReviewsList.addAll(reviewsList);
                    mReviewsAdapter.notifyDataSetChanged();

                } else {
                    showNoReviewsMessage();
                }

            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                showReviewsErrorMessage();
            }
        });
    }


    private void getTrailersList() {

        Call<MovieTrailersResponse> call = apiService.getVideosList(mMovie.getId(), API_KEY);

        call.enqueue(new Callback<MovieTrailersResponse>() {
            @Override
            public void onResponse(Call<MovieTrailersResponse> call, Response<MovieTrailersResponse> response) {
                videosLoadingProgressBar.setVisibility(View.GONE);
                List<Trailer> trailersList = response.body().getResults();
                if (trailersList.size() > 0) {

                    mTrailersList.addAll(trailersList);
                    mTrailerAdapter.notifyDataSetChanged();
                } else {
                    showNoTrailersMessage();
                }


            }

            @Override
            public void onFailure(Call<MovieTrailersResponse> call, Throwable t) {
                showVideosErrorMessage();
            }
        });
    }

    private void showNoTrailersMessage() {
        videosLoadingProgressBar.setVisibility(View.GONE);
        noTrailersMessage.setVisibility(View.VISIBLE);

    }

    private void showNoReviewsMessage() {
        reviewsLoadingProgressBar.setVisibility(View.GONE);
        noReviewsMessage.setVisibility(View.VISIBLE);

    }

    private void showVideosErrorMessage() {
        videosLoadingProgressBar.setVisibility(View.GONE);
        vErrorImage.setVisibility(View.VISIBLE);
        vErrorText.setVisibility(View.VISIBLE);
    }

    private void showReviewsErrorMessage() {
        reviewsLoadingProgressBar.setVisibility(View.GONE);
        rErrorImage.setVisibility(View.VISIBLE);
        rErrorText.setVisibility(View.VISIBLE);
    }


    //Handle click event of favorite button
    //Edit state of favorite button on shared preference when clicked
    public void favoriteClick(View view) {

        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (!favoriteImageClicked) {
            editor.putBoolean(FAVORITE_STATE_KEY, true).apply();
            favoriteImageClicked = true;
            setFavoriteImageBackground();
            insertMovieIntoDatabase();

        } else {
            editor.putBoolean(FAVORITE_STATE_KEY, false).apply();
            favoriteImageClicked = false;
            setFavoriteImageBackground();
            deleteMovieFromDatabase();


        }


    }

    //Check current state of favorite button to set background
    //Called on onCreate method and when favorite button clicked
    private void setFavoriteImageBackground() {

        if (!favoriteImageClicked) {
            favoriteImage.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        } else {
            favoriteImage.setImageResource(R.drawable.ic_favorite_black_24dp);
        }
    }

    //Method delete called and insert movie from database
    //when favorite button clicked
    private void insertMovieIntoDatabase() {

        ContentValues values = new ContentValues();

        values.put(FavoriteMoviesEntry.COLUMN_MOVIE_Id, mMovie.getId());
        values.put(FavoriteMoviesEntry.COLUMN_MOVIE_TITLE, mMovie.getTitle());
        values.put(FavoriteMoviesEntry.COLUMN_MOVIE_YEAR, mMovie.getReleaseDate());
        values.put(FavoriteMoviesEntry.COLUMN_MOVIE_VOTE_AVERAGE, mMovie.getVoteAverage());
        values.put(FavoriteMoviesEntry.COLUMN_MOVIE_OVERVIEW, mMovie.getOverview());
        values.put(FavoriteMoviesEntry.COLUMN_MOVIE_POSTER_URL, mMovie.getPosterPath());
        values.put(FavoriteMoviesEntry.COLUMN_MOVIE_BACKDROP_URL, mMovie.getBackdropPath());

        getContentResolver().insert(FavoriteMoviesEntry.CONTENT_URI, values);

        Toast.makeText(this, "Movie added to favorites", Toast.LENGTH_SHORT).show();


    }

    //Method delete called and delete movie from database
    //when favorite button clicked
    private void deleteMovieFromDatabase() {


        String movieId = mMovie.getId().toString();
        Uri uri = FavoriteMoviesEntry.CONTENT_URI.buildUpon().appendPath(movieId).build();
        getContentResolver().delete(uri, null, null);

        Toast.makeText(this, "Movie deleted from favorites", Toast.LENGTH_SHORT).show();

    }


}
