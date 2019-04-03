package com.example.moviemax;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener, ShowAdapter.OnItemClickListener {
    private RecyclerView recyclerView;
    private ShowAdapter showAdapter;
    private ArrayList<Show> showArrayList;
    private RequestQueue requestQueue;

    //Variables used for the onItemClick method.
    public static final String EXTRA_POSTERPATH = "posterPath";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_DESCRIPTION = "description";
    public static final String EXTRA_TRAILER = "trailerlink";
    public static final String EXTRA_GENRE = "genre";

    //Variables used for the URL builder.
    private final String ROVER_BASE_URL = "https://api.themoviedb.org/3/movie/popular";
    private final static String PARAM_PAGE = "page";
    private final static String LANGUAGE = "language";
    private final static String LANGUAGE_TYPE = "en-US";
    private final static String PARAM_API = "api_key";
    private final static String PARAM_APIKEY = "ee960f573833509472cb7ab57f055c12";

    private int pageNumber = 1;

    private Button middleBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        Button leftPageBtn = findViewById(R.id.leftPageBtn);
        Button rightPageBtn = findViewById(R.id.rightPageBtn);
        middleBtn = findViewById(R.id.middleBtn);

        middleBtn.setText(Integer.toString(pageNumber));
        leftPageBtn.setOnClickListener(this);
        rightPageBtn.setOnClickListener(this);

        parseJSON(pageNumber);
        //show
    }

    private void parseJSON(int pageNumber){
        if (showArrayList.size() > 0) {
            showArrayList.clear();
        }


        String url = buildUrl(pageNumber);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");


                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject hit = jsonArray.getJSONObject(i);

                                int id = hit.getInt("id");
                                String title = hit.getString("title");
                                String language = hit.getString("original_language");
                                String showImage = "https://image.tmdb.org/t/p/w500" + hit.getString("poster_path");
                                String overview = hit.getString("overview");


                                ArrayList<String> genres = new ArrayList<String>();
                                JSONArray arrGenres = hit.getJSONArray("genre_ids");
                                for( int j = 0; j < arrGenres.length(); j++) {
                                    genres.add(Genres.getList().get(arrGenres.get(j)));
                                }



                                showArrayList.add(new Show(id, title, genres, language, showImage, overview));
                            }

                            showAdapter = new ShowAdapter(MainActivity.this, showArrayList);
                            recyclerView.setAdapter(showAdapter);
                            showAdapter.notifyDataSetChanged();
                            showAdapter.setOnItemClickListener(MainActivity.this);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjectRequest);

    }

    //URL BUILDER
    private String buildUrl(int pageNumber) {
        Uri builtUri = Uri.parse(ROVER_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API, PARAM_APIKEY)
                .appendQueryParameter(LANGUAGE, LANGUAGE_TYPE)
                .appendQueryParameter(PARAM_PAGE, Integer.toString(pageNumber))
                .build();
        return builtUri.toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightPageBtn:
                pageNumber++;
                parseJSON(pageNumber);
                break;

            case R.id.leftPageBtn:
                if (pageNumber > 1) {
                    pageNumber--;
                    parseJSON(pageNumber);
                    break;
                }
        }
        middleBtn.setText(Integer.toString(pageNumber));
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);
        Show clickedShow = showArrayList.get(position);

        detailIntent.putExtra(EXTRA_POSTERPATH, clickedShow.getPosterpath());
        detailIntent.putExtra(EXTRA_TITLE, clickedShow.getTitle());
        detailIntent.putExtra(EXTRA_DESCRIPTION, clickedShow.getOverview());
        detailIntent.putExtra(EXTRA_TRAILER, clickedShow.getTrailerLink());
        detailIntent.putExtra(EXTRA_GENRE, clickedShow.getGenreToString());

        startActivity(detailIntent );
    }
}
