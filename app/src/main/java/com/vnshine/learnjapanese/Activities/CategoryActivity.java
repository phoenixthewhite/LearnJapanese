package com.vnshine.learnjapanese.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.vnshine.learnjapanese.Adapters.ListViewAdapter;
import com.vnshine.learnjapanese.Models.ListViewItem;
import com.vnshine.learnjapanese.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    Toolbar toolbar;
    String name;
    ListView listView;
    ListViewAdapter listViewAdapter;
    ArrayList<ListViewItem> items = new ArrayList<>();
    String [] names = new String[]{
            "AAAAAAAA", "AAAAAA", "AAAAAA", "AAAAAA", "AAAAAA", "AAAAAA", "AAAAAA", "AAAAAA"
            , "AAAAAA", "AAAAAA", "AAAAAA", "AAAAAA", "AAAAAA"
            , "AAAAAA", "AAAAAA", "AAAAAA", "AAAAAA", "AAAAAA"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        setToolbar();
        setListView();

    }

    private void setListView() {

        for (int i = 0; i < names.length; i++) {
            ListViewItem item = new ListViewItem();
            item.setSentence(names[i]);
            items.add(item);
        }
        listView = findViewById(R.id.sentence);
        listViewAdapter = new ListViewAdapter(this,R.layout.item_sentence,items);
        listView.setAdapter(listViewAdapter);
    }

    void getContent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        this.name = bundle.getString("name");
    }

    void setToolbar() {
        toolbar = findViewById(R.id.category_toolbar);
        toolbar.setTitle("Category");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getContent();
        toolbar.setTitle(name);
    }

    public void runButton(View view) {
        Toast.makeText(this, "Button Was Clicked", Toast.LENGTH_SHORT).show();
    }
}
