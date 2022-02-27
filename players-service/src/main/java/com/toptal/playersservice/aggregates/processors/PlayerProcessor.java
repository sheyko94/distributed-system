package com.toptal.playersservice.aggregates.processors;

import com.toptal.playersservice.aggregates.processors.dtos.PlayerProcessorDTO;
import com.toptal.playersservice.domain.events.PlayerEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerProcessor {

  public List<PlayerProcessorDTO> process(final List<PlayerEvent> playerEvents) {

    log.info("Calling PlayerProcessor...");

    final Map<String, List<PlayerEvent>> playerEventsGroupedByPlayerId = playerEvents
      .stream()
      .collect(Collectors.groupingBy(PlayerEvent::getPlayerId));

    final List<PlayerProcessorDTO> result = new ArrayList<>();

    for (Map.Entry<String, List<PlayerEvent>> entry : playerEventsGroupedByPlayerId.entrySet()) {

      final String currentPlayerId = entry.getKey();
      final List<PlayerEvent> currentPlayerEvents = entry.getValue();

      final PlayerEvent createdPlayerEvent = currentPlayerEvents
        .stream()
        .filter(playerEvent -> PlayerEvent.PlayerEventType.PLAYER_CREATE.equals(playerEvent.getEventType()))
        .findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Created Event for Player with ID %s not found", currentPlayerId)));

      PlayerProcessorDTO playerProcessorDTO = PlayerProcessorDTO.builder()
        .id(currentPlayerId)
        .teamId(createdPlayerEvent.getTeamId())
        .firstName(createdPlayerEvent.getFirstName())
        .lastName(createdPlayerEvent.getLastName())
        .country(createdPlayerEvent.getCountry())
        .age(createdPlayerEvent.getAge())
        .marketValue(createdPlayerEvent.getMarketValue())
        .type(createdPlayerEvent.getType())
        .build();

      final List<PlayerEvent> updatedPlayerEvents = currentPlayerEvents
        .stream()
        .filter(teamEvent -> PlayerEvent.PlayerEventType.PLAYER_UPDATE.equals(teamEvent.getEventType()))
        .sorted(Comparator.comparing(PlayerEvent::getDate))
        .collect(Collectors.toList());

      for (PlayerEvent update : updatedPlayerEvents) {
        if (PlayerEvent.PlayerEventSubtype.PLAYER_UPDATE_INFO.equals(update.getEventSubtype())) {
          playerProcessorDTO.setFirstName(update.getFirstName());
          playerProcessorDTO.setLastName(update.getLastName());
          playerProcessorDTO.setCountry(update.getCountry());
        } else if (PlayerEvent.PlayerEventSubtype.PLAYER_TRANSFER.equals(update.getEventSubtype())) {
          playerProcessorDTO.setTeamId(update.getTeamId());
          playerProcessorDTO.setMarketValue(update.getMarketValue());
        }
      }

      result.add(playerProcessorDTO);
    }

    return result;
  }

}
