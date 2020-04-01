package com.yang.demo.exception;

/**
 * @Author: Yiang37
 * @Description: 错误页面 捕捉的异常
 * @Date: Create in 22:44 2019/12/8
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {

    SYSTEM_ERROR(9900, "系统出错了,稍后再来试试吧..."),
    ADDRESS_INPUT_ERROR(9901, "非法的请求"),
    QUESRION_NOT_EXIST(9902, "你要找的问题不存在了..."),
    FILE_UPLOAD_ERROR(9903, "文件上传失败了...");

    private Integer code;
    private String message;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}
