package com.example.moviemax;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.Locale;

public class  MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {
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

    private int searchType = apiLinks.SEARCH_TYPE.length - 1;
    private int filter;
    private int language;
    private int pageNumber;
    private int region;
    private int genre_id;
    private String searchText;
    private int totalPages;

    public static String loginEmail;
    public static String loginPassword;

    //false is not logged in, true is logged in
    public static boolean loggedIn;

    //private int numberOfRequestsToMake = 0;
    //private boolean hasRequestFailed = false;

    private Button middleBtn;
    private Button totalPagesBtn;
    private Button loginBtn;
    private Button testButton;

    private DrawerLayout drawer;

    private Locale locale;

    //database
    private DatabaseHelper mDatabaseHelper;
    private Button addBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchType = apiLinks.SEARCH_TYPE.length - 1;
        filter = 0;
        language = 1;
        pageNumber = 1;
        region = 1;
        genre_id = 0;
        searchText = "";
        totalPages = 0;

        loadLocale();

        //database
        mDatabaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recycler_view);

        showArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Button leftPageBtn = findViewById(R.id.leftPageBtn);
        Button rightPageBtn = findViewById(R.id.rightPageBtn);
        middleBtn = findViewById(R.id.middleBtn);
        totalPagesBtn = findViewById(R.id.totalPagesBtn);
        Button startPageBtn = findViewById(R.id.startPageBtn);

        middleBtn.setText(Integer.toString(pageNumber));
        startPageBtn.setText("1");
        leftPageBtn.setOnClickListener(this);
        rightPageBtn.setOnClickListener(this);
        totalPagesBtn.setOnClickListener(this);
        startPageBtn.setOnClickListener(this);

        parseJSON();
        loggedIn = false;
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
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
                        searchText = newText;
                        searchType = 0;
                        filter = apiLinks.FILTER.length - 1;
                        showArrayList = new ArrayList<>();
                        requestQueue = Volley.newRequestQueue(context);
                        parseJSON();
                        Toast.makeText(context, "searched for " + newText, Toast.LENGTH_SHORT).show();
                        System.out.println(url);

                        searchText = "";
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Intent intent = new Intent();
        switch (menuItem.getItemId()){
            case R.id.login:
                intent = new Intent(this, login.class);
                this.startActivity(intent);
                break;
            case R.id.register:
                intent = new Intent(this, Signup.class);
                this.startActivity(intent);
                break;
            case R.id.language_switch:
                showChangeLanguageDialog();
                break;
        }
        return true;
    }

    private void showChangeLanguageDialog(){
        final String[] languages = {"English", "Dutch"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
        mBuilder.setTitle(R.string.language);
        mBuilder.setSingleChoiceItems(languages, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        setLocale("en");
                        region = 0;
                        language = 0;
                        break;
                    case 1:
                        setLocale("nl");
                        region = 1;
                        language = 1;
                        break;
                }
                recreate();
                dialog.dismiss();
                showArrayList = new ArrayList<>();
                requestQueue = Volley.newRequestQueue(context);
                parseJSON();
                Log.d("poep", url);
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String lang) {
        locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", lang);
        editor.apply();
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String languagePref = prefs.getString("My_Lang", "");
        setLocale(languagePref);
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
            case R.id.upcoming:
                filter = 2;
                break;
            case R.id.now_playing:
                filter = 3;
                break;
            case R.id.genres:
                break;
            case R.id.action:
                genre_id = 28;
                break;
            case R.id.adventure:
                genre_id = 12;
                break;
            case R.id.animation:
                genre_id = 16;
                break;
            case R.id.comedy:
                genre_id = 35;
                break;
            case R.id.crime:
                genre_id = 80;
                break;
            case R.id.documentary:
                genre_id = 99;
                break;
            case R.id.drama:
                genre_id = 18;
                break;
            case R.id.family:
                genre_id = 10751;
                break;
            case R.id.fantasy:
                genre_id = 14;
                break;
            case R.id.history:
                genre_id = 36;
                break;
            case R.id.horror:
                genre_id = 27;
                break;
            case R.id.music:
                genre_id = 10402;
                break;
            case R.id.mystery:
                genre_id = 9648;
                break;
            case R.id.romance:
                genre_id = 10749;
                break;
            case R.id.science_fiction:
                genre_id = 878;
                break;
            case R.id.tv_movie:
                genre_id = 10770;
                break;
            case R.id.thriller:
                genre_id = 53;
                break;
            case R.id.war:
                genre_id = 10752;
                break;
            case R.id.western:
                genre_id = 37;
                break;
        }
        if(genre_id != 0){
            filter = apiLinks.FILTER.length - 1;
            searchType = 1;
        }
        showArrayList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);
        parseJSON();
        System.out.println(url);
        if(genre_id == 0){
            Toast.makeText(this, "filtered by " + apiLinks.FILTER[filter], Toast.LENGTH_SHORT).show();
            genre_id = 0;
        }
        else{
            Toast.makeText(this, "filtered by genre " + Genres.getList().get(genre_id), Toast.LENGTH_SHORT).show();
        }

        return true;

        // return super.onOptionsItemSelected(item);
    }

    private void parseJSON() {
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
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject hit = jsonArray.getJSONObject(i);

                                //get all item data
                                int id = hit.getInt("id");
                                String title = hit.getString("title");
                                String language = hit.getString("original_language");
                                String showImage = "https://image.tmdb.org/t/p/w500" + hit.getString("poster_path");
                                String overview = hit.getString("overview");
                                String releaseDate = hit.getString("release_date");
                                String rating = hit.getString("vote_average");
                                String voteCount = hit.getString("vote_count");
                                String backDrop = "https://image.tmdb.org/t/p/w500" + hit.getString("backdrop_path");

                                //Put all genres in a list.
                                ArrayList<String> genres = new ArrayList<String>();
                                JSONArray arrGenres = hit.getJSONArray("genre_ids");
                                for (int j = 0; j < arrGenres.length(); j++) {
                                    genres.add(Genres.getList().get(arrGenres.get(j)));
                                }

                                //Make new show items.
                                showArrayList.add(new Show(id, title, genres, language, showImage, overview, releaseDate, rating, voteCount, backDrop));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setupRecyclerView(showArrayList);
                        showAdapter.notifyDataSetChanged();
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

    private void setupRecyclerView(ArrayList<Show> showArrayList) {
         showAdapter = new ShowAdapter(this, showArrayList);
         recyclerView.setLayoutManager(new LinearLayoutManager(this));

         recyclerView.setHasFixedSize(true);
         recyclerView.setAdapter(showAdapter);
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

                        if(searchType == 1){
                            builder.appendQueryParameter("sort_by", "popularity.desc");
                        }
                        if(genre_id != 0){
                            builder.appendQueryParameter("with_genres", Integer.toString(genre_id));
                        }
                        if(pageNumber != 0){
                            builder.appendQueryParameter("page", Integer.toString(pageNumber));
                        }
                        if(!searchText.equals("")){
                            builder.appendQueryParameter("query", searchText);
                        }

                        builder.appendQueryParameter("region", apiLinks.REGION[region]);

                        url = builder.build().toString();
        Log.d("Tag", url);
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

            case R.id.loginBtn:
                Intent login = new Intent(this, login.class);
                startActivity(login);

                break;


        }
        parseJSON();
        middleBtn.setText(Integer.toString(pageNumber));
    }
}
