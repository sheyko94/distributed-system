package com.toptal.authservice.resources.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {

  @NotBlank(message = "Username may not be empty")
  private String username;

  @NotBlank(message = "Password may not be empty")
  private String password;

  @NotBlank(message = "Password Verification may not be empty")
  private String passwordVerification;

}
