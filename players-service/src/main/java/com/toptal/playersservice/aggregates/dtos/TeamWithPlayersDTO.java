package com.toptal.playersservice.aggregates.dtos;

import com.toptal.playersservice.resources.dtos.PlayerFullDTO;
import com.toptal.playersservice.resources.dtos.TeamFullDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamWithPlayersDTO {

  private TeamFullDTO team;
  private List<PlayerFullDTO> players;

}
