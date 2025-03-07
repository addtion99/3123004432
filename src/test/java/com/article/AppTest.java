package com.article;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Files;

public class AppTest {
    @Test
    void smokeTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));
    
        System.out.println("This should appear in output");
        
        assertTrue(outContent.toString().contains("appear in output"));
        System.setOut(originalOut);
    }

    @Test
    void testInsufficientArguments() throws UnsupportedEncodingException, IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        String[] args = {"arg1", "arg2"};
        App.main(args);

        System.setOut(originalOut);
        assertTrue(outContent.toString().contains("需要输入三个参数"));
    }

    @Test
    void testIdenticalFiles(@TempDir Path tempDir) throws Exception {
        Path origFile = tempDir.resolve("orig.txt");
        Files.writeString(origFile, "同一个世界同一个梦想");
        
        Path copyFile = tempDir.resolve("copy.txt");
        Files.writeString(copyFile, "同一个世界同一个梦想");
        
        Path outputFile = tempDir.resolve("output.txt");

        App.main(new String[]{
            origFile.toString(),
            copyFile.toString(),
            outputFile.toString()
        });

        String result = Files.readString(outputFile).trim();
        assertEquals("100.00", result);
    }

    @Test
    void testPartialSimilarity() {
        String orig = "今天天气很好";
        String copy = "今天天气不错";
        double result = App.calculateSimilarity(orig, copy);
        assertEquals(66.67, result, 0.01);
    }

    @Test
    void testEmptyOriginal(@TempDir Path tempDir) throws Exception {
        Path origFile = tempDir.resolve("empty.txt");
        Files.createFile(origFile);
        
        Path copyFile = tempDir.resolve("copy.txt");
        Files.writeString(copyFile, "有内容的文本");
        
        Path outputFile = tempDir.resolve("output.txt");

        App.main(new String[]{
            origFile.toString(),
            copyFile.toString(),
            outputFile.toString()
        });

        String result = Files.readString(outputFile).trim();
        assertEquals("0.00", result);
    }
}