package com.toptal.playersservice.resources.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamFullDTO {

  private String id;
  private String ownerId;
  private String name;
  private String country;
  private BigDecimal budget;
  private BigDecimal value;

}
