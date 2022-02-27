package com.toptal.playersservice.aggregates;

import com.toptal.playersservice.domain.events.PlayerEvent;
import com.toptal.playersservice.domain.events.TeamEvent;
import com.toptal.playersservice.domain.repositories.PlayerEventRepository;
import com.toptal.playersservice.domain.repositories.TeamEventRepository;
import com.toptal.playersservice.resources.dtos.PlayerFullDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerFullAggregate {

  private final TeamEventRepository teamEventRepository;
  private final PlayerEventRepository playerEventRepository;

  public PlayerFullDTO fetchPlayerFullInformation(final String playerId) {

    log.info("Calling fetchPlayerFullInformation for player ID {}", playerId);

    final List<PlayerEvent> playerEvents = playerEventRepository.findByPlayerId(playerId);

    final PlayerEvent createdPlayerEvent = playerEvents
      .stream()
      .filter(playerEvent -> PlayerEvent.PlayerEventType.PLAYER_CREATE.equals(playerEvent.getEventType()))
      .findFirst()
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Player with ID %s not found", playerId)));

    final TeamEvent teamEvent = teamEventRepository.findByTeamIdAndEventType(createdPlayerEvent.getTeamId(), TeamEvent.TeamEventType.TEAM_CREATE);

    PlayerFullDTO playerFullDTO = PlayerFullDTO.builder()
      .id(playerId)
      .teamId(teamEvent.getTeamId())
      .ownerId(teamEvent.getOwnerId())
      .firstName(createdPlayerEvent.getFirstName())
      .lastName(createdPlayerEvent.getLastName())
      .country(createdPlayerEvent.getCountry())
      .age(createdPlayerEvent.getAge())
      .type(createdPlayerEvent.getType())
      .marketValue(createdPlayerEvent.getMarketValue())
      .build();

    final List<PlayerEvent> updatedPlayerEvents = playerEvents
      .stream()
      .filter(playerEvent -> PlayerEvent.PlayerEventType.PLAYER_UPDATE.equals(playerEvent.getEventType()))
      .collect(Collectors.toList());

    for (PlayerEvent update : updatedPlayerEvents) {
      if (PlayerEvent.PlayerEventSubtype.PLAYER_UPDATE_INFO.equals(update.getEventSubtype())) {
        playerFullDTO.setFirstName(update.getFirstName());
        playerFullDTO.setLastName(update.getLastName());
        playerFullDTO.setCountry(update.getCountry());
      } else if (PlayerEvent.PlayerEventSubtype.PLAYER_TRANSFER.equals(update.getEventSubtype())) {
        playerFullDTO.setTeamId(update.getTeamId());
        playerFullDTO.setMarketValue(update.getMarketValue());
      }
    }

    return playerFullDTO;
  }

}
