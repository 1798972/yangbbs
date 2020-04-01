package com.yang.demo.exception;

/**
 * @Author: Yiang37
 * @Description:
 * @Date: Create in 22:41 2019/12/8
 */
//传递值给CustomizeException
public interface ICustomizeErrorCode {
    //接口中的变量public static final
    //final 最终的 不可修改

    String getMessage();

    Integer getCode();

}

