package com.example.vocacyclicreview;

import java.util.ArrayList;

public class VocaLearningCycle {

    private String vocaLearningCycleName;
    private String vocaLearningCycleArea1;
    private String vocaLearningCycleArea2;
    private ArrayList<VocaLearningCycleArea> vocaLearningCycleAreaList;

    public VocaLearningCycle(String vocaLearningCycleName, String vocaLearningCycleArea1, String vocaLearningCycleArea2, ArrayList<VocaLearningCycleArea> vocaLearningCycleAreaList) {
        this.vocaLearningCycleName = vocaLearningCycleName;
        this.vocaLearningCycleArea1 = vocaLearningCycleArea1;
        this.vocaLearningCycleArea2 = vocaLearningCycleArea2;
        this.vocaLearningCycleAreaList = vocaLearningCycleAreaList;
    }

    public String getVocaLearningCycleName() {
        return vocaLearningCycleName;
    }

    public void setVocaLearningCycleName(String vocaLearningCycleName) {
        this.vocaLearningCycleName = vocaLearningCycleName;
    }

    public String getVocaLearningCycleArea1() {
        return vocaLearningCycleArea1;
    }

    public void setVocaLearningCycleArea1(String vocaLearningCycleArea1) {
        this.vocaLearningCycleArea1 = vocaLearningCycleArea1;
    }

    public String getVocaLearningCycleArea2() {
        return vocaLearningCycleArea2;
    }

    public void setVocaLearningCycleArea2(String vocaLearningCycleArea2) {
        this.vocaLearningCycleArea2 = vocaLearningCycleArea2;
    }

    public ArrayList<VocaLearningCycleArea> getVocaLearningCycleAreaList() {
        return vocaLearningCycleAreaList;
    }

    public void setVocaLearningCycleAreaList(ArrayList<VocaLearningCycleArea> vocaLearningCycleAreaList) {
        this.vocaLearningCycleAreaList = vocaLearningCycleAreaList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("VocaLearningCycle{");
        sb.append("vocaLearningCycleName='").append(vocaLearningCycleName).append('\'');
        sb.append(", vocaLearningCycleArea1='").append(vocaLearningCycleArea1).append('\'');
        sb.append(", vocaLearningCycleArea2='").append(vocaLearningCycleArea2).append('\'');
        sb.append(", vocaLearningCycleAreaList=").append(vocaLearningCycleAreaList);
        sb.append('}');
        return sb.toString();
    }
}
