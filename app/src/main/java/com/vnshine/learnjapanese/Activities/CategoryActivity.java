package com.vnshine.learnjapanese.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.vnshine.learnjapanese.Adapters.ExpandableListAdapter;
import com.vnshine.learnjapanese.DataBase.DatabaseHelper;
import com.vnshine.learnjapanese.Models.JapaneseSentence;
import com.vnshine.learnjapanese.Models.Meaning;
import com.vnshine.learnjapanese.Models.Sentence;
import com.vnshine.learnjapanese.R;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    private int lastExpandedPosition = -1;
    Toolbar toolbar;
    String category;
    int category_id;
    ExpandableListView listView;
    ExpandableListAdapter listViewAdapter;
        ArrayList<Sentence> sentences = new ArrayList<>();
    ArrayList<JapaneseSentence> listJapaneseSentences = new ArrayList<>();
    ArrayList<Meaning> listMeanings = new ArrayList<>();
    Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getContent();
        setToolbar();
        setListView();
    }

    private void setListView() {
        readDB();
        listView = findViewById(R.id.sentence);
        listViewAdapter = new ExpandableListAdapter(this, listJapaneseSentences, listMeanings);
        listView.setAdapter(listViewAdapter);
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    listView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });
    }

    private void readDB() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        if (category_id == 0) {
//            sentences = databaseHelper.getAllFavoriteSentences();
            databaseHelper.getAllFavoriteSentences();
            this.listJapaneseSentences = databaseHelper.getListJapansesSentences();
            this.listMeanings = databaseHelper.getListMeanings();
//            sentences = databaseHelper.getAllFavoriteSentences();

        } else {
//            sentences = databaseHelper.getAllSentences(String.valueOf(category_id));
            databaseHelper.getAllSentences(String.valueOf(category_id));
            this.listJapaneseSentences = databaseHelper.getListJapansesSentences();
            this.listMeanings = databaseHelper.getListMeanings();
        }
        databaseHelper.close();
    }

    void getContent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        this.category = bundle.getString("category");
        this.category_id = bundle.getInt("category_id");
    }

    void setToolbar() {
        toolbar = findViewById(R.id.category_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Log.d("category", category);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
