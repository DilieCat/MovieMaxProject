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

public class  MainActivity extends AppCompatActivity implements View.OnClickListener{
    private RecyclerView recyclerView;

    private ShowAdapter showAdapter;
    private ArrayList<Show> showArrayList;
    private RequestQueue requestQueue;

    //Variables used for the URL builder.
    private final String ROVER_BASE_URL = "https://api.themoviedb.org/3/movie/popular";
    private final static String PARAM_PAGE = "page";
    private final static String LANGUAGE = "language";
    private final static String LANGUAGE_TYPE = "en-US";
    private final static String PARAM_API = "api_key";
    private final static String PARAM_APIKEY = "ee960f573833509472cb7ab57f055c12";

    private int pageNumber = 1;
    private int totalPages = 0;

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
        totalPagesBtn = findViewById(R.id.totalPagesBtn);
        Button startPageBtn = findViewById(R.id.startPageBtn);
        testButton = findViewById(R.id.testButton);
        loginBtn = findViewById(R.id.loginBtn);

        middleBtn.setText(Integer.toString(pageNumber));
        startPageBtn.setText("1");
        leftPageBtn.setOnClickListener(this);
        rightPageBtn.setOnClickListener(this);
        totalPagesBtn.setOnClickListener(this);
        startPageBtn.setOnClickListener(this);
        testButton.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        parseJSON(pageNumber);

        loggedIn = false;
    }

    private void parseJSON(int pageNumber){
        //clear current list if items are in the list
        if (showArrayList.size() > 0) {
            showArrayList.clear();
        }

        //use buildUrl method to build the API url.
        String url = buildUrl(pageNumber);

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
    private String buildUrl(int pageNumber) {
        Uri builtUri = Uri.parse(ROVER_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_API, PARAM_APIKEY)
                .appendQueryParameter(LANGUAGE, LANGUAGE_TYPE)
                .appendQueryParameter(PARAM_PAGE, Integer.toString(pageNumber))
                .build();
        return builtUri.toString();
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
                parseJSON(pageNumber);
                break;

            case R.id.leftPageBtn:
                if (pageNumber > 1) {
                    pageNumber--;
                    parseJSON(pageNumber);
                    break;
                }

            case R.id.totalPagesBtn:
                pageNumber = totalPages;
                parseJSON(pageNumber);
                break;

            case R.id.startPageBtn:
                pageNumber = 1;
                parseJSON(pageNumber);
                break;

            case R.id.testButton:
                Intent singUp = new Intent(this, Signup.class);
                startActivity(singUp);
                break;

            case R.id.loginBtn:
                Intent login = new Intent(this, login.class);
                startActivity(login);

                break;


        }
        middleBtn.setText(Integer.toString(pageNumber));
    }
}
