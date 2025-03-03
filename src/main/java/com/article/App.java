package com.article;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
// import java.text.DecimalFormat;
import java.util.List;
/**
 * Hello world!
 */
public class App {
//first step : check the args

    public static void main(String[] args) throws UnsupportedEncodingException, IOException {
        if (args.length != 3) {
            System.out.println("用法: java -jar checker.jar <原文路径> <抄袭版路径> <输出路径>");
            return;
        }

        String orig = new String(Files.readAllBytes(Paths.get(args[0])), "UTF-8");
        String copy = new String(Files.readAllBytes(Paths.get(args[1])), "UTF-8");

        double result = calculateSimilarity(orig, copy) ; 

        Files.write(Paths.get(args[2]), String.format("%.2f", result).getBytes());
    }



    public static double calculateSimilarity(String orig, String copy) {
        JiebaSegmenter segmenter = new JiebaSegmenter();
        List<SegToken> origWords = segmenter.process(orig, JiebaSegmenter.SegMode.INDEX);
        List<SegToken> copyWords = segmenter.process(copy, JiebaSegmenter.SegMode.INDEX);

         // 转换为纯词列表
         List<String> origList = origWords.stream().map(t -> t.word).toList();
         List<String> copyList = copyWords.stream().map(t -> t.word).toList();

          // 计算 LCS
        int[][] dp = new int[origList.size()+1][copyList.size()+1];
        for (int i = 1; i <= origList.size(); i++) {
            for (int j = 1; j <= copyList.size(); j++) {
                if (origList.get(i-1).equals(copyList.get(j-1))) {
                    dp[i][j] = dp[i-1][j-1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
                }
            }
        }

        int lcs = dp[origList.size()][copyList.size()];
        return origList.isEmpty() ? 0.0 : Math.round(lcs * 10000.0 / origList.size()) / 100.0;
    }
}

