package com.toptal.playersservice.domain.events;

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
@Document(collection = "players")
public class PlayerEvent {

  public static BigDecimal DEFAULT_PLAYER_MARKET_VALUE = BigDecimal.valueOf(1000000);
  public static int MINIMUM_PLAYER_AGE = 18;
  public static int MAXIMUM_PLAYER_AGE = 40;
  public static int MINIMUM_PLAYER_MARKET_VALUE_PERCENT_INCREMENT = 10;
  public static int MAXIMUM_PLAYER_MARKET_VALUE_PERCENT_INCREMENT = 100;

  public enum PlayerEventType {
    PLAYER_CREATE, PLAYER_UPDATE
  }

  public enum PlayerEventSubtype {
    PLAYER_UPDATE_INFO, PLAYER_TRANSFER
  }

  public enum PlayerTypeEnum {
    GOALKEEPER, DEFENDER, MIDFIELDER, ATTACKER
  }

  @Id
  private String id;
  private Date date;
  private PlayerEvent.PlayerEventType eventType;
  private PlayerEvent.PlayerEventSubtype eventSubtype;
  private String playerId;
  private String teamId;
  private String firstName;
  private String lastName;
  private String country;
  private Integer age;
  private BigDecimal marketValue;
  private PlayerTypeEnum type;

}
