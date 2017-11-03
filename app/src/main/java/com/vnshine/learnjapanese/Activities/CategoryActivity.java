package com.vnshine.learnjapanese.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.vnshine.learnjapanese.Adapters.ListViewAdapter;
import com.vnshine.learnjapanese.DataBase.DatabaseHelper;
import com.vnshine.learnjapanese.Models.Sentence;
import com.vnshine.learnjapanese.R;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    String category;
    int category_id;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<Sentence> sentences = new ArrayList<>();
    Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getContent();
        setToolbar();
        setListView();
    }

    private void setListView() {
        readDB();
        listView = findViewById(R.id.sentence);
        listViewAdapter = new ListViewAdapter(this,R.layout.item_sentence, sentences);
        listView.setAdapter(listViewAdapter);

    }

    private void readDB() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        if (category_id == 0){
            sentences = databaseHelper.getAllFavoriteSentences();
        }else{
            sentences = databaseHelper.getAllSentences(String.valueOf(category_id));
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
        Log.d("category",category);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
