package com.toptal.authservice.resources.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  private String id;
  private String username;
  private Boolean enabled;

}
