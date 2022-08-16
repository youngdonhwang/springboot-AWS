package com.sb.beFriendWith.web;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = HelloSpringBootController.class)
public class HelloSpringBootControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void hello문자열을_리턴한다() throws Exception{
        String hello = "hello";
        mockMvc.perform(get("/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string(hello));
    }

    @Test
    public void DTO클래스_인스턴스를_리턴한다() throws Exception {
        String name = "ming";
        int amount = 30;

        mockMvc.perform(get("/hello/dto").param("name", name).param("amount", String.valueOf(amount)))
                .andExpect(status().isOk())
                .andExpect( jsonPath("$.name", Matchers.is(name)))
                .andExpect( jsonPath("$.amount", Matchers.is(amount)));
    }
}
