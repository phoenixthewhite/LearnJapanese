package com.vnshine.learnjapanese.Models;

/**
 * Created by phoenix on 30/10/17.
 */

public class ListViewItem {
    String sentence;

    public ListViewItem(String sentence) {
        this.sentence = sentence;
    }

    public ListViewItem() {
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
