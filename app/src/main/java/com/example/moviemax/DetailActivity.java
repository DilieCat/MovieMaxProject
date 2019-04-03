package com.example.moviemax;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import static com.example.moviemax.MainActivity.EXTRA_DESCRIPTION;
import static com.example.moviemax.MainActivity.EXTRA_GENRE;
import static com.example.moviemax.MainActivity.EXTRA_POSTERPATH;
import static com.example.moviemax.MainActivity.EXTRA_TITLE;


public class DetailActivity extends AppCompatActivity {

    private TextView titleView;
    private TextView descriptionView;
    private TextView genresView;
    private TextView ratingView;
    private TextView releaseDateView;
    private ImageView posterView;
    private Intent intent;
    private Show show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        intent = getIntent();
        String showJson = intent.getStringExtra("Show");
        show = new Gson().fromJson(showJson, Show.class);

        posterView = findViewById(R.id.image_view_detail);
        titleView = findViewById(R.id.text_view_detail_title);
        genresView = findViewById(R.id.text_view_detail_genre);
        descriptionView = findViewById(R.id.text_view_detail_description);
        ratingView = findViewById(R.id.text_view_detail_rating);
        releaseDateView = findViewById(R.id.text_view_detail_release_date);

        Glide.with(this).load(show.getBackDrop()).centerCrop().into(posterView);
        titleView.setText(show.getTitle());
        descriptionView.setText("Description: \n" + show.getOverview());
        genresView.setText("Genres: \n" + show.getGenreToString());
        ratingView.setText("Rating: " + show.getRating() + " (" + show.getVoteCount() + ")");
        releaseDateView.setText("Release date: " + show.getReleaseDate());
    }
}
