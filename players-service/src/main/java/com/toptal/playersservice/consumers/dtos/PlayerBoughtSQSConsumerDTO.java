package com.toptal.playersservice.consumers.dtos;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerBoughtSQSConsumerDTO {

  private String playerId;
  private String sellingTeamId;
  private String buyingTeamId;
  private BigDecimal value;

}
