package com.taraskin.cache.controller;

import com.taraskin.cache.entity.Record;
import com.taraskin.cache.repository.RecordRepo;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecordRepo recordRepo;


    @Test
    public void getRecordByKeyTest() throws Exception {

        recordRepo.save(new Record(1l, "record 1"));

        mockMvc.perform(get("/{key}", 1)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("key", is(1)))
                .andExpect(jsonPath("value", is("record 1")));

        mockMvc.perform(get("/{key}", 1)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("key", is(1)))
                .andExpect(jsonPath("value", is("record 1")));

        mockMvc.perform(get("/{key}", 666)).andDo(print())
                .andExpect(status().isNotFound());

        recordRepo.deleteById(1l);
    }


    @Test
    public void createOrUpdateRecordTest() throws Exception {

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"key\": \"50\", \"value\": \"record 50\"}"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());


        mockMvc.perform(get("/{key}", 50)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("key", is(50)))
                .andExpect(jsonPath("value", is("record 50")));


        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"key\": \"50\", \"value\": \"record 50 updated\"}"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());


        mockMvc.perform(get("/{key}", 50)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("key", is(50)))
                .andExpect(jsonPath("value", is("record 50 updated")));

        recordRepo.deleteById(50l);
    }

}
