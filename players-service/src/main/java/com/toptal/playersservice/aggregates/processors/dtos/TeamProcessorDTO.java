package com.toptal.playersservice.aggregates.processors.dtos;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamProcessorDTO {

  private String id;
  private String ownerId;
  private String name;
  private String country;
  private BigDecimal budget;

}
