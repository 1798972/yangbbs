package com.yang.demo.enums;

public enum QuestionStateEnums {
    SUCCESS_TO_PUBLISH(9200,"问题发布成功"),
    TITLE_IS_ERROR(9201,"问题标题为空"),
    DESCRIPTION_IS_ERROR(9202,"问题内容为空"),
    TAG_IS_ERROR(9203,"问题标签为空"),
    USER_IS_UNLOGIN(9204,"用户未登录"),
    INSERT_ONE_QUESTION_ERROR(9205,"服务器故障");

    private Integer code;
    private String des;

    QuestionStateEnums(Integer code, String des) {
        this.code = code;
        this.des = des;
    }

    public Integer getCode() {
        return code;
    }

    public String getDes() {
        return des;
    }
}
