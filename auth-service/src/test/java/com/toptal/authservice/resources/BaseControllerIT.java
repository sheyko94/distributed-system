package com.toptal.authservice.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import javax.annotation.PostConstruct;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public abstract class BaseControllerIT {

  @Autowired
  protected MockMvc mockMvc;

  protected Gson builder;
  protected ObjectMapper objectMapper;

  @PostConstruct
  public void setUp() {
    objectMapper = new ObjectMapper();
    builder = new GsonBuilder().create();
  }

}
