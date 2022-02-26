package com.toptal.playersservice.aggregates;

import com.toptal.playersservice.aggregates.dtos.TeamWithPlayersDTO;
import com.toptal.playersservice.domain.events.CreatedPlayerEvent;
import com.toptal.playersservice.domain.events.CreatedTeamEvent;
import com.toptal.playersservice.domain.events.UpdatedPlayerEvent;
import com.toptal.playersservice.domain.events.UpdatedTeamEvent;
import com.toptal.playersservice.domain.repositories.CreatedPlayerEventRepository;
import com.toptal.playersservice.domain.repositories.CreatedTeamEventRepository;
import com.toptal.playersservice.domain.repositories.UpdatedPlayerEventRepository;
import com.toptal.playersservice.domain.repositories.UpdatedTeamEventRepository;
import com.toptal.playersservice.resources.dtos.PlayerDTO;
import com.toptal.playersservice.resources.dtos.TeamDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamWithPlayersAggregate {

  private final CreatedTeamEventRepository createdTeamEventRepository;
  private final UpdatedTeamEventRepository updatedTeamEventRepository;
  private final CreatedPlayerEventRepository createdPlayerEventRepository;
  private final UpdatedPlayerEventRepository updatedPlayerEventRepository;

  public List<TeamWithPlayersDTO> fetchTeamWithPlayersByOwnerID(final String ownerId) {

    List<TeamWithPlayersDTO> result = new ArrayList<>();

    final List<CreatedTeamEvent> foundTeams = createdTeamEventRepository.findByOwnerId(ownerId);
    final List<UpdatedTeamEvent> foundTeamsUpdates = updatedTeamEventRepository.findByTeamIdIn(getTeamsIds(foundTeams));
    final List<CreatedPlayerEvent> foundPlayers = createdPlayerEventRepository.findByTeamIdIn(getTeamsIds(foundTeams));
    final List<UpdatedPlayerEvent> foundPlayersUpdates = updatedPlayerEventRepository.findByPlayerIdIn(getPlayerIds(foundPlayers));

    for (CreatedTeamEvent team : foundTeams) {

      final TeamDTO teamDTO = TeamDTO.builder()
        .budget(team.getBudget())
        .country(team.getCountry())
        .name(team.getName())
        .id(team.getTeamId())
        .build();

      final List<UpdatedTeamEvent> updatedForCurrentTeam = foundTeamsUpdates
        .stream()
        .filter(teamUpdate -> teamUpdate.getTeamId().equals(team.getTeamId()))
        // .sorted() // TODO sort by date desc
        .collect(Collectors.toList());

      // TODO think about fetching the last event or loop
      for (UpdatedTeamEvent update : updatedForCurrentTeam) {
        teamDTO.setName(update.getName());
        teamDTO.setCountry(update.getCountry());
      }

      final List<CreatedPlayerEvent> playersForCurrentTeam = foundPlayers
        .stream()
        .filter(player -> player.getTeamId().equals(team.getTeamId()))
        .collect(Collectors.toList());

      List<PlayerDTO> playerDTOs = new ArrayList<>();

      BigDecimal teamValue = BigDecimal.ZERO;

      for (CreatedPlayerEvent player : playersForCurrentTeam) {

        final String currentPlayerId = player.getPlayerId();
        final BigDecimal playerValue = player.getMarketValue();

        final PlayerDTO playerDTO = PlayerDTO.builder()
          .id(currentPlayerId)
          .age(player.getAge())
          .country(player.getCountry())
          .firstName(player.getFirstName())
          .lastName(player.getLastName())
          .marketValue(playerValue)
          .type(player.getType())
          .build();

        final List<UpdatedPlayerEvent> updatedForCurrentPLayer = foundPlayersUpdates
          .stream()
          .filter(playerUpdate -> playerUpdate.getPlayerId().equals(currentPlayerId))
          // .sorted() // TODO sort by date desc
          .collect(Collectors.toList());

        // TODO think about fetching the last event or loop
        for (UpdatedPlayerEvent update : updatedForCurrentPLayer) {
          playerDTO.setFirstName(update.getFirstName());
          playerDTO.setLastName(update.getLastName());
          playerDTO.setCountry(update.getCountry());
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

  private List<String> getTeamsIds(final List<CreatedTeamEvent> teams) {
    return teams.stream().map(CreatedTeamEvent::getTeamId).collect(Collectors.toList());
  }

  private List<String> getPlayerIds(final List<CreatedPlayerEvent> players) {
    return players.stream().map(CreatedPlayerEvent::getPlayerId).collect(Collectors.toList());
  }

}
