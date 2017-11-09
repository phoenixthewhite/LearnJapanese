package com.vnshine.learnjapanese.Models;

/**
 * Created by phoenix on 09/11/17.
 */

public class JapaneseSentence {
    private int id;
    private int category_id;
    private String pinyin;
    private  String japanese;


    public JapaneseSentence(int id, int category_id, String pinyin,
                            String japanese) {
        this.id = id;
        this.category_id = category_id;
        this.pinyin = pinyin;
        this.japanese = japanese;
    }

    public JapaneseSentence() {
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

}
