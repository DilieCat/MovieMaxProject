package com.example.moviemax;

import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ShowableListMenu;
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

public class  MainActivity extends AppCompatActivity implements View.OnClickListener, ShowAdapter.OnItemClickListener{
    private RecyclerView recyclerView;

    private ShowAdapter showAdapter;
    private ArrayList<Show> showArrayList;
    private RequestQueue requestQueue;
    private String url = "";

    private Context context = this;
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

    private int searchType = 2;
    private int filter = 0;
    private int language = 0;
    private int pageNumber = 1;
    private int region = 1;
    private int genre_id = 0;
    private String searchtext = "";
    private int totalPages = 0;

    public static String loginEmail;
    public static String loginPassword;
    public static String loginId;

    //false is not logged in, true is logged in
    public static boolean loggedIn;

    //private int numberOfRequestsToMake = 0;
    //private boolean hasRequestFailed = false;

    private Button middleBtn;
    private Button totalPagesBtn;
    private Button loginBtn;
    private Button testButton;

    //database
    private DatabaseHelper mDatabaseHelper;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //database
        mDatabaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        showArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        Button leftPageBtn = findViewById(R.id.leftPageBtn);
        Button rightPageBtn = findViewById(R.id.rightPageBtn);
        middleBtn = findViewById(R.id.middleBtn);
        totalPagesBtn = findViewById(R.id.totalPagesBtn);
        Button startPageBtn = findViewById(R.id.startPageBtn);
        testButton = findViewById(R.id.testButton);
        loginBtn = findViewById(R.id.loginBtn);
        Button listBtn = findViewById(R.id.listBtn);

        middleBtn.setText(Integer.toString(pageNumber));
        startPageBtn.setText("1");
        leftPageBtn.setOnClickListener(this);
        rightPageBtn.setOnClickListener(this);
        totalPagesBtn.setOnClickListener(this);
        startPageBtn.setOnClickListener(this);
        testButton.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        listBtn.setOnClickListener(this);

        parseJSON();
        loggedIn = false;
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
        //use buildUrl method to build the API url.

        //Start a json object request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //fetch all the array results.
                            JSONArray jsonArray = response.getJSONArray("results");

                            //Get the total pages, then put it in the button text.
                            totalPages = response.getInt("total_pages");
                            totalPagesBtn.setText(Integer.toString(totalPages));

                            //Start a for loop for the items in the array.
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject hit = jsonArray.getJSONObject(i);

                                //get all item data
                                int id = hit.getInt("id");
                                String title = hit.getString("title");
                                String language = hit.getString("original_language");
                                String showImage = "https://image.tmdb.org/t/p/w500" + hit.getString("poster_path");
                                String overview = hit.getString("overview");

                                //Put all genres in a list.
                                ArrayList<String> genres = new ArrayList<String>();
                                JSONArray arrGenres = hit.getJSONArray("genre_ids");
                                for( int j = 0; j < arrGenres.length(); j++) {
                                    genres.add(Genres.getList().get(arrGenres.get(j)));
                                }

                                //Make new show items.
                                showArrayList.add(new Show(id, title, genres, language, showImage, overview));
                            }

                            //Connect adapters to data
                            showAdapter = new ShowAdapter(MainActivity.this, showArrayList);
                            recyclerView.setAdapter(showAdapter);

                            //This makes sure the adapters knows there is going to be a list change.
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

        //Add a request to the queue.
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

    //Method that activates when activity gets restarted
    @Override
    protected void onRestart() {
        super.onRestart();
        if (loggedIn) {
            loginBtn.setVisibility(View.GONE);
            testButton.setVisibility(View.GONE);
            Toast.makeText(this, "Welcome " + loginEmail, Toast.LENGTH_LONG).show();
        }

    }

    //All click cases
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

            case R.id.totalPagesBtn:
                pageNumber = totalPages;
                parseJSON();
                break;

            case R.id.startPageBtn:
                pageNumber = 1;
                parseJSON();
                break;

            case R.id.testButton:
                Intent singUp = new Intent(this, Signup.class);
                startActivity(singUp);
                break;

            case R.id.loginBtn:
                Intent login = new Intent(this, login.class);
                startActivity(login);
                break;

            case R.id.listBtn:
                Intent showList = new Intent(this, showListActivity.class);
                startActivity(showList);
                break;

        }
        parseJSON();
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
