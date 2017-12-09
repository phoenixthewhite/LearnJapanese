package com.vnshine.learnjapanese.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.vnshine.learnjapanese.DataBase.DatabaseHelper;
import com.vnshine.learnjapanese.Fragment.ChoiceFragment;
import com.vnshine.learnjapanese.Fragment.PronounceFragment;
import com.vnshine.learnjapanese.Fragment.TranslateFragment;
import com.vnshine.learnjapanese.Models.Sentence;
import com.vnshine.learnjapanese.R;

import java.util.ArrayList;
import java.util.Random;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView btnBack;
    private ImageView btnClearProgress;
    private Button btnNext;
    private ProgressBar progressBar;
    private String category;
    private int category_id;
    private int status = 0;
    private Random random;
    private ArrayList<Sentence> listSentences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        readDB();
        initComponent();
        setFragment();
    }

    private void initComponent() {
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnClearProgress = findViewById(R.id.btn_clear_progess);
        btnClearProgress.setOnClickListener(this);
        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(this);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setProgress(0);
        progressBar.setMax(listSentences.size());
    }

    private void setFragment() {
        Random random = new Random();
        int a = random.nextInt(2);
//        switch (a) {
//            case 0: {
//                openFragment(new ChoiceFragment());
//                break;
//            }
//            case 1: {
                TranslateFragment translateFragment = new TranslateFragment();
                translateFragment.setSentence(selectSentence());
                openFragment(translateFragment);
//                break;
//            }
//            case 2: {
//                PronounceFragment pronounceFragment = new PronounceFragment();
//                pronounceFragment.setSentence(selectSentence());
//                openFragment(pronounceFragment);
//                break;
//            }
//        }
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment, fragment)
                .commit();
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back: {
                finish();
                break;
            }
            case R.id.btn_clear_progess: {

                break;
            }
            case R.id.btn_next: {
                setFragment();
                progressBar.setProgress(status++);
            }
        }
    }

    private void getContent() {
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        this.category = bundle.getString("category");
        this.category_id = bundle.getInt("category_id");
    }

    private void readDB() {
        getContent();
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        listSentences = databaseHelper.getAllSentences(String.valueOf(category_id), true);
        databaseHelper.close();
    }


    private int getSumOfTestedSentence(){
        int count = 0;
        for (int i = 0; i < listSentences.size(); i++) {
            if (listSentences.get(i).getStatus() != 0) count++;
        }
        return count;
    }

    private Sentence selectSentence() {
        random = new Random();
        int a = random.nextInt(listSentences.size());
        if (listSentences.get(a).getStatus() == 0) {
            listSentences.get(a).setStatus(1);
            return listSentences.get(a);
        } else return selectSentence();
    }
}
