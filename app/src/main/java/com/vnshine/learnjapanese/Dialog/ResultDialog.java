package com.vnshine.learnjapanese.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.vnshine.learnjapanese.Models.Sentence;
import com.vnshine.learnjapanese.R;

import java.util.Locale;

/**
 * Created by phoenix on 12/8/17.
 */

public class ResultDialog extends Dialog {
    Context context;
    private TextView correctness;
    private TextView text1;
    private TextView text2;
    private boolean result;
    private Sentence sentence;
    private Button next;

    public ResultDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_test_result);
        correctness  = findViewById(R.id.txt_dialog_correctness);
        text1 = findViewById(R.id.txt_dialog_text1);
        text2 = findViewById(R.id.txt_dialog_text2);
        next = findViewById(R.id.btn_dialog_next);
        setContent();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void getContent(boolean result, Sentence sentence){
        this.result = result;
        this.sentence = sentence;
    }

    public void setContent(){
        if (result){
            correctness.setText(R.string.correct);
            correctness.setTextColor(Color.GREEN);
            text1.setText(sentence.getJapanese());
            if (Locale.getDefault().getDisplayLanguage().equals("English")){
                text2.setText(sentence.getEnglish());
            }else text2.setText(sentence.getVietnamese());
        }else {
            correctness.setText(R.string.incorrect);
            correctness.setTextColor(Color.RED);
            text1.setText(context.getString(R.string.correct_answer)+sentence.getJapanese());
            if (Locale.getDefault().getDisplayLanguage().equals("English")){
                text2.setText(sentence.getEnglish());
            }else text2.setText(sentence.getVietnamese());
        }
    }

}
