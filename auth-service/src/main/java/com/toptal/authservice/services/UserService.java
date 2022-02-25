package com.toptal.authservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toptal.authservice.domain.models.User;
import com.toptal.authservice.resources.dtos.SignUpDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.xml.bind.ValidationException;
import java.io.UnsupportedEncodingException;

public interface UserService extends UserDetailsService {

  User signUp(SignUpDTO signUpDto) throws ValidationException, UnsupportedEncodingException, JsonProcessingException;

}
