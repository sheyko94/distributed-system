package com.toptal.playersservice.resources.dtos;

import com.toptal.playersservice.domain.events.PlayerEvent;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerFullDTO {

  private String id;
  private String teamId;
  private String ownerId;
  private String firstName;
  private String lastName;
  private String country;
  private Integer age;
  private BigDecimal marketValue;
  private PlayerEvent.PlayerTypeEnum type;

}