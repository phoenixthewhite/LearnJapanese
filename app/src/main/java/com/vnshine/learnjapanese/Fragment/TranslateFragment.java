package com.vnshine.learnjapanese.Fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.vnshine.learnjapanese.Models.Sentence;
import com.vnshine.learnjapanese.R;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by phoenix on 12/8/17.
 */

public class TranslateFragment extends Fragment implements View.OnClickListener {

    private TextView question;
    private ImageView speaker;
    private TextView translateSentence;
    private ImageView help;
    private EditText inputSentence;
    private MediaPlayer mp;
    private Sentence sentence;
    private TextView hint;
    private int selected;
    private String hintJapanese;
    private String hintLocalLanguage;
    private boolean empty;
    public String getHintJapanese() {
        return hintJapanese;
    }

    public void setHintJapanese() {
        String temp = sentence.getJapanese();
        String[] tempSplited = temp.split("");
        for (int i = 0; i < tempSplited.length; i++) {
            tempSplited[i] = "[" + tempSplited[i] + "]";
        }
        sufferArray(tempSplited);
        temp = "";
        for (String s :
                tempSplited) {
            temp += s;
        }
        this.hintJapanese = temp;
    }

    public String getHintLocalLanguage() {
        return hintLocalLanguage;
    }

    public void setHintLocalLanguage() {
        String temp;
        if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            temp = sentence.getEnglish();
        } else temp = sentence.getVietnamese();
        String[] tempSplited = temp.split("\\W+");
        for (int i = 0; i < tempSplited.length; i++) {
            tempSplited[i] = "[" + tempSplited[i] + "]";
        }
        sufferArray(tempSplited);
        temp = "";
        for (String s :
                tempSplited) {
            temp += s;
        }
        this.hintLocalLanguage = temp;
    }

    public int getSelected() {
        return selected;
    }

    private void sufferArray(String[] tempSplited) {
        Random rnd = new Random();
        for (int i = tempSplited.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            String a = tempSplited[index];
            tempSplited[index] = tempSplited[i];
            tempSplited[i] = a;
        }
    }


    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mp = new MediaPlayer();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container
            , @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_translate, container, false);
        question = view.findViewById(R.id.txt_translate_question);
        speaker = view.findViewById(R.id.img_translate_speaker);
        translateSentence = view.findViewById(R.id.txt_translate_sentence);
        help = view.findViewById(R.id.img_help);
        hint = view.findViewById(R.id.txt_hint);
        inputSentence = view.findViewById(R.id.edt_translate_text);
        speaker.setOnClickListener(this);
        help.setOnClickListener(this);
        selected = selectTest();
        return view;
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

    // write the japanese
    private void test3() {
        question.setText(R.string.write_what_you_heard);
        translateSentence.setText("");
    }

    // translate from audio to local language
    private void test2() {
        question.setText(R.string.translate_what_you_heard);
        translateSentence.setText("");
    }

    // translate from local language to japanese
    private void test1() {
        question.setText(R.string.translate_this_sentence);
        if (Locale.getDefault().getDisplayLanguage().equals("English")) {
            translateSentence.setText(sentence.getEnglish());
        } else translateSentence.setText(sentence.getVietnamese());

    }

    // translate from japanese to local language
    private void test0() {
        question.setText(R.string.translate_this_sentence);
        translateSentence.setText(sentence.getJapanese());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_translate_speaker: {
                playAudio();
                break;
            }
            case R.id.img_help: {
                if (getSelected() == 2 || getSelected() == 0) {
                    showHint(true);
                } else showHint(false);
                break;
            }
        }
    }

    private void showHint(boolean b) {
        if (b) {
            setHintLocalLanguage();
            hint.setText(getHintLocalLanguage());
        } else {
            setHintJapanese();
            hint.setText(getHintJapanese());
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

    public boolean getResult() {
        setEmpty();
        if (getSelected() == 0 || getSelected() == 2) {
            String s = String.valueOf(inputSentence.getText());
            s = s.toLowerCase().trim().replaceAll("\\(.*?\\) ?", "")
                    .replaceAll("\\.", "");
            if (Locale.getDefault().getDisplayLanguage().equals("English")) {
                if (s.equals(sentence.getEnglish().toLowerCase()
                        .replaceAll("\\(.*?\\) ?", "")
                        .replaceAll("\\.", ""))) {
                    return true;
                } else return false;
            } else {
                s = removeAccent(s);
                if (s.equals(sentence.getVietnamese())) {
                    return true;
                } else return false;
            }
        } else { //to japanese
            String s = String.valueOf(inputSentence.getText());
            s = s.replaceAll("\\(.*?\\) ?", "")
                    .replaceAll("\\.", "");
            if (s.equals(sentence.getJapanese().replaceAll("\\(.*?\\) ?", "")
                    .replaceAll("\\.", ""))) {
                return true;
            } else return false;

        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty() {
        this.empty = (inputSentence.getText().equals("")) ? true : false;
    }

    private String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}
