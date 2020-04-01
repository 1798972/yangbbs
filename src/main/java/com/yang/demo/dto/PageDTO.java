package com.yang.demo.dto;

import lombok.Data;

import java.util.Set;

/**
 * @Author Yiang37
 * @Date 2020/3/13 21:35
 * Description:
 * 分页
 */
@Data
public class PageDTO {
    private boolean hasFirst = true;
    private boolean hasEnd = true;
    private boolean hasPrevious = true;
    private boolean hasNext = true;
    private Set<Integer> pageNumbers;

    private Integer nowPage;
    private Integer endPage;
}