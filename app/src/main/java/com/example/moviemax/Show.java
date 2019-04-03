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
    private String trailerLink;
    //private Genres genres = new Genres();

    public Show(int id, String title, ArrayList<String> genre, String language, String posterPath, String overview) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.posterPath = posterPath;
        this.overview = overview;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        language = language;
    }

    public String getPosterpath() {
        return posterPath;
    }

    public void setPosterpath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTrailerLink() {
        return trailerLink;
    }

    public void setTrailerLink(String trailerLink) {
        this.trailerLink = trailerLink;
    }

    @Override
    public String toString() {
        return  "Title: " + title + '\n' +
                "Genre: " + genre.toString() +
                "Language: " + language + '\n' +
                "Overview: " + overview + '\n';
    }
}

