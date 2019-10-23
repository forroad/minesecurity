package com.ycjw.minesecurity.model;

public enum ExamRecordStateEnum {
    HAS_FINISHED(1),
    NO_FINISHED(0),
    ;
    private int code;
    ExamRecordStateEnum(int code){
        this.code=code;
    }

    public int getCode() {
        return code;
    }
}
