package com.vnshine.learnjapanese.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vnshine.learnjapanese.Models.Sentence;
import com.vnshine.learnjapanese.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by phoenix on 12/8/17.
 */

public class PronounceFragment extends Fragment implements RecognitionListener, View.OnClickListener {
    Sentence sentence;
    TextView question;
    ImageView speaker;
    TextView japanese;
    TextView pinyin;
    TextView result;
    ImageView micButton;
    ProgressBar soundWave;
    private SpeechRecognizer speech;
    private Intent recognizerIntent;
    private MediaPlayer mp;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        promptSpeechInput();
        mp = new MediaPlayer();
    }

    private void promptSpeechInput() {
        speech = SpeechRecognizer.createSpeechRecognizer(getActivity());
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE + "",
                Locale.JAPAN.toString());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE + "",
                getActivity().getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL + "",
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS + "", 1);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_pronounce, container, false);
        question = view.findViewById(R.id.txt_pronounce_question);
        micButton = view.findViewById(R.id.img_micButton);
        japanese = view.findViewById(R.id.txt_pronounce_japanese);
        pinyin = view.findViewById(R.id.txt_pronounce_pinyin);
        result = view.findViewById(R.id.txt_result);
        soundWave = view.findViewById(R.id.sound_wave);
        speaker = view.findViewById(R.id.img_pronounce_speaker);

        japanese.setText(sentence.getJapanese());
        pinyin.setText("(" + sentence.getPinyin() + ")");
        micButton.setOnClickListener(this);
        speaker.setOnClickListener(this);
        question.setText(R.string.read_exactly_this_sentence);
        return view;
    }

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {
        soundWave.setIndeterminate(false);
        soundWave.setMax(10);
        result.setText("Listening...");
        result.setTextColor(Color.BLUE);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        soundWave.setProgress((int) rmsdB);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        soundWave.setIndeterminate(false);
        soundWave.setVisibility(View.INVISIBLE);
        micButton.setImageResource(R.drawable.micro);
        result.setText(R.string.processing);
        micButton.setBackgroundResource(R.drawable.gray_ring);
    }

    @Override
    public void onError(int error) {
        String errorMessage = getErrorText(error);
//        Log.d(LOG_TAG, "FAILED " + errorMessage);
        result.setTextColor(Color.RED);
        result.setText(errorMessage);
        micButton.setImageResource(R.drawable.micro);
        micButton.setBackgroundResource(R.drawable.gray_ring);
    }

    @Override
    public void onResults(Bundle results) {
//        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = matches.get(0);
        String checksum = sentence.getJapanese();
        checksum = checksum.replaceAll("\\(.*?\\) ?", "");
        checksum = checksum.replaceAll("\\.", "");
        System.out.println(text);
        if (text.equals(checksum)) {
            result.setTextColor(Color.GREEN);
            result.setText(R.string.correct);
        } else {
            result.setTextColor(Color.RED);
            result.setText(R.string.incorrect);
            System.out.println(text);
        }
    }

    public boolean getResult() {
        if (result.getText().equals(R.string.correct)) {
            return true;
        }
        return false;
    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_pronounce_speaker: {
                playAudio();
                break;
            }
            case R.id.img_micButton: {
                speak();
                break;
            }
        }
    }

    private void speak() {
        micButton.setImageResource(R.drawable.micro_focus);
        micButton.setBackgroundResource(R.drawable.red_ring);
        soundWave.setVisibility(View.VISIBLE);
        soundWave.setIndeterminate(true);
        result.setText("Listening...");
        result.setTextColor(Color.BLUE);
        speech.startListening(recognizerIntent);
    }

    private void playAudio() {
        int identifier = getActivity().getResources().getIdentifier(sentence.getVoice()
                + "_f", "raw", getActivity().getPackageName());
        speaker.setImageResource(R.drawable.listen_focus);
        if (mp.isPlaying()) {
            mp.stop();
            mp = MediaPlayer.create(getActivity(), identifier);
            mp.start();
        } else {
            mp = MediaPlayer.create(getActivity(), identifier);
            mp.start();
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                speaker.setImageResource(R.drawable.listen);
            }
        });
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
