package com.example.moviemax;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import static com.example.moviemax.MainActivity.EXTRA_DESCRIPTION;
import static com.example.moviemax.MainActivity.EXTRA_GENRE;
import static com.example.moviemax.MainActivity.EXTRA_POSTERPATH;
import static com.example.moviemax.MainActivity.EXTRA_TITLE;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        String posterPath = intent.getStringExtra(EXTRA_POSTERPATH);
        String title = intent.getStringExtra(EXTRA_TITLE);
        String overview = intent.getStringExtra(EXTRA_DESCRIPTION);
        String genre = intent.getStringExtra(EXTRA_GENRE);

        ImageView imageView = findViewById(R.id.image_view_detail);
        TextView titleView = findViewById(R.id.text_view_detail_title);
        TextView descriptionView = findViewById(R.id.text_view_detail_description);
        TextView genreView = findViewById(R.id.text_view_detail_genre);



        Glide.with(this).load(posterPath).centerCrop().into(imageView);
        titleView.setText(title);
        descriptionView.setText("Description: \n" + overview);
        genreView.setText("Genres: \n" + genre);




    }
}
