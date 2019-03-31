package com.example.moviemax;

import java.util.HashMap;

public class Genres {
    public static HashMap<Integer, String> getList()
    {
        HashMap<Integer, String> list = new HashMap<>();
        list.put(28, "Action");
        list.put(12, "Adventure");
        list.put(16, "Animation");
        list.put(35, "Comedy");
        list.put(80, "Crime");
        list.put(99, "Documentary");
        list.put(18, "Drama");
        list.put(10751, "Family");
        list.put(14, "Fantasy");
        list.put(36, "History");
        list.put(27, "Horror");
        list.put(10402, "Music");
        list.put(9648, "Mystery");
        list.put(10749, "Romance");
        list.put(878, "Science Fiction");
        list.put(10770, "TV Movie");
        list.put(53, "Thriller");
        list.put(10752, "War");
        list.put(37, "Western");
        return list;
    }
}
