package com.toptal.marketservice.aggregates;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketPlayerSellingDTO {

  // TODO add more fields here and query players-service to fetch values
  private String id;
  private String teamId;
  private BigDecimal marketValue;

}
