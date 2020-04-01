package com.yang.demo.provider;

import com.yang.demo.dto.PageDTO;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author Yiang37
 * @Date 2020/3/13 16:23
 * Description:
 * 分页功能
 */

@Component
public class PageProvider {


    /*
    SELECT *  FROM questions  LIMIT 1,5;   显示1之后的5个
            1 0，5
            2 5，5
            3 10，5
            n (n-1)*size 5
            故起始数为(n-1)*size
   */
    //根据页码和大小计算起始数
    public String startNum(String page, String size) {
        Integer tempNum = (Integer.parseInt(page) - 1) * Integer.parseInt(size);
        if (tempNum <= 0){
            tempNum = 0;
        }
        String startNum = tempNum.toString();
        return startNum;
    }

    /*
     * 现在是第一页 不显示首页 上一页 ...
     * 计算好PageDTO 中的五个值
     *
     * 当前页码
     * size
     * 总条数
     * */
    //根据传入的page和size计算pageDTO
    //当前页、每页条数、总数、范围
    public PageDTO gainPageDTO(String tempNowPage, String tempSize, String tempCounts, Integer range) {
        // << < 1 2 3 4 5 6 7 > >>
        PageDTO pageDTO = new PageDTO();

        Integer nowPage = Integer.parseInt(tempNowPage);
        Integer size = Integer.parseInt(tempSize);
        Integer counts = Integer.parseInt(tempCounts);

        Integer totalPage;
        Set<Integer> whatPages = new TreeSet<>();

        // eg:13条 每页五个


        //矫正size 错误设置为默认的5 正则
        String regex = "^[0-9]*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(size.toString());
        boolean isMatch = m.matches();
        if (!isMatch) {
            size = 5;
        }

        //1.计算总页数
        if (counts % size == 0) {
            totalPage = counts / size;
        } else {
            totalPage = (counts / size) + 1;
        }

        //2.规定当前页和大小的范围 因为计算total要size 所以size先矫正
        if (nowPage <= 0) {
            nowPage = 1;
        }
        if (nowPage > totalPage) {
            nowPage = totalPage;
        }


        //3.根据当前页 判断包含哪些页
        Integer sizeRight = totalPage - nowPage;
        Integer sizeLeft = nowPage - 1;

        //对左边的处理
        if (nowPage != 1){
            if (sizeLeft >= range) {
                //最多只显示左边的range个
                for (int i = 1; i <= range; i++) {
                    whatPages.add(nowPage - i);
                }
            } else {
                //显示sizeLeft个
                for (int i = 1; i <= sizeLeft; i++) {
                    whatPages.add(nowPage - i);
                }
            }
        }

        //对右边的处理
        if (!nowPage.equals(totalPage)){
            if (sizeRight >= range) {
                for (int j = 1; j <= range; j++) {
                    whatPages.add(nowPage + j);
                }
            } else {
                for (int j = 1; j <= sizeRight; j++) {
                    whatPages.add(nowPage + j);
                }
            }
        }
        whatPages.add(nowPage);

        //4.上页下页 第一页最后一页的处理
        if (nowPage == 1) {
            pageDTO.setHasFirst(false);
            pageDTO.setHasPrevious(false);
        }
        if (nowPage.equals(totalPage)) {
            pageDTO.setHasEnd(false);
            pageDTO.setHasNext(false);
        }


        pageDTO.setPageNumbers(whatPages);
        pageDTO.setNowPage(nowPage);
        pageDTO.setEndPage(totalPage);
        return pageDTO;
    }


    //矫正大小
    public String checkSizeNow(String tempSize){
        String checkedSize = "5";

        //验证是否为数字格式
        String regex = "^[0-9]*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(tempSize);
        boolean isMatch = m.matches();
        if (!isMatch) {
            return checkedSize;
        }
        //即使满足 也要处理为0的情况
        if ("0".equals(tempSize)){
            return  checkedSize;
        }

        return tempSize;
    }

    //矫正页码数
    public String checkPageNow(String tempNowPage,String tempCounts,String tempSize){

        //不满足数字格式 页数返回1
        String regex = "^[0-9]*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(tempNowPage);
        boolean isMatch = m.matches();
        if (!isMatch) {
            return "1";
        }

        //为数字格式则做小于第一页和大于最后一页的处理
        Integer nowPage = Integer.parseInt(tempNowPage);
        Integer size = Integer.parseInt(tempSize);
        Integer counts = Integer.parseInt(tempCounts);

        Integer totalPage;

        //计算总页数
        if (counts % size == 0) {
            totalPage = counts / size;
        } else {
            totalPage = (counts / size) + 1;
        }

        //规定当前页的范围
        if (nowPage <= 0) {
            nowPage = 1;
        }

        if (nowPage > totalPage) {
            nowPage = totalPage;
        }

        return nowPage.toString();
    }

}