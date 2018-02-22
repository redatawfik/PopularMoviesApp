package com.popular_movies_app_st_1.app.model;

public class Movie {

    private static final String BASE_URI = "https://image.tmdb.org/t/p/w500";

    private final String originalTitle;
    private final String overview;
    private final String posterImage;
    private final double votRange;
    private final String releaseDate;
    private final String backdropPath;



    public Movie(String originalTitle, String overview, String posterImage, double votRange, String releaseDate, String backdropPath) {

        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterImage = BASE_URI + posterImage;
        this.votRange = votRange;
        this.releaseDate = releaseDate;
        this.backdropPath = BASE_URI + backdropPath;
    }


    //getter methods
    //-----------------------------------------
    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterImage() {
        return posterImage;
    }

    public double getVotRange() {
        return votRange;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }
    //------------------------------------------


}
