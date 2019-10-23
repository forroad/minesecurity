package com.ycjw.minesecurity.model;

import java.util.Date;

public class TrainingBack {
    private String trainingBackId;
    private String telephone;
    private String content;
    private Date createDate;

    public TrainingBack() {
    }

    public TrainingBack(String trainingBackId, String telephone, String content, Date createDate) {
        this.trainingBackId = trainingBackId;
        this.telephone = telephone;
        this.content = content;
        this.createDate = createDate;
    }

    public String getTrainingBackId() {
        return trainingBackId;
    }

    public void setTrainingBackId(String trainingBackId) {
        this.trainingBackId = trainingBackId;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "TrainingBack{" +
                "trainingBackId='" + trainingBackId + '\'' +
                ", telephone='" + telephone + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
