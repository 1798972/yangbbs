package com.yang.demo.enums;

public enum CommentStateEnums {
    COMMENT_QUESTION_SUCCESS(9300,"发表回复成功"),
    COMMENT_COMMENT_SUCCESS(9301,"发表评论成功"),
    CONTENT_IS_NULL(9302,"内容为空!"),
    USER_IS_NULL(9303,"用户未登录!"),
    USER_IS_ERROR(9304,"非法的用户请求!"),
    QUESTION_IS_NULL(9305,"问题不存在了"),

    INSERT_QUESTION_COMMENT_ERROR(9306,"回复问题失败,服务器出错了..."),
    INSERT_COMMENT_COMMENT_ERROR(9307,"回复评论失败,服务器出错了...");


    private Integer code;
    private String des;

    CommentStateEnums(Integer code, String des) {
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
