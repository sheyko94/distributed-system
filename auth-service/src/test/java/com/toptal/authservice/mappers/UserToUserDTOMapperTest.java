package com.toptal.authservice.mappers;

import com.toptal.authservice.domain.models.User;
import com.toptal.authservice.resources.dtos.UserDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = { ModelMapper.class, UserToUserDTOMapper.class })
public class UserToUserDTOMapperTest {

  private final String USER_ID = RandomStringUtils.randomAlphabetic(5);
  private final String USERNAME = RandomStringUtils.randomAlphabetic(5);
  private final String PASSWORD_HASH = RandomStringUtils.randomAlphabetic(5);

  @Autowired
  private UserToUserDTOMapper userToUserDTOMapper;

  @Test
  public void mapTest() {

    User user = User.builder()
      .id(USER_ID)
      .passwordHash(PASSWORD_HASH)
      .username(USERNAME)
      .build();

    UserDTO userDTO = userToUserDTOMapper.map(user);

    assertEquals(USER_ID, userDTO.getId());
    assertEquals(USERNAME, userDTO.getUsername());
    assertEquals(Boolean.TRUE, userDTO.getEnabled());
  }

}
