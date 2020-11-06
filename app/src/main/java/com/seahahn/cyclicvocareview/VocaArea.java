package com.seahahn.cyclicvocareview;

import android.graphics.Bitmap;

import java.io.Serializable;

public class VocaArea implements Serializable {

    private String vocaAreaName;
    private String EditText_vocaAdd_vocaAreaInput;
    private boolean Switch_vocaAdd_areaSwitch;
    private Bitmap ImageView_vocaAdd_imageView;
    private String ImageView_vocaAdd_photoPath;

    public VocaArea(String vocaAreaName, String editText_vocaAdd_vocaAreaInput, boolean switch_vocaAdd_areaSwitch, Bitmap imageView_vocaAdd_imageView, String imageView_vocaAdd_photoPath) {
        this.vocaAreaName = vocaAreaName;
        EditText_vocaAdd_vocaAreaInput = editText_vocaAdd_vocaAreaInput;
        Switch_vocaAdd_areaSwitch = switch_vocaAdd_areaSwitch;
        ImageView_vocaAdd_imageView = imageView_vocaAdd_imageView;
        ImageView_vocaAdd_photoPath = imageView_vocaAdd_photoPath;
    }

    public String getVocaAreaName() {
        return vocaAreaName;
    }

    public void setVocaAreaName(String vocaAreaName) {
        this.vocaAreaName = vocaAreaName;
    }

    public String getEditText_vocaAdd_vocaAreaInput() {
        return EditText_vocaAdd_vocaAreaInput;
    }

    public void setEditText_vocaAdd_vocaAreaInput(String editText_vocaAdd_vocaAreaInput) {
        EditText_vocaAdd_vocaAreaInput = editText_vocaAdd_vocaAreaInput;
    }

    public boolean isSwitch_vocaAdd_areaSwitch() {
        return Switch_vocaAdd_areaSwitch;
    }

    public void setSwitch_vocaAdd_areaSwitch(boolean switch_vocaAdd_areaSwitch) {
        Switch_vocaAdd_areaSwitch = switch_vocaAdd_areaSwitch;
    }

    public Bitmap getImageView_vocaAdd_imageView() {
        return ImageView_vocaAdd_imageView;
    }

    public void setImageView_vocaAdd_imageView(Bitmap imageView_vocaAdd_imageView) {
        ImageView_vocaAdd_imageView = imageView_vocaAdd_imageView;
    }

    public String getImageView_vocaAdd_photoPath() {
        return ImageView_vocaAdd_photoPath;
    }

    public void setImageView_vocaAdd_photoPath(String imageView_vocaAdd_photoPath) {
        ImageView_vocaAdd_photoPath = imageView_vocaAdd_photoPath;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("VocaArea{");
        sb.append("vocaAreaName='").append(vocaAreaName).append('\'');
        sb.append(", EditText_vocaAdd_vocaAreaInput='").append(EditText_vocaAdd_vocaAreaInput).append('\'');
        sb.append(", Switch_vocaAdd_areaSwitch=").append(Switch_vocaAdd_areaSwitch);
        sb.append(", ImageView_vocaAdd_imageView=").append(ImageView_vocaAdd_imageView);
        sb.append('}');
        return sb.toString();
    }
}
