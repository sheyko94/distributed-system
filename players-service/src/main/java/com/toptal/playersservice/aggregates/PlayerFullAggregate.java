package com.toptal.playersservice.aggregates;

import com.toptal.playersservice.aggregates.processors.PlayerProcessor;
import com.toptal.playersservice.aggregates.processors.dtos.PlayerProcessorDTO;
import com.toptal.playersservice.domain.events.PlayerEvent;
import com.toptal.playersservice.domain.repositories.PlayerEventRepository;
import com.toptal.playersservice.resources.dtos.PlayerFullDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerFullAggregate {

  private final PlayerEventRepository playerEventRepository;
  private final PlayerProcessor playerProcessor;

  public PlayerFullDTO fetchByPlayerId(final String playerId) {

    log.info("Calling PlayerFullAggregate.fetchByPlayerId for player ID {}", playerId);

    final List<PlayerEvent> playerEvents = playerEventRepository.findByPlayerId(playerId);
    final PlayerProcessorDTO playerProcessed = playerProcessor.process(playerEvents)
      .stream()
      .filter(playerProcessorDTO -> playerProcessorDTO.getId().equals(playerId))
      .findFirst()
      .get();

    return PlayerFullDTO.builder()
      .id(playerId)
      .teamId(playerProcessed.getTeamId())
      .firstName(playerProcessed.getFirstName())
      .lastName(playerProcessed.getLastName())
      .country(playerProcessed.getCountry())
      .age(playerProcessed.getAge())
      .type(playerProcessed.getType())
      .marketValue(playerProcessed.getMarketValue())
      .build();
  }

  public List<PlayerFullDTO> fetchByPlayerIds(final List<String> playerIds) {

    log.info("Calling PlayerFullAggregate.fetchByPlayerIds for player IDs {}", playerIds);

    final List<PlayerEvent> playerEvents = playerEventRepository.findByPlayerIdIn(playerIds);
    return playerProcessor.process(playerEvents)
      .stream()
      .map(playerProcessorDTO -> PlayerFullDTO.builder()
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
  }

}
