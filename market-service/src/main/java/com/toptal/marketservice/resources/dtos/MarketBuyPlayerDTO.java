package com.toptal.marketservice.resources.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MarketBuyPlayerDTO {

  @NonNull
  private String teamId;

}
