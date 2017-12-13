package com.vnshine.learnjapanese.Activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
    MediaPlayer mp;
    ExpandableListView listView;
    ExpandableListAdapter listViewAdapter;
    ArrayList<Sentence> sentences = new ArrayList<>();
    ArrayList<JapaneseSentence> listJapaneseSentences = new ArrayList<>();
    ArrayList<Meaning> listMeanings = new ArrayList<>();
    Button test;
    boolean lastStatus = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getContent();
        setToolbar();
        setListView();
        test = findViewById(R.id.btn_test);
        if (category_id == 0) {
            test.setBackgroundColor(Color.GRAY);
            test.setEnabled(false);
        } else
            test.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CategoryActivity.this, TestActivity.class);
                    intent.putExtra("category", category);
                    intent.putExtra("category_id", category_id);
                    CategoryActivity.this.startActivity(intent);
                }
            });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_category_activity, menu);
//        final MenuItem mute = findViewById(R.id.action_mute);
//        mute.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                if (lastStatus){
//                    mp.setVolume(0,0);
//                    item.setIcon(R.drawable.ic_volume_off_white_24dp);
//                    lastStatus = false;
//                }else {
//                    mp.setVolume(1,1);
//                    item.setIcon(R.drawable.ic_volume_up_white_24dp);
//                    lastStatus = true;
//                }
//                return true;
//
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (lastStatus) {
            AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            manager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            item.setIcon(R.drawable.ic_volume_off_white_24dp);
            lastStatus = false;
        } else {
            AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            manager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            item.setIcon(R.drawable.ic_volume_up_white_24dp);
            lastStatus = true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setListView() {
        mp = new MediaPlayer();
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
            databaseHelper.getAllFavoriteSentences();
            this.listJapaneseSentences = databaseHelper.getListJapansesSentences();
            this.listMeanings = databaseHelper.getListMeanings();

        } else {
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


}
