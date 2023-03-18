package io.search.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class CommonTest {

    @Autowired
    private ObjectMapper objectMapper;

    public void viewJson(Object obj) {
        try {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            System.out.println("========================================================================");
            System.out.println(objectMapper.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
