package com.toptal.playersservice.aggregates.processors.dtos;

import com.toptal.playersservice.domain.events.PlayerEvent;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerProcessorDTO {

  private String id;
  private String teamId;
  private String firstName;
  private String lastName;
  private String country;
  private Integer age;
  private BigDecimal marketValue;
  private PlayerEvent.PlayerTypeEnum type;

}
