package com.toptal.playersservice.domain.events;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "players")
public class UpdatedPlayerEvent {

  @Id
  private String id;
  private String playerId;
  private String firstName;
  private String lastName;
  private String country;

}
