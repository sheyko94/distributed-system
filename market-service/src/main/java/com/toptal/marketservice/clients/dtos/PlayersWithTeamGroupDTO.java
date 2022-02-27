package com.toptal.marketservice.clients.dtos;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayersWithTeamGroupDTO {

  private List<PlayerWithTeamEntryDTO> players;

}
