package com.toptal.playersservice.domain.events;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "players")
public class CreatedPlayerEvent {

  public static BigDecimal DEFAULT_PLAYER_MARKET_VALUE = BigDecimal.valueOf(1000000);
  public static int MINIMUM_PLAYER_AGE = 18;
  public static int MAXIMUM_PLAYER_AGE = 40;

  public enum PlayerTypeEnum {
    GOALKEEPER, DEFENDER, MIDFIELDER, ATTACKER
  }

  @Id
  private String id;
  private String playerId;
  private String teamId;
  private String firstName;
  private String lastName;
  private String country;
  private Integer age;
  private BigDecimal marketValue;
  private PlayerTypeEnum type;

}
