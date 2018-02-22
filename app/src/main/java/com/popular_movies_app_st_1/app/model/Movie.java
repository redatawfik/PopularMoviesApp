package com.popular_movies_app_st_1.app.model;

public class Movie {

    private static final String BASE_URI = "https://image.tmdb.org/t/p/w500";

    private String originalTitle;
    private String overview;
    private String posterImage;
    private double votRange;
    private String releaseDate;
    private String backdropPath;

    public Movie() {
    }

    public Movie(String originalTitle, String overview, String posterImage, double votRange, String releaseDate, String backdropPath) {

        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterImage = BASE_URI + posterImage;
        this.votRange = votRange;
        this.releaseDate = releaseDate;
        this.backdropPath = BASE_URI + backdropPath;
    }

    //setter Methods
    //------------------------------------------------------
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
    }

    public void setVotRange(double votRange) {
        this.votRange = votRange;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    //----------------------------------------------------------


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
