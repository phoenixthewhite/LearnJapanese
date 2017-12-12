package com.vnshine.learnjapanese.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.vnshine.learnjapanese.Activities.TestActivity;
import com.vnshine.learnjapanese.Models.Sentence;
import com.vnshine.learnjapanese.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

/**
 * Created by phoenix on 12/8/17.
 */

public class ChoiceFragment extends Fragment implements View.OnClickListener{

    private TextView question;
    private TextView choiceSentence;
    private RadioGroup radioGroup;
    private RadioButton first;
    private RadioButton second;
    private RadioButton third;
    private RadioButton fourth;
    private ImageView speaker;
    private int selectedTest;

    private ArrayList<Sentence> randomSentence;
    private Sentence sentence;
    private MediaPlayer mp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_choice, container, false);
        question = view.findViewById(R.id.txt_choice_question);
        speaker = view.findViewById(R.id.img_choice_speaker);
        radioGroup = view.findViewById(R.id.radio_group);
        first = view.findViewById(R.id.radio_first);
        second = view.findViewById(R.id.radio_second);
        third = view.findViewById(R.id.radio_third);
        fourth = view.findViewById(R.id.radio_fourth);
        choiceSentence = view.findViewById(R.id.txt_choice_sentence);
        speaker.setOnClickListener(this);
        selectedTest = selectTest();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                TestActivity.setEnableCheck();

            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp = new MediaPlayer();
    }

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    private int selectTest() {
        Random random = new Random();
        int a = random.nextInt(4);
        switch (a) {
            case 0: {
                test0();
                break;
            }
            case 1: {
                test1();
                break;
            }
            case 2: {
                test2();
                break;
            }
            case 3: {
                test3();
                break;
            }
        }

        return a;
    }

    // japanese to local language
    private void test3() {
        question.setText(R.string.choose_the_right_meaning_of_this_sentence);
        choiceSentence.setText(sentence.getJapanese());
        Log.e("sentence: ", sentence.getEnglish());
        randomSentence.add(sentence);
        Collections.shuffle(randomSentence);
        if (Locale.getDefault().getDisplayLanguage().equals("English")){
            first.setText(randomSentence.get(0).getEnglish());
            second.setText(randomSentence.get(1).getEnglish());
            third.setText(randomSentence.get(2).getEnglish());
            fourth.setText(randomSentence.get(3).getEnglish());
        }else {
            first.setText(randomSentence.get(0).getVietnamese());
            second.setText(randomSentence.get(1).getVietnamese());
            third.setText(randomSentence.get(2).getVietnamese());
            fourth.setText(randomSentence.get(3).getVietnamese());
        }

    }

    // audio to local language
    private void test2() {
        question.setText(R.string.choose_the_correct_meaning_of_the_sentence_that_you_heard);
        choiceSentence.setText("");
        Log.e("sentence: ", sentence.getEnglish());
        randomSentence.add(sentence);
        Collections.shuffle(randomSentence);
        if (Locale.getDefault().getDisplayLanguage().equals("English")){
            first.setText(randomSentence.get(0).getEnglish());
            second.setText(randomSentence.get(1).getEnglish());
            third.setText(randomSentence.get(2).getEnglish());
            fourth.setText(randomSentence.get(3).getEnglish());
        }else {
            first.setText(randomSentence.get(0).getVietnamese());
            second.setText(randomSentence.get(1).getVietnamese());
            third.setText(randomSentence.get(2).getVietnamese());
            fourth.setText(randomSentence.get(3).getVietnamese());
        }
    }

    //local language to japanese
    private void test1() {
        question.setText(R.string.choose_the_right_meaning_of_this_sentence);
        if (Locale.getDefault().getDisplayLanguage().equals("English")){
            choiceSentence.setText(sentence.getEnglish());
        }else choiceSentence.setText(sentence.getVietnamese());
        Log.e("sentence: ", sentence.getEnglish());
        randomSentence.add(sentence);
        Collections.shuffle(randomSentence);
        first.setText(randomSentence.get(0).getJapanese());
        second.setText(randomSentence.get(1).getJapanese());
        third.setText(randomSentence.get(2).getJapanese());
        fourth.setText(randomSentence.get(3).getJapanese());
    }

    //audio to japanese
    private void test0() {
        question.setText(R.string.choose_the_sentence_that_you_heard);
        choiceSentence.setText("");
        randomSentence.add(sentence);
        Collections.shuffle(randomSentence);
        first.setText(randomSentence.get(0).getJapanese());
        second.setText(randomSentence.get(1).getJapanese());
        third.setText(randomSentence.get(2).getJapanese());
        fourth.setText(randomSentence.get(3).getJapanese());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_choice_speaker:{
                playAudio();
            }
        }
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

    public void setRandomSentence(ArrayList<Sentence> randomSentence) {
        this.randomSentence = randomSentence;
    }

    public ArrayList<Sentence> getRandomSentence() {
        return randomSentence;
    }

    public boolean getResult(){
        if (selectedTest == 0 || selectedTest == 1){
            if (getCheckedSentence().equals(sentence.getJapanese())){
                return true;
            }
            return false;
        }else {
            if (Locale.getDefault().getDisplayLanguage().equals("English")){
                if (getCheckedSentence().equals(sentence.getEnglish())){
                    return true;
                }
                return false;
            }else {
                if (getCheckedSentence().equals(sentence.getVietnamese())){
                    return true;
                }
                return false;
            }
        }
    }

    private String getCheckedSentence(){
        String s;
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.radio_first: {
                s = String.valueOf(first.getText());
                break;
            }
            case R.id.radio_second: {
                s = String.valueOf(second.getText());
                break;
            }
            case R.id.third:{
                s = String.valueOf(third.getText());
                break;
            }
            case R.id.radio_fourth:{
                s = String.valueOf(fourth.getText());
                break;
            }
            default: {
                s = "";
                break;
            }
        }
        return s;
    }

}
