package com.example.vocacyclicreview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.Date;

public class VocaShowItem {

    private boolean showingSide;
    private String vocaShowText;
    private String image;
    private String addedDate;

    public VocaShowItem(boolean showingSide, String vocaShowText, String image, String addedDate) {
        this.showingSide = showingSide;
        this.vocaShowText = vocaShowText;
        this.image = image;
        this.addedDate = addedDate;
    }

    public boolean isShowingSide() {
        return showingSide;
    }

    public void setShowingSide(boolean showingSide) {
        this.showingSide = showingSide;
    }

    public String getVocaShowText() {
        return vocaShowText;
    }

    public void setVocaShowText(String vocaShowText) {
        this.vocaShowText = vocaShowText;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("VocaShowItem{");
        sb.append("showingSide=").append(showingSide);
        sb.append(", vocaShowText='").append(vocaShowText).append('\'');
        sb.append(", image='").append(image).append('\'');
        sb.append(", addedDate=").append(addedDate);
        sb.append('}');
        return sb.toString();
    }
}
