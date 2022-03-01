package com.toptal.authservice.resources.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toptal.authservice.domain.models.User;
import com.toptal.authservice.mappers.UserToUserDTOMapper;
import com.toptal.authservice.resources.dtos.SignUpDTO;
import com.toptal.authservice.resources.dtos.UserDTO;
import com.toptal.authservice.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.xml.bind.ValidationException;
import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping(SignUpController.SIGN_UP_CONTROLLER_PREFIX)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SignUpController {

  public final static String SIGN_UP_CONTROLLER_PREFIX = "/v1/sign-up";

  private final UserService userService;
  private final UserToUserDTOMapper userToUserDTOMapper;

  @PostMapping
  public ResponseEntity<UserDTO> signUp(@RequestBody @Valid @NonNull SignUpDTO signUpDTO) throws ValidationException, UnsupportedEncodingException, JsonProcessingException {
    User newUser = userService.signUp(signUpDTO);
    return ResponseEntity.ok(userToUserDTOMapper.map(newUser));
  }

}
