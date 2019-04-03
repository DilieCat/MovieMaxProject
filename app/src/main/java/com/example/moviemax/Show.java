package com.example.moviemax;

import java.io.Serializable;
import java.util.ArrayList;

public class Show implements Serializable {
    private int id;
    private String title;
    private ArrayList<String> genre;
    private String language;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String rating;
    private String voteCount;
    private String backDrop;
    //private Genres genres = new Genres();

    public Show(int id, String title, ArrayList<String> genre, String language, String posterPath, String overview, String releaseDate, String rating, String voteCount, String backDrop) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.voteCount = voteCount;
        this.backDrop = backDrop;
    }

    public String getGenreToString(){
        String genres = "";

        for (int i = 0; i < genre.size(); i++) {
            if(i == genre.size() -1){
                genres += genre.get(i);
            }else {
                genres += genre.get(i) + ", ";
            }

        }

        return genres;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public String getLanguage() {
        return language;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getRating() {
        return rating;
    }

    public String getVoteCount() {
        return voteCount;
    }

    public String getBackDrop() {
        return backDrop;
    }

    @Override
    public String toString() {
        return  "Title: " + title + '\n' +
                "Genre: " + genre.toString() +
                "Language: " + language + '\n' +
                "Overview: " + overview + '\n' +
                "Release date: " + releaseDate;
    }
}

