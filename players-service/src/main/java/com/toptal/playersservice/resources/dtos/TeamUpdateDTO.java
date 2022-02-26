package com.toptal.playersservice.resources.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamUpdateDTO {

  private String name;
  private String country;

}
