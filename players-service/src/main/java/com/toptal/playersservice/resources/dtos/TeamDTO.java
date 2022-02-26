package com.toptal.playersservice.resources.dtos;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDTO {

  private String id;
  private BigDecimal budget;
  private String name;
  private String country;

}
