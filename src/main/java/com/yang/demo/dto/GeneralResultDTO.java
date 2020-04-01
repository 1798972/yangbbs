package com.yang.demo.dto;

import com.yang.demo.exception.CustomizeErrorCode;
import com.yang.demo.exception.CustomizeException;
import lombok.Data;

/**
 * @Author Yiang37
 * @Date 2020/3/31 22:11
 * Description:
 */
@Data
public class GeneralResultDTO<T> {

    private Integer code;
    private String message;
    private T data;

    public static GeneralResultDTO errorOf(Integer code, String message) {
        GeneralResultDTO resultDTO = new GeneralResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    public static GeneralResultDTO errorOf(CustomizeErrorCode notLogin) {
        return errorOf(notLogin.getCode(), notLogin.getMessage());
    }

    public static GeneralResultDTO errorOf(CustomizeException e) {
        return GeneralResultDTO.errorOf(e.getCode(), e.getMessage());
    }

    //请求成功
    public static GeneralResultDTO okOf() {
        GeneralResultDTO resultDTO = new GeneralResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功！");
        return resultDTO;
    }

    //请求成功
    public static <T> GeneralResultDTO okOf(T t) {
        GeneralResultDTO resultDTO = new GeneralResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功！");
        resultDTO.setData(t);
        return resultDTO;
    }

}