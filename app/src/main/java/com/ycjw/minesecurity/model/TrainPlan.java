package com.ycjw.minesecurity.model;

import java.util.Date;

public class TrainPlan {

    /**
     * 培训通知id
     */
    private String trainId;

    /**
     * 通知标题
     */
    private String planName;

    /**
     * 培训通知内容
     */
    private String trainContent;

    /**
     * 发布时间
     */
    private Date createTime;

    /**
     * 培训日期
     */
    private Date trainTime;

    /**
     * 标识符 0-培训通知 1-考试通知
     */
    private int planType;


    public TrainPlan() {
    }

    public TrainPlan(String trainId, String planName, String trainContent, Date createTime, Date trainTime, int planType) {
        this.trainId = trainId;
        this.planName = planName;
        this.trainContent = trainContent;
        this.createTime = createTime;
        this.trainTime = trainTime;
        this.planType = planType;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public String getTrainContent() {
        return trainContent;
    }

    public void setTrainContent(String trainContent) {
        this.trainContent = trainContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getTrainTime() {
        return trainTime;
    }

    public void setTrainTime(Date trainTime) {
        this.trainTime = trainTime;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public int getPlanType() {
        return planType;
    }

    public void setPlanType(int planType) {
        this.planType = planType;
    }

    @Override
    public String toString() {
        return "TrainPlan{" +
                "trainId='" + trainId + '\'' +
                ", trainContent='" + trainContent + '\'' +
                ", createTime=" + createTime +
                ", trainTime=" + trainTime +
                '}';
    }


}
