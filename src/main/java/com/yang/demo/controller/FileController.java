package com.yang.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yang.demo.exception.CustomizeErrorCode;
import com.yang.demo.exception.CustomizeException;
import com.yang.demo.provider.FileProvider;
import com.yang.demo.provider.QCloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author Yiang37
 * @Date 2020/3/26 21:00
 * |Description:
 * 上传文件相关
 *
 * 图片是可以base64格式存储到数据库的 但是占地方 不用
 */

@Controller
public class FileController {
    @Autowired
    private QCloudProvider qCloudProvider;

    @ResponseBody
    @PostMapping("/file/upload")
    public JSONObject uploadFile(HttpServletRequest request) throws IOException {

        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        //MultipartFile 这个类一般是用来接受前台传过来的文件
        //在spring上传文件中，一般都使用了MultipartFile来接收
        MultipartFile file = multipartHttpServletRequest.getFile("editor_img");

        //进行文件的上传操作
        try {
            //转换成File
            File targetFile = FileProvider.multipartFileToFile(file);
            //上传操作 返回图片url
            String url = qCloudProvider.upload(targetFile != null ? targetFile : null, file.getOriginalFilename());
            //删除临时的file
            FileProvider.delteTempFile(targetFile);


//            JSONArray jsonArray=new JSONArray();
            //传多个图片才加数组
//            jsonArray.add(url);
            //返回成功的json
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("errno",0);
//            jsonObject.put("data",jsonArray);
            jsonObject.put("url",url);
            return jsonObject;

//            UploadDTO uploadDTO = new UploadDTO();
//            uploadDTO.setSuccess(1);
//            uploadDTO.setMessage("上传成功！");
//            uploadDTO.setUrl(url);
//            return uploadDTO;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    @ResponseBody
    @PostMapping("/file/uploadAvator")
    public JSONObject uploadAvator(@RequestParam MultipartFile file) throws Exception {
        if (file == null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state", 0);
            jsonObject.put("des","不能上传空文件");
            jsonObject.put("url", "");
            return jsonObject;
        }

        //临时生成一个File 接收即转为File类型
        File targetFile = FileProvider.multipartFileToFile(file);
        //上传操作 返回图片url
        String url = qCloudProvider.upload(targetFile, Objects.requireNonNull(file.getOriginalFilename()));
        //删除临时的file
        FileProvider.delteTempFile(targetFile) ;

        if (url != null){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state", 1);
            jsonObject.put("des","上传成功");
            jsonObject.put("url", url);
            return jsonObject;
        }else {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("state", 0);
            jsonObject.put("des","上传至文件服务器失败");
            jsonObject.put("url","");
            return jsonObject;
        }
    }

}