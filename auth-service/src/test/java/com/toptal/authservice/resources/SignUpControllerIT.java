package com.toptal.authservice.resources;

import com.toptal.authservice.resources.dtos.SignUpDTO;
import com.toptal.authservice.resources.dtos.UserDTO;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SqlGroup(@Sql(scripts = SignUpControllerIT.SIGNUP_DB_PATH, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD))
public class SignUpControllerIT extends BaseControllerIT {

  public static final String SIGNUP_DB_PATH = "/h2-files/data-h2.sql";

  private final String USERNAME = "User Test 1";
  private final String PASSWORD = "test123";

  @Test
  public void signUpOkTest() throws Exception {

    final MvcResult mvcResult = mockMvc
      .perform(post("/v1/sign-up")
        .contentType(MediaType.APPLICATION_JSON)
        .content(builder.toJson(getSignUpDto())))
      .andDo(log())
      .andExpect(status().isOk())
      .andReturn();

    final UserDTO result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), UserDTO.class);

    assertNotNull(result.getId());
    assertEquals(USERNAME, result.getUsername());
    assertTrue(result.getEnabled());
  }

  private SignUpDTO getSignUpDto() {
    return SignUpDTO.builder()
      .username(USERNAME)
      .password(PASSWORD)
      .passwordVerification(PASSWORD)
      .build();
  }

}
