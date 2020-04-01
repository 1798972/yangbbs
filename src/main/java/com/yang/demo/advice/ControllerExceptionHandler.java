package com.yang.demo.advice;

import com.alibaba.fastjson.JSON;
import com.yang.demo.dto.GeneralResultDTO;
import com.yang.demo.exception.CustomizeErrorCode;
import com.yang.demo.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * @Author Yiang37
 * @Date 2020/3/31 21:55
 * Description:
 * 异常处理
 */

@ControllerAdvice
public class ControllerExceptionHandler {
    //  throw new CustomizeException(CustomizeErrorCode.SYSTEM_ERROR);
    @ExceptionHandler(Exception.class)
    ModelAndView getErrorPage(HttpServletRequest request, Throwable e, Model model, HttpServletResponse response) {
        //返回类型
        String contentType = request.getContentType();

        //1.json的操作 返回的是一个resultDTO对象
        if ("application/json".equals(contentType)) {
            GeneralResultDTO resultDTO;
            //JSON
            if (e instanceof CustomizeException) {
                //CustomizeException有这个就返回这个
                resultDTO = GeneralResultDTO.errorOf((CustomizeException) e);
            } else {
                //不然返回默认的系统错误
                resultDTO = GeneralResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
            //由于返回的是个view 所以手写返回json对象
            try {
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.setStatus(200);
                PrintWriter writer = response.getWriter();

                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;


            //2.下面是在model里面添加值
        } else {
            //CustomizeException有这个就返回这个
            if (e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            } else {
                //不然返回默认的"系统错误"
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}