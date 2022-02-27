package com.toptal.playersservice.aggregates.dtos;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerWithTeamDTO {

  private String id;
  private String teamId;
  private String teamName;
  private String firstName;
  private String lastName;
  private String country;
  private Integer age;
  private String type;
  private BigDecimal marketValue;

}
