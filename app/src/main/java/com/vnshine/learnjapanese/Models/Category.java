package com.vnshine.learnjapanese.Models;

/**
 * Created by phoenix on 27/10/17.
 */

public class Category {
    int id;
    String english;
    String vietnamese;

    public Category(int id, String english, String vietnamese) {
        this.id = id;
        this.english = english;
        this.vietnamese = vietnamese;
    }

    public Category() {
    }

    public int getId() {
        return id;
    }

    public String getEnglish() {
        return english;
    }

    public String getVietnamese() {
        return vietnamese;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public void setVietnamese(String vietnamese) {
        this.vietnamese = vietnamese;
    }

}
