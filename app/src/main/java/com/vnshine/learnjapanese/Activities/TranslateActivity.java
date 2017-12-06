package com.vnshine.learnjapanese.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.LoginFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vnshine.learnjapanese.Models.TranslateText;
import com.vnshine.learnjapanese.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslateActivity extends AppCompatActivity implements View.OnClickListener {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ImageView imageBack;
    /**
     * Dịch văn bản
     */
    private TextView textName;
    private ImageView imageXoa;
    private ImageView imgSpeak;
    private ImageView imgVoice;
    /**
     * Anh - Việt
     */
    private Button btnAnhviet;
    /**
     * Việt - Anh
     */
    private Button btnVietanh;
    /**
     * i love you
     */
    private TextView textKetqua;
    /**
     * Nhập cụm từ, câu văn hoặc đoạn văn.
     */
    private EditText textCandich;
    //    private SpeakUltil speakUltil;
    private String textToBeTranslated;
    private String languagePair;
    private InputMethodManager imm;
    private TranslateText translateText = new TranslateText();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        setToolbar();
        initView();
//        speakUltil=new SpeakUltil(this);
        Intent intent = getIntent();
        String text = intent.getStringExtra("text");
        if (text != null) {
            languagePair = "en-vi";
            textCandich.setText(text);
            translateText = new TranslateText();
            translateText.setTextToBeTranslate(text);
            translateText.setLanguagePair(languagePair);
            translateText.translateLanguage().enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        if (response.body() != null) {
                            System.out.println(response.body());
                            String resultString = response.body().trim();
                            //Getting the characters between [ and ]
                            resultString = resultString.substring(resultString.indexOf('[') + 1);
                            resultString = resultString.substring(0, resultString.indexOf("]"));
                            //Getting the characters between " and "
                            resultString = resultString.substring(resultString.indexOf("\"") + 1);
                            resultString = resultString.substring(0, resultString.indexOf("\""));
                            textKetqua.setText(resultString);
                            Log.d("Translation Result:", resultString);
                        }
                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {

                }
            });
            imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(
                    textCandich.getWindowToken(), 0);
        }
        textCandich.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textToBeTranslated = textCandich.getText().toString();


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.translate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_translate_activity,menu);
        MenuItem clear = findViewById(R.id.action_clear);
//        clear.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                textCandich.setText("");
//                return true;
//            }
//        });
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {

        imgSpeak = findViewById(R.id.img_speak);
        imgSpeak.setOnClickListener(this);
        imgVoice = findViewById(R.id.img_voice_input);
        imgVoice.setOnClickListener(this);
        btnAnhviet = findViewById(R.id.btn_jap_vie);
        btnAnhviet.setOnClickListener(this);
        btnVietanh = findViewById(R.id.btn_vie_jap);
        btnVietanh.setOnClickListener(this);
        textKetqua = findViewById(R.id.text_ketqua);
        textKetqua.setOnClickListener(this);
        textCandich = findViewById(R.id.text_candich);
        textCandich.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_speak:
//                promptSpeechInput();
                break;
            case R.id.img_voice_input:
//                speak();
                break;
            case R.id.btn_jap_vie:
                dichAnhViet();
                break;
            case R.id.btn_vie_jap:
                dichVietAnh();
                break;
            case R.id.text_ketqua:
                break;
            case R.id.text_candich:
                break;
        }
    }

    private void xoa() {
        textCandich.setText("");
        textKetqua.setText("");
    }

//    private void speak() {
//        speakUltil.speak(textToBeTranslated);
//    }

    private void dichVietAnh() {
        Log.i("Button click:", "Button clicked");
        languagePair = "vi-ja";
        translateText.setTextToBeTranslate(textToBeTranslated);
        translateText.setLanguagePair(languagePair);
        translateText.translateLanguage().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.body() != null) {
                        System.out.println(response.body());
                        String resultString = response.body().trim();
                        //Getting the characters between [ and ]
                        resultString = resultString.substring(resultString.indexOf('[') + 1);
                        resultString = resultString.substring(0, resultString.indexOf("]"));
                        //Getting the characters between " and "
                        resultString = resultString.substring(resultString.indexOf("\"") + 1);
                        resultString = resultString.substring(0, resultString.indexOf("\""));
                        textKetqua.setText(resultString);
                        Log.d("Translation Result:", resultString);
                    }
                } catch (Exception e) {
                    Log.i("Result exception: ", e + "");
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                textCandich.getWindowToken(), 0);
    }

    private void dichAnhViet() {
        languagePair = "en-vi";
        translateText.setTextToBeTranslate(textToBeTranslated);
        translateText.setLanguagePair(languagePair);
        translateText.translateLanguage().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if (response.body() != null) {
                        System.out.println(response.body());
                        String resultString = response.body().trim();
                        //Getting the characters between [ and ]
                        resultString = resultString.substring(resultString.indexOf('[') + 1);
                        resultString = resultString.substring(0, resultString.indexOf("]"));
                        //Getting the characters between " and "
                        resultString = resultString.substring(resultString.indexOf("\"") + 1);
                        resultString = resultString.substring(0, resultString.indexOf("\""));
                        textKetqua.setText(resultString);
                        Log.d("Translation Result:", resultString);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("null");
            }
        });

        imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                textCandich.getWindowToken(), 0);
    }

//    private void promptSpeechInput() {
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
//                getString(R.string.speech_prompt));
//        try {
//            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
//        } catch (ActivityNotFoundException a) {
//            Toast.makeText(getApplicationContext(),
//                    getString(R.string.speech_not_supported),
//                    Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * Receiving speech input
     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQ_CODE_SPEECH_INPUT: {
//                if (resultCode == RESULT_OK && null != data) {
//                    String text="";
//                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    textCandich.setText(result.get(0));
//                    DatabaseHelperAnhViet databaseHelperAnhViet=EnglishWord.getEnglishWordDAO(DichVanBanActivity.this);
//                    EnglishWord englishWord =databaseHelperAnhViet.getCurrentWord(result.get(0));
//                    if(englishWord !=null){
//                        Intent intent = new Intent(this, DetailAnhVietActivity.class);
//                        intent.putExtra("text", result.get(0));
//                        startActivity(intent);
//                    }
//                    else {
//                        languagePair = "en-vi";
//                        translateText.setTextToBeTranslate(textToBeTranslated);
//                        translateText.setLanguagePair(languagePair);
//                        translateText.translateLanguage().enqueue(new Callback<String>() {
//                            @Override
//                            public void onResponse(Call<String> call, Response<String> response) {
//                                try {
//                                    if(response.body()!=null){
//                                        System.out.println(response.body());
//                                        String resultString = response.body().trim();
//                                        //Getting the characters between [ and ]
//                                        resultString = resultString.substring(resultString.indexOf('[')+1);
//                                        resultString = resultString.substring(0,resultString.indexOf("]"));
//                                        //Getting the characters between " and "
//                                        resultString = resultString.substring(resultString.indexOf("\"")+1);
//                                        resultString = resultString.substring(0,resultString.indexOf("\""));
//                                        textKetqua.setText(resultString);
//                                        Log.d("Translation Result:", resultString);
//                                    }
//
//                                }catch (Exception e){
//
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<String> call, Throwable t) {
//
//                            }
//                        });
//                        imm = (InputMethodManager)
//                                getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.hideSoftInputFromWindow(
//                                textCandich.getWindowToken(), 0);
//                    }
//                }
//                break;
//            }
//
//        }
//    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //SpeakUltil.close();
    }
}
