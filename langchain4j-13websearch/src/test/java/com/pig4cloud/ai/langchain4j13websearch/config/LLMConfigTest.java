package com.pig4cloud.ai.langchain4j13websearch.config;

import com.pig4cloud.ai.langchain4j13websearch.service.ChatAssistant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest
class LLMConfigTest {

    @Autowired
    private ChatAssistant chatAssistant;

    @Test
    void chatAssistant() {
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 定义日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // 将日期格式化为字符串
        String formattedDate = currentDate.format(formatter);

        String message = String.format("今天%s上证指数是多少？", formattedDate);
        String chat = chatAssistant.chat(message);

        System.out.println(chat);
    }
}
