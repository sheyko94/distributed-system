package com.toptal.playersservice.aggregates.dtos;

import com.toptal.playersservice.resources.dtos.PlayerDTO;
import com.toptal.playersservice.resources.dtos.TeamDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamWithPlayersDTO {

  private TeamDTO team;
  private List<PlayerDTO> players;

}
