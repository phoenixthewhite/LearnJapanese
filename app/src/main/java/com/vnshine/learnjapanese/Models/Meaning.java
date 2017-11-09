package com.vnshine.learnjapanese.Models;

/**
 * Created by phoenix on 09/11/17.
 */

public class Meaning {
    private int id;
    private int category_id;
    private String english;
    private int favorite;
    private String voice;
    private int status;
    private String vietnamese;

    public Meaning(int id, int category_id, String english,
                   int favorite, String voice, int status, String vietnamese) {
        this.id = id;
        this.category_id = category_id;
        this.english = english;
        this.favorite = favorite;
        this.voice = voice;
        this.status = status;
        this.vietnamese = vietnamese;
    }

    public Meaning() {
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
