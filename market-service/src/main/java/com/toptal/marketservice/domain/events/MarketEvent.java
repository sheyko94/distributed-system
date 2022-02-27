package com.toptal.marketservice.domain.events;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "market")
public class MarketEvent {

  public enum MarketEventType {
    PLAYER_SALE, PLAYER_BOUGHT
  }

  @Id
  private String id;
  private Date date;
  private MarketEvent.MarketEventType eventType;
  private String teamId;
  private String playerId;
  private BigDecimal value;

}
