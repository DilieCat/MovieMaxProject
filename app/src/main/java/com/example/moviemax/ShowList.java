package com.example.moviemax;

import java.util.ArrayList;

public class ShowList {
    private String name;
    private ArrayList<Integer> showNumbers;

    public ShowList(String name) {
        this.name = name;
        showNumbers = new ArrayList<Integer>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Integer> getShowNumbers() {
        return showNumbers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShowNumber(int show) {
        this.showNumbers.add(show);
    }

    public String getShowCount(){
        return "" + showNumbers.size();
    }
}
