package com.toptal.playersservice.resources.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PlayersWithTeamGroupDTO {

  private List<PlayerWithTeamEntryDTO> players;

}
