package com.ycjw.minesecurity.model;


public class SelectionQuestion {

    /**
     * 题目编号
     */
    private String questionId;

    /**
     * 题目描述
     */
    private String questionDescribe;

    /**
     *是否为多选，0-否，1-是
     */
    private int isMultiAnswer;

    /**
     * 选项 A
     */
    private String optionA;

    private String optionB;

    private String optionC;

    private String optionD;

    /**
     * 正确答案
     */
    private String rightOptions;

    public SelectionQuestion() {
    }

    public SelectionQuestion(String questionId, String questionDescribe, int isMultiAnswer, String optionA, String optionB, String optionC, String optionD, String rightOptions) {
        this.questionId = questionId;
        this.questionDescribe = questionDescribe;
        this.isMultiAnswer = isMultiAnswer;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.rightOptions = rightOptions;
    }

    public SelectionQuestion(String questionDescribe, int isMultiAnswer, String optionA, String optionB, String optionC, String optionD, String rightOptions) {
        this.questionDescribe = questionDescribe;
        this.isMultiAnswer = isMultiAnswer;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.rightOptions = rightOptions;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionDescribe() {
        return questionDescribe;
    }

    public void setQuestionDescribe(String questionDescribe) {
        this.questionDescribe = questionDescribe;
    }

    public int getIsMultiAnswer() {
        return isMultiAnswer;
    }

    public void setIsMultiAnswer(int isMultiAnswer) {
        this.isMultiAnswer = isMultiAnswer;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getRightOptions() {
        return rightOptions;
    }

    public void setRightOptions(String rightOptions) {
        this.rightOptions = rightOptions;
    }

    @Override
    public String toString() {
        return "SelectionQuestion{" +
                "questionId='" + questionId + '\'' +
                ", questionDescribe='" + questionDescribe + '\'' +
                ", isMultiAnswer=" + isMultiAnswer +
                ", optionA='" + optionA + '\'' +
                ", optionB='" + optionB + '\'' +
                ", optionC='" + optionC + '\'' +
                ", optionD='" + optionD + '\'' +
                ", rightOptions='" + rightOptions + '\'' +
                '}';
    }
}
