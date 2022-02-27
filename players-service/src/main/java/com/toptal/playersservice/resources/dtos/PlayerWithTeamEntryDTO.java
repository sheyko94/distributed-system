package com.toptal.playersservice.resources.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayerWithTeamEntryDTO {

  private String id;
  private String teamName;
  private String firstName;
  private String lastName;
  private String country;
  private Integer age;
  private BigDecimal marketValue;
  private String type;

}
