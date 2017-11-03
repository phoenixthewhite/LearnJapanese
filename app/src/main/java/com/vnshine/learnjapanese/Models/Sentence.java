package com.vnshine.learnjapanese.Models;

/**
 * Created by phoenix on 30/10/17.
 */

public class Sentence {
    int id;
    int category_id;
    String english;
    String pinyin;
    String japanese;
    int favorite;
    String voice;
    int status;
    String vietnamese;

    public Sentence(int id, int category_id, String english, String pinyin,
                    String japanese, int favorite, String voice, int status,
                    String vietnamese) {
        this.id = id;
        this.category_id = category_id;
        this.english = english;
        this.pinyin = pinyin;
        this.japanese = japanese;
        this.favorite = favorite;
        this.voice = voice;
        this.status = status;
        this.vietnamese = vietnamese;
    }

    public Sentence() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getJapanese() {
        return japanese;
    }

    public void setJapanese(String japanese) {
        this.japanese = japanese;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public void setVietnamese(String vietnamese) {
        this.vietnamese = vietnamese;
    }
}
