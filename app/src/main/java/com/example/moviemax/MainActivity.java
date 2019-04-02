package com.example.moviemax;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView recyclerView;
    private ShowAdapter showAdapter;
    private ArrayList<Show> showArrayList;
    private RequestQueue requestQueue;

    //Variables used for the URL builder.

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

        parseJSON(apiLinks.SEARCH_TYPE[apiLinks.SEARCH_TYPE.length - 1],
                apiLinks.FILTER[1],
                apiLinks.LANGUAGE_TYPE[0],
                pageNumber,
                apiLinks.REGIO[0]);
        //show
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rating:
                parseJSON(apiLinks.SEARCH_TYPE[apiLinks.SEARCH_TYPE.length - 1],
                        apiLinks.FILTER[1],
                        apiLinks.LANGUAGE_TYPE[0],
                        pageNumber,
                        apiLinks.REGIO[0]);
                break;
            case R.id.popularity:
                parseJSON(apiLinks.SEARCH_TYPE[apiLinks.SEARCH_TYPE.length - 1],
                        apiLinks.FILTER[0],
                        apiLinks.LANGUAGE_TYPE[0],
                        pageNumber,
                        apiLinks.REGIO[0]);
                break;
            case R.id.genre:
                parseJSON(apiLinks.SEARCH_TYPE[apiLinks.SEARCH_TYPE.length - 1],
                        apiLinks.FILTER[2],
                        apiLinks.LANGUAGE_TYPE[0],
                        pageNumber,
                        apiLinks.REGIO[0]);
                System.out.println();
                break;
            case R.id.latest:
                parseJSON(apiLinks.SEARCH_TYPE[apiLinks.SEARCH_TYPE.length - 1],
                        apiLinks.FILTER[2],
                        apiLinks.LANGUAGE_TYPE[0],
                        pageNumber,
                        apiLinks.REGIO[0]);
                break;
            case R.id.upcoming:
                parseJSON(apiLinks.SEARCH_TYPE[apiLinks.SEARCH_TYPE.length - 1],
                        apiLinks.FILTER[3],
                        apiLinks.LANGUAGE_TYPE[0],
                        pageNumber,
                        apiLinks.REGIO[0]);
                break;
            case R.id.now_playing:
                parseJSON(apiLinks.SEARCH_TYPE[apiLinks.SEARCH_TYPE.length - 1],
                        apiLinks.FILTER[4],
                        apiLinks.LANGUAGE_TYPE[0],
                        pageNumber,
                        apiLinks.REGIO[0]);
        }
        showArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJSON(apiLinks.SEARCH_TYPE[apiLinks.SEARCH_TYPE.length - 1],
                apiLinks.FILTER[apiLinks.FILTER.length -1],
                apiLinks.LANGUAGE_TYPE[0],
                pageNumber,
                apiLinks.REGIO[0]);

        return true;

        // return super.onOptionsItemSelected(item);
    }

    private void parseJSON(String searchType, String filter, String language, int pageNumber, String regio){
        if (showArrayList.size() > 0) {
            showArrayList.clear();
        }


        String url = buildUrl(searchType, filter, language, pageNumber, regio);

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
    public String buildUrl(String searchType, String filter, String language, int pageNumber, String regio) {

//        if(!searchType.equals("") && pageNumber != 0 && !regio.equals("")) {
//            Uri.Builder builder = new Uri.Builder();
//            builder.scheme("https");
//            builtUri.buildUpon().appendQueryParameter();
//        }

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority(apiLinks.MDB_BASE_URL)
                .appendPath("3");

        if(!searchType.equals("")){
           builder.appendPath(searchType);
        }

        builder.appendPath("movie");
                if(!filter.equals("")){
                    builder.appendPath(filter);
                }

                builder.appendQueryParameter("api_key", apiLinks.APIKEY)
                        .appendQueryParameter("language", language);
                        if(pageNumber != 0){
                            builder.appendQueryParameter("page", Integer.toString(pageNumber));
                        }
                        if(!regio.equals("")){
                            builder.appendQueryParameter("region", regio);
                        }

                        String url = builder.build().toString();
        System.out.println(url);
        return url;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rightPageBtn:
                pageNumber++;
                parseJSON(apiLinks.SEARCH_TYPE[apiLinks.SEARCH_TYPE.length - 1],
                        apiLinks.FILTER[apiLinks.FILTER.length -1],
                        apiLinks.LANGUAGE_TYPE[0],
                        pageNumber,
                        apiLinks.REGIO[0]);
                break;

            case R.id.leftPageBtn:
                if (pageNumber > 1) {
                    pageNumber--;
                    parseJSON(apiLinks.SEARCH_TYPE[apiLinks.SEARCH_TYPE.length - 1],
                            apiLinks.FILTER[apiLinks.FILTER.length -1],
                            apiLinks.LANGUAGE_TYPE[0],
                            pageNumber,
                            apiLinks.REGIO[0]);
                    break;
                }
        }
        middleBtn.setText(Integer.toString(pageNumber));
    }

}
