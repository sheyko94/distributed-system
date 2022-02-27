package com.toptal.playersservice.resources.dtos;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamFullDTO {

  private String id;
  private String ownerId;
  private String name;
  private String country;
  private BigDecimal budget;

}
