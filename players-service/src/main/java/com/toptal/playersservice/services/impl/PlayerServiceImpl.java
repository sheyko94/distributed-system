package com.toptal.playersservice.services.impl;

import com.toptal.playersservice.aggregates.PlayerFullAggregate;
import com.toptal.playersservice.domain.events.PlayerEvent;
import com.toptal.playersservice.domain.events.TeamEvent;
import com.toptal.playersservice.domain.repositories.PlayerEventRepository;
import com.toptal.playersservice.domain.repositories.TeamEventRepository;
import com.toptal.playersservice.resources.dtos.PlayerFullDTO;
import com.toptal.playersservice.resources.dtos.PlayerUpdateDTO;
import com.toptal.playersservice.services.PlayerService;
import com.toptal.playersservice.shared.SecurityUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerServiceImpl implements PlayerService {

  private final PlayerEventRepository playerEventRepository;
  private final SecurityUtils securityUtils;
  private final TeamEventRepository teamEventRepository;
  private final PlayerFullAggregate playerFullAggregate;

  @Override
  public List<PlayerEvent> batchGenerate(@NonNull final String teamId, final int noPlayers) {
    List<PlayerEvent> playersToCreate = generatePlayersToCreate(teamId, noPlayers);
    return playerEventRepository.saveAll(playersToCreate);
  }

  @Override
  public PlayerFullDTO update(@NonNull final String playerId, @NonNull final PlayerUpdateDTO playerUpdateDTO) {

    final String loggedUserId = securityUtils.getLoggedUserID();

    log.info("User with ID {} is updating the player with ID {}", loggedUserId, playerId);

    final PlayerFullDTO currentPlayer = Optional.ofNullable(playerFullAggregate.fetchByPlayerId(playerId))
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Player with ID %s not found", playerId)));

    List<String> teamsIdsForTheLoggedUser = teamEventRepository.findByOwnerId(loggedUserId)
      .stream()
      .map(TeamEvent::getTeamId)
      .distinct()
      .collect(Collectors.toList());

    if (!teamsIdsForTheLoggedUser.contains(currentPlayer.getTeamId())) {
      log.info("Forbidden user trying to update a player. User ID {} with teams IDs {}", loggedUserId, teamsIdsForTheLoggedUser);
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("User with ID %s can not update the Player with ID %s", loggedUserId, playerId));
    }

    final String newFirstName = playerUpdateDTO.getFirstName();
    final String newLastName = playerUpdateDTO.getLastName();
    final String newCountry = playerUpdateDTO.getCountry();

    final PlayerEvent updatedPlayerEvent = playerEventRepository.save(PlayerEvent.builder()
      .date(new Date())
      .eventType(PlayerEvent.PlayerEventType.PLAYER_UPDATE)
      .eventSubtype(PlayerEvent.PlayerEventSubtype.PLAYER_UPDATE_INFO)
      .playerId(playerId)
      .firstName(newFirstName)
      .lastName(newLastName)
      .country(newCountry)
      .build());

    log.info("Player with ID {} has been updated. Event ID {}", playerId, updatedPlayerEvent.getId());

    return playerFullAggregate.fetchByPlayerId(playerId);
  }

  private List<PlayerEvent> generatePlayersToCreate(@NonNull final String teamId, @NonNull final int noPlayers) {

    List<PlayerEvent> playersToCreate = new ArrayList<>();
    int index = 1;

    while (index <= noPlayers) {
      playersToCreate.add(PlayerEvent.builder()
        .eventType(PlayerEvent.PlayerEventType.PLAYER_CREATE)
        .date(new Date())
        .playerId(UUID.randomUUID().toString())
        .teamId(teamId)
        .type(getNextPlayerType(index))
        .marketValue(PlayerEvent.DEFAULT_PLAYER_MARKET_VALUE)
        .firstName(RandomStringUtils.randomAlphabetic(10))
        .lastName(RandomStringUtils.randomAlphabetic(10))
        .age(RandomUtils.nextInt(PlayerEvent.MINIMUM_PLAYER_AGE, PlayerEvent.MAXIMUM_PLAYER_AGE))
        .country(RandomStringUtils.randomAlphabetic(10))
        .build());
      index++;
    }

    return playersToCreate;
  }

  private PlayerEvent.PlayerTypeEnum getNextPlayerType(int index) {

    if (index <= 3) {
      return PlayerEvent.PlayerTypeEnum.GOALKEEPER;
    }

    if (index <= 9) {
      return PlayerEvent.PlayerTypeEnum.DEFENDER;
    }

    if (index <= 15) {
      return PlayerEvent.PlayerTypeEnum.MIDFIELDER;
    }

    return PlayerEvent.PlayerTypeEnum.ATTACKER;
  }

}
