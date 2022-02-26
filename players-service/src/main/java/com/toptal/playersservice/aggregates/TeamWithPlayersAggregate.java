package com.toptal.playersservice.aggregates;

import com.toptal.playersservice.aggregates.dtos.TeamWithPlayersDTO;
import com.toptal.playersservice.domain.events.CreatedPlayerEvent;
import com.toptal.playersservice.domain.events.CreatedTeamEvent;
import com.toptal.playersservice.domain.repositories.CreatedPlayerEventRepository;
import com.toptal.playersservice.domain.repositories.CreatedTeamEventRepository;
import com.toptal.playersservice.resources.dtos.PlayerDTO;
import com.toptal.playersservice.resources.dtos.TeamDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamWithPlayersAggregate {

  private final CreatedTeamEventRepository createdTeamEventRepository;
  private final CreatedPlayerEventRepository createdPlayerEventRepository;

  public List<TeamWithPlayersDTO> fetchTeamWithPlayersByOwnerID(final String ownerId) {

    List<TeamWithPlayersDTO> result = new ArrayList<>();

    final List<CreatedTeamEvent> foundTeams = createdTeamEventRepository.findByOwnerId(ownerId);
    final List<CreatedPlayerEvent> foundPlayers = createdPlayerEventRepository.findByTeamIdIn(getTeamsIds(foundTeams));

    for (CreatedTeamEvent team : foundTeams) {

      final TeamDTO teamDTO = TeamDTO.builder()
        .budget(team.getBudget())
        .country(team.getCountry())
        .name(team.getName())
        .id(team.getId())
        .build();

      final List<CreatedPlayerEvent> playersForCurrentTeam = foundPlayers
        .stream()
        .filter(player -> player.getTeamId().equals(team.getId()))
        .collect(Collectors.toList());

      List<PlayerDTO> playerDTOs = new ArrayList<>();

      for (CreatedPlayerEvent player : playersForCurrentTeam) {

        final PlayerDTO playerDTO = PlayerDTO.builder()
          .id(player.getId())
          .age(player.getAge())
          .country(player.getCountry())
          .firstName(player.getFirstName())
          .lastName(player.getLastName())
          .marketValue(player.getMarketValue())
          .type(player.getType())
          .build();

        playerDTOs.add(playerDTO);
      }

      result.add(TeamWithPlayersDTO.builder()
        .team(teamDTO)
        .players(playerDTOs)
        .build());
    }

    return result;
  }

  private List<String> getTeamsIds(final List<CreatedTeamEvent> teams) {
    return teams.stream().map(CreatedTeamEvent::getId).collect(Collectors.toList());
  }

}
