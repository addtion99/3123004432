package com.article;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
/**
 * Hello world!
 */
public class App {
//first step : check the args

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("用法: java -jar checker.jar <原文路径> <抄袭版路径> <输出路径>");
            return;
        }
    }
}