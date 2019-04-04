package com.example.moviemax;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import static com.example.moviemax.MainActivity.EXTRA_DESCRIPTION;
import static com.example.moviemax.MainActivity.EXTRA_GENRE;
import static com.example.moviemax.MainActivity.EXTRA_POSTERPATH;
import static com.example.moviemax.MainActivity.EXTRA_TITLE;


public class DetailActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView titleView;
    private TextView descriptionView;
    private TextView genresView;
    private TextView ratingView;
    private TextView releaseDateView;
    private ImageView posterView;
    private Intent intent;
    private Show show;
    private Button shareBtn;

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
        shareBtn = findViewById(R.id.image_view_detail_share_button);

        shareBtn.setOnClickListener(this);

        Glide.with(this).load(show.getBackDrop()).centerCrop().into(posterView);
        titleView.setText(show.getTitle());
        descriptionView.setText("Samenvatting" + ": \n" + show.getOverview());
        genresView.setText("Genres" + ": \n" + show.getGenreToString());
        ratingView.setText("Beoordeling" + ": \n" + show.getRating() + " (" + show.getVoteCount() + ")");
        releaseDateView.setText("Releasedatum" + ": \n" + show.getReleaseDate());
    }

    @Override
    public void onClick(View v) {
            Intent intent = new Intent(android.content.Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBodyText = String.format(getString(R.string.share_message), String.valueOf(show.getTitle()), String.valueOf(show.getRating()),String.valueOf(show.getPosterPath()));
            intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }
}
