package com.ycjw.minesecurity.model;

import java.util.Date;

public class Exam {
    private String examId;
    private String examTitle;
    private Date examDeadline;
    private Date examStartTime;
    private Date examEndTime;
    private String paperId;

    public Exam() {
    }

    public Exam(String examId, String examTitle, Date examDeadline, Date examStartTime, Date examEndTime, String paperId) {
        this.examId = examId;
        this.examTitle = examTitle;
        this.examDeadline = examDeadline;
        this.examStartTime = examStartTime;
        this.examEndTime = examEndTime;
        this.paperId = paperId;
    }

    public String getExamId() {
        return examId;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public Date getExamDeadline() {
        return examDeadline;
    }

    public void setExamDeadline(Date examDeadline) {
        this.examDeadline = examDeadline;
    }

    public Date getExamStartTime() {
        return examStartTime;
    }

    public void setExamStartTime(Date examStartTime) {
        this.examStartTime = examStartTime;
    }

    public Date getExamEndTime() {
        return examEndTime;
    }

    public void setExamEndTime(Date examEndTime) {
        this.examEndTime = examEndTime;
    }

    public String getPaperId() {
        return paperId;
    }

    public void setPaperId(String paperId) {
        this.paperId = paperId;
    }

    @Override
    public String toString() {
        return "Exam{" +
                "examId='" + examId + '\'' +
                ", examTitle='" + examTitle + '\'' +
                ", examDeadline=" + examDeadline +
                ", examStartTime=" + examStartTime +
                ", examEndTime=" + examEndTime +
                '}';
    }
}
