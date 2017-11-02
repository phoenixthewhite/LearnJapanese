package com.vnshine.learnjapanese.Models;

/**
 * Created by phoenix on 26/10/17.
 */

public class GridViewItem {
    private int image;
    private Category category;

    public GridViewItem(int image, Category category) {
        this.image = image;
        this.category = category;
    }

    public GridViewItem() {
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
