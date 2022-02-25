package com.toptal.authservice.services.impl;

import com.toptal.authservice.config.CustomPasswordEncoder;
import com.toptal.authservice.domain.models.User;
import com.toptal.authservice.domain.repositories.UserRepository;
import com.toptal.authservice.resources.dtos.SignUpDTO;
import com.toptal.authservice.services.UserService;
import com.toptal.authservice.services.validators.UserServiceValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

  private final CustomPasswordEncoder customPasswordEncoder;
  private final UserRepository userRepository;
  private final UserServiceValidator userServiceValidator;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return Optional.ofNullable(userRepository.findByUsernameIgnoreCase(username))
      .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with username %s", username)));
  }

  @Override
  @Transactional(rollbackOn = Exception.class)
  public User signUp(SignUpDTO signUpDto) throws ValidationException {

    userServiceValidator.validateSignUp(signUpDto);

    User user = User.builder()
      .username(signUpDto.getUsername())
      .passwordHash(customPasswordEncoder.encode(signUpDto.getPassword()))
      .build();

    user = userRepository.save(user);

    // TODO this should send a domain event

    return user;
  }

}
