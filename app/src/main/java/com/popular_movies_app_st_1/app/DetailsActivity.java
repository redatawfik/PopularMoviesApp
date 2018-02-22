package com.popular_movies_app_st_1.app;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class DetailsActivity extends AppCompatActivity {

    private TextView originalTitleTextView;
    private TextView overviewTextView;
    private ImageView posterImageTextView;
    private TextView votRangeTextView;
    private TextView releaseDateTextView;
    private ImageView backdropImageView;


    private String originalTitle;
    private String overview;
    private String posterImage;
    private double votRange;
    private String releaseDate;
    private String backdrop;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        originalTitleTextView = findViewById(R.id.tv_movie_name);
        overviewTextView = findViewById(R.id.tv_overview);
        votRangeTextView = findViewById(R.id.tv_rate);
        releaseDateTextView = findViewById(R.id.tv_year);
        posterImageTextView = findViewById(R.id.iv_movie_poster);
        backdropImageView = findViewById(R.id.iv_movie_backdrop);

        setIntentValues();
        setDetailsActivityViews();

    }

    private void setIntentValues() {

        Intent intent = getIntent();

        if (intent.hasExtra("originalTitle")) {
            originalTitle = intent.getStringExtra("originalTitle");
            overview = intent.getStringExtra("overview");
            posterImage = intent.getStringExtra("posterImage");
            votRange = intent.getDoubleExtra("votRange", 0.0);
            releaseDate = intent.getStringExtra("releaseDate");
            backdrop = intent.getStringExtra("backdropPath");
        }


    }

    private void setDetailsActivityViews() {

        originalTitleTextView.setText(originalTitle);
        overviewTextView.setText(overview);
        votRangeTextView.setText(votRangeString());
        releaseDateTextView.setText(releaseDate.subSequence(0, 4));

        Picasso.with(this).load(posterImage).into(posterImageTextView);
        Picasso.with(this).load(backdrop).into(backdropImageView);


    }

    private String votRangeString() {

        DecimalFormat df = new DecimalFormat("#.#");
        String newVotRange = df.format(votRange);
        return newVotRange + getString(R.string.rate_percent);
    }


}
