package com.yang.demo.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author Yiang37
 * @Date 2020/3/25 14:44
 * Description:
 * 个人中心-我的通知
 */
@Data
public class MyNoticeAndPageDTO {
    List<MyNoticeDTO> myNoticeDTOList;
    private PageDTO pageDTO;
}