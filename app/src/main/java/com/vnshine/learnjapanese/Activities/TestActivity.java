package com.vnshine.learnjapanese.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.vnshine.learnjapanese.DataBase.DatabaseHelper;
import com.vnshine.learnjapanese.Dialog.CompleteDialog;
import com.vnshine.learnjapanese.Dialog.ResultDialog;
import com.vnshine.learnjapanese.Fragment.ChoiceFragment;
import com.vnshine.learnjapanese.Fragment.PronounceFragment;
import com.vnshine.learnjapanese.Fragment.TranslateFragment;
import com.vnshine.learnjapanese.Models.Sentence;
import com.vnshine.learnjapanese.R;
import com.vnshine.lib.ads.VnshineAds;

import java.util.ArrayList;
import java.util.Random;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    public static int countAds = 0;
    private static final String FRAGMENTTAG = "CONTAINER";
    private ImageView btnBack;
    private ImageView btnClearProgress;
    private static Button btnTest;
    private ProgressBar progressBar;
    private int count = 0;
    private int category_id;
    private String category;
    private int status = 0;
    private Random random;
    private ArrayList<Sentence> listSentences;
    private int position;

    public Sentence getSentence() {
        return sentence;
    }

    public int setSentence() {
        random = new Random();
        int a;
        while (true) {
            a = random.nextInt(listSentences.size());
            if (listSentences.get(a).getStatus() == 0) {
                this.sentence = listSentences.get(a);
                Log.e("select sentence error: ", this.sentence.getEnglish() + "");
                break;
            }
        }
        return a;
    }

    private Sentence sentence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        readDB();
        initComponent();
        setFragment();
        countAds++;
        if (countAds>2){
            VnshineAds.getInstance().showFullAds();
            countAds =0;
        }

    }

    private void initComponent() {
        btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(this);
        btnClearProgress = findViewById(R.id.btn_clear_progess);
        btnClearProgress.setOnClickListener(this);
        btnTest = findViewById(R.id.btn_next);
        btnTest.setOnClickListener(this);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setProgress(0);
        progressBar.setMax(listSentences.size());
    }

    private void setFragment() {
        if (count == listSentences.size()){
            CompleteDialog completeDialog = new CompleteDialog(this);
            completeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    TestActivity.this.finish();
                }
            });
            completeDialog.show();
            return;
        }
        Random random = new Random();
        position = setSentence();
        int a = random.nextInt(3);
        switch (a) {
            case 0: {
                ChoiceFragment choiceFragment = new ChoiceFragment();
                choiceFragment.setSentence(getSentence());
                choiceFragment.setRandomSentence(randomThreeSentence());
                openFragment(choiceFragment);
                break;
            }
            case 1: {
                TranslateFragment translateFragment = new TranslateFragment();
                translateFragment.setSentence(getSentence());
                openFragment(translateFragment);
                break;
            }
            case 2: {
                setEnableCheck();
                PronounceFragment pronounceFragment = new PronounceFragment();
                pronounceFragment.setSentence(getSentence());
                openFragment(pronounceFragment);
                break;
            }
        }
    }

    private void openFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment, fragment, FRAGMENTTAG)
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
                progressBar.setProgress(0);
                listSentences.clear();
                readDB();
                setFragment();
                break;
            }
            case R.id.btn_next: {
                Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(FRAGMENTTAG);
                if (currentFragment instanceof PronounceFragment) {
                    boolean result = ((PronounceFragment) currentFragment).getResult();
                    if (result){
                        count++;
                        progressBar.setProgress(count);
                        listSentences.get(position).setStatus(1);
                    }
                    setDisableCheck();
                    setFragment();
                } else if (currentFragment instanceof TranslateFragment) {
                    boolean result = ((TranslateFragment) currentFragment).getResult();
                    if (result){
                        count++;
                        progressBar.setProgress(count);
                        listSentences.get(position).setStatus(1);
                    }
                    ResultDialog resultDialog = new ResultDialog(this);
                    resultDialog.getContent(result, getSentence());
                    resultDialog.show();
                    resultDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            setFragment();
                            setDisableCheck();
                        }
                    });
//                    }
                } else if (currentFragment instanceof ChoiceFragment) {
                    boolean result = ((ChoiceFragment) currentFragment).getResult();
                    if (result){
                        count++;
                        progressBar.setProgress(count);
                        listSentences.get(position).setStatus(1);
                    }
                    ResultDialog resultDialog = new ResultDialog(this);
                    resultDialog.getContent(result, getSentence());
                    resultDialog.show();
                    resultDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            setFragment();
                            setDisableCheck();
                        }
                    });
                }
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


    private int getSumOfTestedSentence() {
        int count = 0;
        for (int i = 0; i < listSentences.size(); i++) {
            if (listSentences.get(i).getStatus() != 0) count++;
        }
        return count;
    }

    public static void setEnableCheck(){
        btnTest.setEnabled(true);
        btnTest.setTextColor(Color.GREEN);
        btnTest.setBackgroundResource(R.drawable.button_border_green);
    }

    public static void setDisableCheck(){
        btnTest.setEnabled(false);
        btnTest.setTextColor(Color.GRAY);
        btnTest.setBackgroundResource(R.drawable.button_border_gray);
    }

    private ArrayList<Sentence> randomThreeSentence() {
        Random random = new Random();
        ArrayList<Sentence> list = new ArrayList<>();
        int a, b, c;
        while (true) {
            a = random.nextInt(listSentences.size());
            if (a != getSentence().getId()) break;
        }
        while (true) {
            b = random.nextInt(listSentences.size());
            if (b != getSentence().getId() && b != a) break;
        }
        while (true) {
            c = random.nextInt(listSentences.size());
            if (c != getSentence().getId() && c != a && c != b) break;
        }
        list.add(listSentences.get(a));
        list.add(listSentences.get(b));
        list.add(listSentences.get(c));
        return list;
    }


}
