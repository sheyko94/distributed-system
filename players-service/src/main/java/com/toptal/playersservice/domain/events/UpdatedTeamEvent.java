package com.toptal.playersservice.domain.events;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "teams")
public class UpdatedTeamEvent {

  @Id
  private String id;
  private String teamId;
  private String name;
  private String country;

}
