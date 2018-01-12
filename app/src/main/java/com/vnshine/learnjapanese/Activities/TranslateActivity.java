package com.vnshine.learnjapanese.Activities;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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
import android.widget.Toast;

import com.vnshine.learnjapanese.Models.TranslateText;
import com.vnshine.learnjapanese.R;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TranslateActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {

    private final int REQ_CODE_SPEECH_INPUT = 100;
    private ImageView imageBack;
    private TextToSpeech tts = null;
    /**
     * Dịch văn bản
     */
    private ImageView imgSpeak1;
    private ImageView imgSpeak2;
    private ImageView imgVoice;
    /**
     * Anh - Việt
     */
    private Button btnJapIn;
    /**
     * Việt - Anh
     */
    private Button btnJapOut;
    /**
     * i love you
     */
    private TextView textOutput;
    /**
     * Nhập cụm từ, câu văn hoặc đoạn văn.
     */
    private EditText textInput;
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
            textInput.setText(text);
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
                            textOutput.setText(resultString);
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
                    textInput.getWindowToken(), 0);
        }
        textInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textToBeTranslated = textInput.getText().toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        tts = new TextToSpeech(this, this);
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
        getMenuInflater().inflate(R.menu.menu_translate_activity, menu);
        MenuItem clear = findViewById(R.id.action_clear);
        return super.onCreateOptionsMenu(menu);
    }

    private void initView() {

        imgSpeak1 = findViewById(R.id.img_voice_output1);
        imgSpeak1.setOnClickListener(this);
        imgVoice = findViewById(R.id.img_voice_input);
        imgVoice.setOnClickListener(this);
        btnJapIn = findViewById(R.id.btn_jap_in);
        btnJapIn.setOnClickListener(this);
        btnJapOut = findViewById(R.id.btn_jap_out);
        btnJapOut.setOnClickListener(this);
        textOutput = findViewById(R.id.text_output);
        textOutput.setOnClickListener(this);
        textInput = findViewById(R.id.text_input);
        textInput.setOnClickListener(this);
        imgSpeak2 = findViewById(R.id.img_voice_output2);
        imgSpeak2.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                clear();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_voice_output1:
                listenSpeechOutput(textInput.getText().toString() + "", true);
                break;
            case R.id.img_voice_output2:
                listenSpeechOutput(textOutput.getText().toString() + "", false);
                break;
            case R.id.img_voice_input:
                promptSpeechInput();
                break;
            case R.id.btn_jap_in:
                transFromJapanese();
                break;
            case R.id.btn_jap_out:
                transToJapanese();
                break;
            case R.id.text_output:
                break;
            case R.id.text_input:
                break;
        }
    }

    private void listenSpeechOutput(String text, boolean b) {
        if (b){
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        } else {
            tts.setLanguage(Locale.JAPANESE);
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            tts.setLanguage(Locale.getDefault());
        }

    }

    private void clear() {
        textInput.setText("");
        textOutput.setText("");
    }

    private void transToJapanese() {
        textOutput.setText(R.string.translating);
        Log.i("Button click:", "Button clicked");
        if (getLocalLanguage()) {
            languagePair = "vi-ja";
        } else languagePair = "en-ja";
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
                        textOutput.setText(resultString);
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
                textInput.getWindowToken(), 0);
    }

    private void transFromJapanese() {
        textOutput.setText(R.string.translating);
        if (getLocalLanguage()) {
            languagePair = "ja-vi";
        } else languagePair = "ja-en";
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
                        textOutput.setText(resultString);
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
                textInput.getWindowToken(), 0);
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, getLocalLanguage());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.i("Speech input result: ", result + "");
                    textInput.setText(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public boolean getLocalLanguage() {
        if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            return false;
        }
        return true;
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.getDefault());

            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
//                listenSpeechOutput();
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }


}
