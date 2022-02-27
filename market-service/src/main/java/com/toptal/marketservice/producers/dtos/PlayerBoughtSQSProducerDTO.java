package com.toptal.marketservice.producers.dtos;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerBoughtSQSProducerDTO {

  private String playerId;
  private String sellingTeamId;
  private String buyingTeamId;
  private BigDecimal value;

}
