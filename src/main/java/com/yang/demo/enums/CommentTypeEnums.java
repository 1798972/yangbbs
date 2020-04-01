package com.yang.demo.enums;

//0问题 1评论
public enum CommentTypeEnums {
    QUESTION_COMMENT(0),
    COMMENT_COMMENT(1);

    private int type;

    CommentTypeEnums(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
