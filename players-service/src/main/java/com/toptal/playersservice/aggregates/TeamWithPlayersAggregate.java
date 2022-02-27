package com.toptal.playersservice.aggregates;

import com.toptal.playersservice.aggregates.dtos.TeamWithPlayersDTO;
import com.toptal.playersservice.domain.events.PlayerEvent;
import com.toptal.playersservice.domain.events.TeamEvent;
import com.toptal.playersservice.domain.repositories.PlayerEventRepository;
import com.toptal.playersservice.domain.repositories.TeamEventRepository;
import com.toptal.playersservice.resources.dtos.PlayerDTO;
import com.toptal.playersservice.resources.dtos.TeamDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamWithPlayersAggregate {

  private final TeamEventRepository teamEventRepository;
  private final PlayerEventRepository playerEventRepository;

  // TODO refactor
  public List<TeamWithPlayersDTO> fetchTeamWithPlayersByOwnerID(final String ownerId) {

    List<TeamWithPlayersDTO> result = new ArrayList<>();

    final List<TeamEvent> foundCreateTeamEvents = teamEventRepository.findByOwnerIdAndEventType(ownerId, TeamEvent.TeamEventType.TEAM_CREATE);
    final List<String> teamIds = getTeamsIds(foundCreateTeamEvents);
    final List<TeamEvent> foundUpdateTeamEvents = teamEventRepository.findByTeamIdInAndEventType(teamIds, TeamEvent.TeamEventType.TEAM_UPDATE);

    final List<PlayerEvent> foundCreatePlayersEvents = playerEventRepository.findByTeamIdInAndEventType(teamIds, PlayerEvent.PlayerEventType.PLAYER_CREATE);
    final List<PlayerEvent> foundPlayersUpdateEvents = playerEventRepository.findByPlayerIdInAndEventType(getPlayersIds(foundCreatePlayersEvents), PlayerEvent.PlayerEventType.PLAYER_UPDATE);

    for (TeamEvent teamCreateEvent : foundCreateTeamEvents) {

      TeamDTO teamDTO = TeamDTO.builder()
        .budget(teamCreateEvent.getBudget())
        .country(teamCreateEvent.getCountry())
        .name(teamCreateEvent.getName())
        .id(teamCreateEvent.getTeamId())
        .build();

      final List<TeamEvent> updatesForCurrentTeam = foundUpdateTeamEvents
        .stream()
        .filter(teamUpdate -> teamUpdate.getTeamId().equals(teamCreateEvent.getTeamId()))
        .sorted(Comparator.comparing(TeamEvent::getDate))
        .collect(Collectors.toList());

      for (TeamEvent update : updatesForCurrentTeam) {

        if (TeamEvent.TeamEventSubtype.TEAM_UPDATE_INFO.equals(update.getEventSubtype())) {
          teamDTO.setName(update.getName());
          teamDTO.setCountry(update.getCountry());
        } else if (TeamEvent.TeamEventSubtype.TEAM_UPDATE_BUDGET.equals(update.getEventSubtype())) {
          teamDTO.setBudget(update.getBudget());
        }
      }

      final List<PlayerEvent> playersForCurrentTeam = foundCreatePlayersEvents
        .stream()
        .filter(player -> player.getTeamId().equals(teamCreateEvent.getTeamId()))
        .collect(Collectors.toList());

      List<PlayerDTO> playerDTOs = new ArrayList<>();

      BigDecimal teamValue = BigDecimal.ZERO;

      for (PlayerEvent player : playersForCurrentTeam) {

        final String currentPlayerId = player.getPlayerId();
        final BigDecimal playerValue = player.getMarketValue();

        PlayerDTO playerDTO = PlayerDTO.builder()
          .id(currentPlayerId)
          .age(player.getAge())
          .country(player.getCountry())
          .firstName(player.getFirstName())
          .lastName(player.getLastName())
          .marketValue(playerValue)
          .type(player.getType())
          .build();

        final List<PlayerEvent> updatesForCurrentPLayer = foundPlayersUpdateEvents
          .stream()
          .filter(playerUpdate -> playerUpdate.getPlayerId().equals(currentPlayerId))
          .sorted(Comparator.comparing(PlayerEvent::getDate))
          .collect(Collectors.toList());

        for (PlayerEvent update : updatesForCurrentPLayer) {

          if (PlayerEvent.PlayerEventSubtype.PLAYER_UPDATE_INFO.equals(update.getEventSubtype())) {
            playerDTO.setFirstName(update.getFirstName());
            playerDTO.setLastName(update.getLastName());
            playerDTO.setCountry(update.getCountry());
          } else if (PlayerEvent.PlayerEventSubtype.PLAYER_TRANSFER.equals(update.getEventSubtype())) {
            playerDTO.setMarketValue(update.getMarketValue());
          }
        }

        teamValue = teamValue.add(playerValue);
        playerDTOs.add(playerDTO);
      }

      teamDTO.setValue(teamValue);

      result.add(TeamWithPlayersDTO.builder()
        .team(teamDTO)
        .players(playerDTOs)
        .build());
    }

    return result;
  }

  private List<String> getTeamsIds(final List<TeamEvent> teams) {
    return teams.stream().map(TeamEvent::getTeamId).collect(Collectors.toList());
  }

  private List<String> getPlayersIds(final List<PlayerEvent> players) {
    return players.stream().map(PlayerEvent::getPlayerId).collect(Collectors.toList());
  }

}
