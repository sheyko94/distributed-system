package com.toptal.playersservice.resources.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerUpdateDTO {

  private String firstName;
  private String lastName;
  private String country;

}
