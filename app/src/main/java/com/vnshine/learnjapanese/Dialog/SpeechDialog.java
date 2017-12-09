package com.vnshine.learnjapanese.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vnshine.learnjapanese.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by phoenix on 12/1/17.
 */

public class SpeechDialog extends Dialog implements RecognitionListener {


    private Button exit;
    private ImageView micButton;
    private ProgressBar progressBar;
    private TextView tResult;
    private TextView tJapanese;
    private TextView tPinyin;
    private ImageView speaker;

    private Context context;

    private String japanese = null;
    private String pinyin = null;
    private int identifier;

    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "VoiceRecognitionActivity";
    private MediaPlayer mp;


    public SpeechDialog(@NonNull Context context) {
        super(context);
        this.context = context;
        mp = new MediaPlayer();
    }

    public void setContent(String japanese, String pinyin, int identifier) {
        this.japanese = japanese;
        this.pinyin = pinyin;
        this.identifier = identifier;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_speech_to_text);
        initComponent();
        playAudio();
        speech = SpeechRecognizer.createSpeechRecognizer(context);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE + "",
                Locale.JAPAN.toString());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE + "",
                context.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL + "",
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS + "", 1);
        addListener();
    }

    private void addListener() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp.stop();
                dismiss();
            }
        });
        micButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                micButton.setImageResource(R.drawable.micro_focus);
                micButton.setBackgroundResource(R.drawable.red_ring);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setIndeterminate(true);
                tResult.setText("Listening...");
                tResult.setTextColor(Color.BLUE);
                speech.startListening(recognizerIntent);
            }
        });
    }

    private void playAudio() {
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speaker.setImageResource(R.drawable.listen_focus);
                if (mp.isPlaying()) {
                    mp.stop();
                    mp = MediaPlayer.create(context, identifier);
                    mp.start();
                } else {
                    mp = MediaPlayer.create(context, identifier);
                    mp.start();
                }
                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        speaker.setImageResource(R.drawable.listen);
                    }
                });
            }
        });
    }

    private void initComponent() {
        mp = MediaPlayer.create(context, identifier);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                speaker.setImageResource(R.drawable.listen);
            }
        });
        speaker = findViewById(R.id.dialog_speaker);
        tJapanese = findViewById(R.id.dialog_japanese);
        tPinyin = findViewById(R.id.dialog_pinyin);
        tResult = findViewById(R.id.dialog_result);
        exit = findViewById(R.id.dialog_exit);
        micButton = findViewById(R.id.dialog_micButton);
        progressBar = findViewById(R.id.progressBar);
        tJapanese.setText(japanese);
        tPinyin.setText(pinyin);
        micButton.setImageResource(R.drawable.micro);
        micButton.setBackgroundResource(R.drawable.gray_ring);

    }


    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
        tResult.setText("Listening...");
        tResult.setTextColor(Color.BLUE);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.INVISIBLE);
        micButton.setImageResource(R.drawable.micro);
        tResult.setText(R.string.processing);
        micButton.setBackgroundResource(R.drawable.gray_ring);
    }

    @Override
    public void onError(int error) {
        String errorMessage = getErrorText(error);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        tResult.setTextColor(Color.RED);
        tResult.setText(errorMessage);
        micButton.setImageResource(R.drawable.micro);
        micButton.setBackgroundResource(R.drawable.gray_ring);
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = matches.get(0);
        japanese = japanese.replaceAll("\\(.*?\\) ?", "");
        japanese = japanese.replaceAll("\\.","");
        System.out.println(text);
        if (text.equals(japanese)) {
            tResult.setTextColor(Color.GREEN);
            tResult.setText(R.string.correct);
        } else {
            tResult.setTextColor(Color.RED);
            tResult.setText(R.string.incorrect);
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(LOG_TAG, "onEvent");
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "Incorrect!";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}
