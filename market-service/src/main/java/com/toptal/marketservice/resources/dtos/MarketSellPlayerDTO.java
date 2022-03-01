package com.toptal.marketservice.resources.dtos;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketSellPlayerDTO {

  @NonNull
  private BigDecimal value;

}
