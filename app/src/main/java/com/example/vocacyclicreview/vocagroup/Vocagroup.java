package com.example.vocacyclicreview.vocagroup;

import com.example.vocacyclicreview.VocaLearningCycle;

import java.util.ArrayList;

public class Vocagroup {

    private String vocagroupName;
    private String vocaLearningCycle;
    private int vocaLearningCyclePosition;
    private String vocagroupArea1;
    private String vocagroupArea2;
    private boolean vocagroupAreaSwitch1, vocagroupAreaSwitch2;
    private ArrayList<VocagroupArea> vocagroupAreaList;

    public Vocagroup(String vocagroupName, String vocaLearningCycle, int vocaLearningCyclePosition, String vocagroupArea1, String vocagroupArea2, boolean vocagroupAreaSwitch1, boolean vocagroupAreaSwitch2, ArrayList<VocagroupArea> vocagroupAreaList) {
        this.vocagroupName = vocagroupName;
        this.vocaLearningCycle = vocaLearningCycle;
        this.vocaLearningCyclePosition = vocaLearningCyclePosition;
        this.vocagroupArea1 = vocagroupArea1;
        this.vocagroupArea2 = vocagroupArea2;
        this.vocagroupAreaSwitch1 = vocagroupAreaSwitch1;
        this.vocagroupAreaSwitch2 = vocagroupAreaSwitch2;
        this.vocagroupAreaList = vocagroupAreaList;
    }

    public String getVocagroupName() {
        return vocagroupName;
    }

    public void setVocagroupName(String vocagroupName) {
        this.vocagroupName = vocagroupName;
    }

    public String getVocaLearningCycle() {
        return vocaLearningCycle;
    }

    public void setVocaLearningCycle(String vocaLearningCycle) {
        this.vocaLearningCycle = vocaLearningCycle;
    }

    public int getVocaLearningCyclePosition() {
        return vocaLearningCyclePosition;
    }

    public void setVocaLearningCyclePosition(int vocaLearningCyclePosition) {
        this.vocaLearningCyclePosition = vocaLearningCyclePosition;
    }

    public String getVocagroupArea1() {
        return vocagroupArea1;
    }

    public void setVocagroupArea1(String vocagroupArea1) {
        this.vocagroupArea1 = vocagroupArea1;
    }

    public String getVocagroupArea2() {
        return vocagroupArea2;
    }

    public void setVocagroupArea2(String vocagroupArea2) {
        this.vocagroupArea2 = vocagroupArea2;
    }

    public boolean isVocagroupAreaSwitch1() {
        return vocagroupAreaSwitch1;
    }

    public void setVocagroupAreaSwitch1(boolean vocagroupAreaSwitch1) {
        this.vocagroupAreaSwitch1 = vocagroupAreaSwitch1;
    }

    public boolean isVocagroupAreaSwitch2() {
        return vocagroupAreaSwitch2;
    }

    public void setVocagroupAreaSwitch2(boolean vocagroupAreaSwitch2) {
        this.vocagroupAreaSwitch2 = vocagroupAreaSwitch2;
    }

    public ArrayList<VocagroupArea> getVocagroupAreaList() {
        return vocagroupAreaList;
    }

    public void setVocagroupAreaList(ArrayList<VocagroupArea> vocagroupAreaList) {
        this.vocagroupAreaList = vocagroupAreaList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Vocagroup{");
        sb.append("vocagroupName='").append(vocagroupName).append('\'');
        sb.append(", vocaLearningCycle='").append(vocaLearningCycle).append('\'');
        sb.append(", vocaLearningCyclePosition=").append(vocaLearningCyclePosition);
        sb.append(", vocagroupArea1='").append(vocagroupArea1).append('\'');
        sb.append(", vocagroupArea2='").append(vocagroupArea2).append('\'');
        sb.append(", vocagroupAreaSwitch1=").append(vocagroupAreaSwitch1);
        sb.append(", vocagroupAreaSwitch2=").append(vocagroupAreaSwitch2);
        sb.append(", vocagroupAreaList=").append(vocagroupAreaList);
        sb.append('}');
        return sb.toString();
    }
}
