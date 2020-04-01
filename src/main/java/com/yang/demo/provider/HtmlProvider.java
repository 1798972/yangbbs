package com.yang.demo.provider;

import org.springframework.stereotype.Component;

/**
 * @Author Yiang37
 * @Date 2020/3/15 17:07
 * Description:
 * 取出html格式中的文本 首页不需要展示
 */
@Component
public class HtmlProvider {

        private String delHtmlTags(String htmlStr) {
            //定义script的正则表达式，去除js可以防止注入
            String scriptRegex = "<script[^>]*?>[\\s\\S]*?<\\/script>";
            //定义style的正则表达式，去除style样式，防止css代码过多时只截取到css样式代码
            String styleRegex = "<style[^>]*?>[\\s\\S]*?<\\/style>";
            //定义HTML标签的正则表达式，去除标签，只提取文字内容
            String htmlRegex = "<[^>]+>";
            //定义空格,回车,换行符,制表符
            String spaceRegex = "\\s*|\t|\r|\n";

            // 过滤script标签
            htmlStr = htmlStr.replaceAll(scriptRegex, "");
            // 过滤style标签
            htmlStr = htmlStr.replaceAll(styleRegex, "");
            // 过滤html标签
            htmlStr = htmlStr.replaceAll(htmlRegex, "");
            // 过滤空格等
            htmlStr = htmlStr.replaceAll(spaceRegex, "");
            return htmlStr.trim(); // 返回文本字符串
        }


        public String getTextFromHtml(String htmlStr) {
            //去除html标签
            htmlStr = delHtmlTags(htmlStr);
            //去除空格" "
            htmlStr = htmlStr.replaceAll(" ", "");
            return htmlStr;
        }
    }
