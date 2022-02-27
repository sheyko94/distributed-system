package com.toptal.playersservice.aggregates;

import com.toptal.playersservice.aggregates.dtos.TeamWithPlayersDTO;
import com.toptal.playersservice.domain.events.PlayerEvent;
import com.toptal.playersservice.domain.events.TeamEvent;
import com.toptal.playersservice.domain.repositories.PlayerEventRepository;
import com.toptal.playersservice.domain.repositories.TeamEventRepository;
import com.toptal.playersservice.resources.dtos.PlayerFullDTO;
import com.toptal.playersservice.resources.dtos.TeamFullDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamWithPlayersAggregate {

  private final TeamEventRepository teamEventRepository;
  private final PlayerEventRepository playerEventRepository;
  private final TeamFullAggregate teamFullAggregate;
  private final PlayerFullAggregate playerFullAggregate;

  public List<TeamWithPlayersDTO> fetchTeamWithPlayersByOwnerID(final String ownerId) {

    List<TeamWithPlayersDTO> result = new ArrayList<>();

    final List<String> teamsIds = getTeamsIds(teamEventRepository.findByOwnerId(ownerId));
    final List<TeamFullDTO> teamFullDTOs = teamFullAggregate.fetchByTeamIds(teamsIds);
    final List<PlayerFullDTO> playerFullDTOs = playerFullAggregate.fetchByPlayerIds(getPlayersIds(playerEventRepository.findByTeamIdIn(teamsIds)));

    for (TeamFullDTO teamFullDTO : teamFullDTOs) {

      final List<PlayerFullDTO> playersForCurrentTeam = playerFullDTOs
        .stream()
        .filter(player -> player.getTeamId().equals(teamFullDTO.getId()))
        .collect(Collectors.toList());

      final BigDecimal totalValue = playersForCurrentTeam.stream()
        .map(PlayerFullDTO::getMarketValue)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

      teamFullDTO.setValue(totalValue);

      result.add(TeamWithPlayersDTO.builder()
        .team(teamFullDTO)
        .players(playersForCurrentTeam)
        .build());
    }

    return result;
  }

  private List<String> getTeamsIds(final List<TeamEvent> teams) {
    return teams.stream().map(TeamEvent::getTeamId).distinct().collect(Collectors.toList());
  }

  private List<String> getPlayersIds(final List<PlayerEvent> players) {
    return players.stream().map(PlayerEvent::getPlayerId).distinct().collect(Collectors.toList());
  }

}
