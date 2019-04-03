package com.example.moviemax;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class  MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ShowAdapter showAdapter;
    private ArrayList<Show> showArrayList;
    private RequestQueue requestQueue;
    private String url = "";

    private Context context = this;

    private int searchType = 2;
    private int filter = 0;
    private int language = 0;
    private int pageNumber = 1;
    private int region = 1;
    private int genre_id = 0;
    private String searchtext = "";

    //Variables used for the URL builder.

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

        parseJSON();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String newText) {
                        searchtext = newText;
                        searchType = 0;
                        filter = 5;
                        showArrayList = new ArrayList<>();
                        requestQueue = Volley.newRequestQueue(context);
                        parseJSON();
                        Toast.makeText(context, "searched for " + newText, Toast.LENGTH_SHORT).show();
                        System.out.println(url);

                        searchtext = "";
                        searchType = 2;
                        filter = 0;

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                }
        );

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popularity:
                filter = 0;
                break;
            case R.id.rating:
                filter = 1;
                break;
            case R.id.latest:
                filter = 2;
                break;
            case R.id.upcoming:
                filter = 3;
                break;
            case R.id.now_playing:
                filter = 4;
                break;
            case R.id.genres:
                break;

        }
        showArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJSON();
        Toast.makeText(this, "filtered by " + apiLinks.FILTER[filter], Toast.LENGTH_SHORT).show();

        return true;

        // return super.onOptionsItemSelected(item);
    }

    private void parseJSON(){
        if (showArrayList.size() > 0) {
            showArrayList.clear();
        }


        String url = buildUrl();

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
    public String buildUrl() {


        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(apiLinks.MDB_BASE_URL)
                .appendPath("3");

        if(searchType != (apiLinks.SEARCH_TYPE.length - 1)){
           builder.appendPath(apiLinks.SEARCH_TYPE[searchType]);
        }

        builder.appendPath("movie");
                if(filter != (apiLinks.FILTER.length - 1)){
                    builder.appendPath(apiLinks.FILTER[filter]);
                }

                builder.appendQueryParameter("api_key", apiLinks.APIKEY)
                        .appendQueryParameter("language", apiLinks.LANGUAGE_TYPE[language]);

                        if(genre_id != 0){
                            builder.appendQueryParameter("with_genres", Integer.toString(genre_id));
                        }
                        if(pageNumber != 0){
                            builder.appendQueryParameter("page", Integer.toString(pageNumber));
                        }
                        if(!searchtext.equals("")){
                            builder.appendQueryParameter("query", searchtext);
                        }
                        if(region != (apiLinks.REGION.length - 1)){
                            builder.appendQueryParameter("region", apiLinks.REGION[region]);
                        }

                        url = builder.build().toString();
        System.out.println(url);
        return url;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightPageBtn:
                pageNumber++;
                break;

            case R.id.leftPageBtn:
                if (pageNumber > 1) {
                    pageNumber--;
                    break;
                }
        }
        parseJSON();
        middleBtn.setText(Integer.toString(pageNumber));
    }

}
