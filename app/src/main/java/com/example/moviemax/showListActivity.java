package com.example.moviemax;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class showListActivity extends AppCompatActivity implements Dialog.dialogListener {

    RecyclerView recyclerView;
    TextView listName;
    TextView listCount;

    ArrayList<ShowList> thisList;
    private DatabaseHelper mDatabaseHelper;
    private String listNameData;
    private showListAdapter adapter;
    private Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDatabaseHelper = new DatabaseHelper(this);
        recyclerView = (RecyclerView) findViewById(R.id.showListRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        thisList = new ArrayList<>();
        adapter = new showListAdapter(showListActivity.this, mDatabaseHelper.getShowListUser());
        adapter.setOnEntryClickListener(new showListAdapter.OnEntryClickListener() {
            @Override
            public void onEntryClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        add = findViewById(R.id.addList);

        if (!(mDatabaseHelper.checkIfUserHasList())) {
            add.setVisibility(Button.GONE);
        }




    }

    public void onClickLogin(View v){
        openDialog();

    }

    public void openDialog(){
        Dialog dialog = new Dialog();
        dialog.show(getSupportFragmentManager(), "Dialog");


    }

    @Override
    public void applyText(String name) {
        if (mDatabaseHelper.createShowList(MainActivity.loginId, name)) {
            Toast.makeText(this, "List created!" + MainActivity.loginId, Toast.LENGTH_LONG).show();
        }
        adapter = new showListAdapter(showListActivity.this, mDatabaseHelper.getShowListUser());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void onClickDeleteList(View v){

    }
}


