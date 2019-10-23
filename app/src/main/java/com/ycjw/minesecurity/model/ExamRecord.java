package com.ycjw.minesecurity.model;

import java.util.Date;

public class ExamRecord {

    private String examRecordId;

    /**
     * 用户电话
     */
    private String phoneNum;

    /**
     * 考试id
     */
    private String examId;

    /**
     * 交卷时间
     */
    private Date endTime;

    /**
     * 进入考试的时间
     */
    private Date startTime;

    /**
     * 最终成绩
     */
    private float grade;

    /**
     * 是否已经完成考试 0-未完成 1-已经完成
     */
    private int hasFinished= ExamRecordStateEnum.NO_FINISHED.getCode();

    public ExamRecord() {
    }

    public ExamRecord(String examRecordId, String examId, Date endTime, Date startTime, float grade,int hasFinished,String phoneNum) {
        this.examRecordId = examRecordId;
        this.examId = examId;
        this.endTime = endTime;
        this.startTime = startTime;
        this.grade = grade;
        this.hasFinished=hasFinished;
        this.phoneNum=phoneNum;
    }


    public String getExamRecordId() {
        return examRecordId;
    }

    public void setExamRecordId(String examRecordId) {
        this.examRecordId = examRecordId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public int getHasFinished() {
        return hasFinished;
    }

    public void setHasFinished(int hasFinished) {
        this.hasFinished = hasFinished;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "ExamRecord{" +
                "examRecordId='" + examRecordId + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", examId='" + examId + '\'' +
                ", endTime=" + endTime +
                ", startTime=" + startTime +
                ", grade=" + grade +
                ", hasFinished=" + hasFinished +
                '}';
    }
}
