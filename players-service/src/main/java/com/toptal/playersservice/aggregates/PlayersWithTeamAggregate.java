package com.toptal.playersservice.aggregates;

import com.toptal.playersservice.aggregates.processors.PlayerProcessor;
import com.toptal.playersservice.aggregates.processors.TeamProcessor;
import com.toptal.playersservice.aggregates.processors.dtos.PlayerProcessorDTO;
import com.toptal.playersservice.aggregates.processors.dtos.TeamProcessorDTO;
import com.toptal.playersservice.domain.events.PlayerEvent;
import com.toptal.playersservice.domain.events.TeamEvent;
import com.toptal.playersservice.domain.repositories.PlayerEventRepository;
import com.toptal.playersservice.domain.repositories.TeamEventRepository;
import com.toptal.playersservice.resources.dtos.PlayerWithTeamEntryDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayersWithTeamAggregate {

  private final PlayerEventRepository playerEventRepository;
  private final TeamEventRepository teamEventRepository;
  private final PlayerProcessor playerProcessor;
  private final TeamProcessor teamProcessor;

  public List<PlayerWithTeamEntryDTO> fetchByPlayerIds(final List<String> playerIds) {

    log.info("Calling PlayersWithTeamAggregate.fetchByPlayerIds for player IDs {}", playerIds);

    final List<PlayerEvent> playerEvents = playerEventRepository.findByPlayerIdIn(playerIds);
    final List<PlayerProcessorDTO> playersFullDTOs = playerProcessor.process(playerEvents)
      .stream()
      .map(playerProcessorDTO -> PlayerProcessorDTO.builder()
        .id(playerProcessorDTO.getId())
        .teamId(playerProcessorDTO.getTeamId())
        .firstName(playerProcessorDTO.getFirstName())
        .lastName(playerProcessorDTO.getLastName())
        .country(playerProcessorDTO.getCountry())
        .age(playerProcessorDTO.getAge())
        .type(playerProcessorDTO.getType())
        .marketValue(playerProcessorDTO.getMarketValue())
        .build()
      ).collect(Collectors.toList());

    final List<TeamEvent> teamEvents = teamEventRepository.findByTeamIdIn(playersFullDTOs
      .stream()
      .map(PlayerProcessorDTO::getTeamId)
      .collect(Collectors.toList()));
    final List<TeamProcessorDTO> teamsFullDTOs = teamProcessor.process(teamEvents)
      .stream()
      .map(teamProcessorDTO -> TeamProcessorDTO.builder()
        .id(teamProcessorDTO.getId())
        .name(teamProcessorDTO.getName())
        .country(teamProcessorDTO.getCountry())
        .build()
      ).collect(Collectors.toList());

    List<PlayerWithTeamEntryDTO> result = new ArrayList<>();

    for (PlayerProcessorDTO player : playersFullDTOs) {

      final TeamProcessorDTO team = teamsFullDTOs
        .stream()
        .filter(teamProcessorDTO -> teamProcessorDTO.getId().equals(player.getTeamId()))
        .findFirst()
        .get();

      result.add(PlayerWithTeamEntryDTO.builder()
        .id(player.getId())
        .firstName(player.getFirstName())
        .lastName(player.getLastName())
        .country(player.getCountry())
        .age(player.getAge())
        .marketValue(player.getMarketValue())
        .type(player.getType().name())
        .teamName(team.getName())
        .build());
    }

    return result;
  }

}
