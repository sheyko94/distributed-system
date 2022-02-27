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
@Document(collection = "teams")
public class TeamEvent {

  public static BigDecimal DEFAULT_TEAM_BUDGET = BigDecimal.valueOf(5000000);
  public static int DEFAULT_TEAM_SIZE = 20;

  public enum TeamEventType {
    TEAM_CREATE, TEAM_UPDATE
  }

  public enum TeamEventSubtype {
    TEAM_UPDATE_INFO, TEAM_UPDATE_BUDGET
  }

  @Id
  private String id;
  private Date date;
  private TeamEventType eventType;
  private TeamEventSubtype eventSubtype;
  private String teamId;
  private String ownerId;
  private BigDecimal budget;
  private String name;
  private String country;

}
