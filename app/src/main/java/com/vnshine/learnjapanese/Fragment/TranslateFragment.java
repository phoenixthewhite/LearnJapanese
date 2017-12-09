package com.vnshine.learnjapanese.Fragment;

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

/**
 * Created by phoenix on 12/8/17.
 */

public class TranslateFragment extends Fragment {

    private TextView    question;
    private ImageView   speaker;
    private TextView  translateSentence;
    private ImageView   help;
    private EditText    result;

    private Sentence    sentence;

    public Sentence getSentence() {
        return sentence;
    }

    public void setSentence(Sentence sentence) {
        this.sentence = sentence;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        result = view.findViewById(R.id.edt_translate_text);
        return view;
    }
}
