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
@Document(collection = "teams")
public class CreatedTeamEvent {

  public static BigDecimal DEFAULT_TEAM_BUDGET = BigDecimal.valueOf(5000000);
  public static int DEFAULT_TEAM_SIZE = 20;

  @Id
  private String id;
  private String ownerId;
  private BigDecimal budget;
  private String name;
  private String country;

}
