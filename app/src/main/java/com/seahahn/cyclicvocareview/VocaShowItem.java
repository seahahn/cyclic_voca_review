package com.seahahn.cyclicvocareview;

public class VocaShowItem {

    private boolean showingSide;
    private String vocaShowText;
    private String image;
    private String addedDate;
    private String vocagroupName;

    public VocaShowItem(boolean showingSide, String vocaShowText, String image, String addedDate, String vocagroupName) {
        this.showingSide = showingSide;
        this.vocaShowText = vocaShowText;
        this.image = image;
        this.addedDate = addedDate;
        this.vocagroupName = vocagroupName;
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

    public String getVocagroupName() {
        return vocagroupName;
    }

    public void setVocagroupName(String vocagroupName) {
        this.vocagroupName = vocagroupName;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("VocaShowItem{");
        sb.append("showingSide=").append(showingSide);
        sb.append(", vocaShowText='").append(vocaShowText).append('\'');
        sb.append(", image='").append(image).append('\'');
        sb.append(", addedDate='").append(addedDate).append('\'');
        sb.append(", vocagroupName='").append(vocagroupName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
