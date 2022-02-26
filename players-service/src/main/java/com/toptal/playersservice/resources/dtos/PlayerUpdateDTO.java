package com.toptal.playersservice.resources.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerUpdateDTO {

  private String firstName;
  private String lastName;
  private String country;

}
