package com.toptal.authservice.services.validators;

import com.toptal.authservice.domain.models.User;
import com.toptal.authservice.domain.repositories.UserRepository;
import com.toptal.authservice.resources.dtos.SignUpDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceValidator {

  private final UserRepository userRepository;

  public void validateSignUp(SignUpDTO signUpDto) throws ValidationException {

    final String username = signUpDto.getUsername();

    Optional<User> userOpt = Optional.ofNullable(userRepository.findByUsernameIgnoreCase(username));

    if (userOpt.isPresent()) {
      throw new DataIntegrityViolationException(String.format("The username %s is already being used", username));
    }

    passwordMatch(signUpDto.getPassword(), signUpDto.getPasswordVerification());
  }

  private void passwordMatch(String password, String verificationPassword) throws ValidationException {
    if (!password.equals(verificationPassword)) {
      throw new ValidationException("Password and Password Verification does not match");
    }
  }

}
