package com.seahahn.cyclicvocareview;

public class VocaLearningCycleArea {

    private String vocaLearningCycleAreaNumber;
    private String EditText_vocaLearningCycle_vocaLearningCycleAreaInput;

    public VocaLearningCycleArea(String vocaLearningCycleAreaNumber, String editText_vocaLearningCycle_vocaLearningCycleAreaInput) {
        this.vocaLearningCycleAreaNumber = vocaLearningCycleAreaNumber;
        this.EditText_vocaLearningCycle_vocaLearningCycleAreaInput = editText_vocaLearningCycle_vocaLearningCycleAreaInput;
    }

    public String getVocaLearningCycleAreaNumber() {
        return vocaLearningCycleAreaNumber;
    }

    public void setVocaLearningCycleAreaNumber(String vocaLearningCycleAreaNumber) {
        this.vocaLearningCycleAreaNumber = vocaLearningCycleAreaNumber;
    }

    public String getEditText_vocaLearningCycle_vocaLearningCycleAreaInput() {
        return EditText_vocaLearningCycle_vocaLearningCycleAreaInput;
    }

    public void setEditText_vocaLearningCycle_vocaLearningCycleAreaInput(String editText_vocaLearningCycle_vocaLearningCycleAreaInput) {
        EditText_vocaLearningCycle_vocaLearningCycleAreaInput = editText_vocaLearningCycle_vocaLearningCycleAreaInput;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("VocaLearningCycleArea{");
        sb.append("vocaLearningCycleAreaNumber='").append(vocaLearningCycleAreaNumber).append('\'');
        sb.append(", EditText_vocaLearningCycle_vocaLearningCycleAreaInput='").append(EditText_vocaLearningCycle_vocaLearningCycleAreaInput).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
