package com.vnshine.learnjapanese.Models;

import com.vnshine.learnjapanese.Translator.ApiUtils;

import retrofit2.Call;

/**
 * Created by phoenix on 12/6/17.
 */

public class TranslateText {
    String textToBeTranslate="";
    String languagePair;
    String textResult;

    public TranslateText() {
    }

    public String getTextToBeTranslate() {
        return textToBeTranslate;
    }

    public void setTextToBeTranslate(String textToBeTranslate) {
        this.textToBeTranslate = textToBeTranslate;
    }
    public String getLanguagePair() {
        return languagePair;
    }

    public void setLanguagePair(String languagePair) {
        this.languagePair = languagePair;
    }

    public String getTextResult() {
        return textResult;
    }

    public void setTextResult(String textResult) {
        this.textResult = textResult;
    }
    //    public void translateLanguage(Context context, TextView textView) {
//        TranslatorBackgroundTask translatorBackgroundTask = new TranslatorBackgroundTask(context, textView);
//        translatorBackgroundTask.execute(getTextToBeTranslate(), getLanguagePair()); // Returns the translated text as a String
//    }
    public Call<String> translateLanguage() {
        return ApiUtils.getTranslateApi().translateText("trnsl.1.1.20170922T032030Z.1d23cd16a7da6df4.45229e7af371a1e88bb428c6501475d20e46e30a",getTextToBeTranslate(),getLanguagePair());
    }
}
