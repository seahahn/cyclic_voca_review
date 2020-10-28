package com.example.vocacyclicreview.vocagroup;


import java.io.Serializable;

public class VocagroupArea implements Serializable {

    private String vocagroupAreaNumber;
    private String EditText_vocagroupAdd_vocagroupAreaInput;
    private boolean Switch_vocagroupAdd_areaSwitch;

    public VocagroupArea(String vocagroupAreaNumber, String editText_vocagroupAdd_vocagroupAreaInput, boolean switch_vocagroupAdd_areaSwitch) {
        this.vocagroupAreaNumber = vocagroupAreaNumber;
        EditText_vocagroupAdd_vocagroupAreaInput = editText_vocagroupAdd_vocagroupAreaInput;
        Switch_vocagroupAdd_areaSwitch = switch_vocagroupAdd_areaSwitch;
    }

    public String getVocagroupAreaNumber() {
        return vocagroupAreaNumber;
    }

    public void setVocagroupAreaNumber(String vocagroupAreaNumber) {
        this.vocagroupAreaNumber = vocagroupAreaNumber;
    }

    public String getEditText_vocagroupAdd_vocagroupAreaInput() {
        return EditText_vocagroupAdd_vocagroupAreaInput;
    }

    public void setEditText_vocagroupAdd_vocagroupAreaInput(String editText_vocagroupAdd_vocagroupAreaInput) {
        EditText_vocagroupAdd_vocagroupAreaInput = editText_vocagroupAdd_vocagroupAreaInput;
    }

    public boolean isSwitch_vocagroupAdd_areaSwitch() {
        return Switch_vocagroupAdd_areaSwitch;
    }

    public void setSwitch_vocagroupAdd_areaSwitch(boolean switch_vocagroupAdd_areaSwitch) {
        Switch_vocagroupAdd_areaSwitch = switch_vocagroupAdd_areaSwitch;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("VocagroupArea{");
        sb.append("vocagroupAreaNumber='").append(vocagroupAreaNumber).append('\'');
        sb.append(", EditText_vocagroupAdd_vocagroupAreaInput='").append(EditText_vocagroupAdd_vocagroupAreaInput).append('\'');
        sb.append(", Switch_vocagroupAdd_areaSwitch=").append(Switch_vocagroupAdd_areaSwitch);
        sb.append('}');
        return sb.toString();
    }
}
