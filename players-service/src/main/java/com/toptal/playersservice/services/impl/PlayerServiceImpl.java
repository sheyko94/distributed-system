package com.toptal.playersservice.services.impl;

import com.toptal.playersservice.domain.events.CreatedPlayerEvent;
import com.toptal.playersservice.domain.events.UpdatedPlayerEvent;
import com.toptal.playersservice.domain.repositories.CreatedPlayerEventRepository;
import com.toptal.playersservice.domain.repositories.UpdatedPlayerEventRepository;
import com.toptal.playersservice.resources.dtos.PlayerDTO;
import com.toptal.playersservice.resources.dtos.PlayerUpdateDTO;
import com.toptal.playersservice.services.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerServiceImpl implements PlayerService {

  private final CreatedPlayerEventRepository createdPlayerEventRepository;
  private final UpdatedPlayerEventRepository updatedPlayerEventRepository;

  @Override
  public List<CreatedPlayerEvent> batchGenerate(String teamId, int noPlayers) {
    List<CreatedPlayerEvent> playersToCreate = generatePlayersToCreate(teamId, noPlayers);
    return createdPlayerEventRepository.saveAll(playersToCreate);
  }

  @Override
  public PlayerDTO update(String playerId, PlayerUpdateDTO playerUpdateDTO) {

    final CreatedPlayerEvent createdPlayerEvent = Optional.ofNullable(createdPlayerEventRepository.findByPlayerId(playerId))
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Player with ID %s not found", playerId)));

    final String newFirstName = playerUpdateDTO.getFirstName();
    final String newLastName = playerUpdateDTO.getLastName();
    final String newCountry = playerUpdateDTO.getCountry();

    final UpdatedPlayerEvent updatedPlayerEvent = updatedPlayerEventRepository.save(UpdatedPlayerEvent.builder()
      .playerId(playerId)
      .firstName(newFirstName)
      .lastName(newLastName)
      .country(newCountry)
      .build());

    log.info("Player with ID {} has been updated. Event ID {}", playerId, updatedPlayerEvent.getId());

    // TODO this return should return an element from an aggregate with the last version of a player based on all the events
    return PlayerDTO.builder()
      .id(playerId)
      .marketValue(createdPlayerEvent.getMarketValue())
      .firstName(newFirstName)
      .lastName(newLastName)
      .country(newCountry)
      .type(createdPlayerEvent.getType())
      .age(createdPlayerEvent.getAge())
      .build();
  }

  private List<CreatedPlayerEvent> generatePlayersToCreate(String teamId, int noPlayers) {

    List<CreatedPlayerEvent> playersToCreate = new ArrayList<>();
    int index = 1;

    while (index <= noPlayers) {
      playersToCreate.add(CreatedPlayerEvent.builder()
        .playerId(UUID.randomUUID().toString())
        .teamId(teamId)
        .type(getNextPlayerType(index))
        .marketValue(CreatedPlayerEvent.DEFAULT_PLAYER_MARKET_VALUE)
        .firstName(RandomStringUtils.randomAlphabetic(10))
        .lastName(RandomStringUtils.randomAlphabetic(10))
        .age(RandomUtils.nextInt(CreatedPlayerEvent.MINIMUM_PLAYER_AGE, CreatedPlayerEvent.MAXIMUM_PLAYER_AGE))
        .country(RandomStringUtils.randomAlphabetic(10))
        .build());
      index++;
    }

    return playersToCreate;
  }

  private CreatedPlayerEvent.PlayerTypeEnum getNextPlayerType(int index) {

    if (index <= 3) {
      return CreatedPlayerEvent.PlayerTypeEnum.GOALKEEPER;
    }

    if (index <= 9) {
      return CreatedPlayerEvent.PlayerTypeEnum.DEFENDER;
    }

    if (index <= 15) {
      return CreatedPlayerEvent.PlayerTypeEnum.MIDFIELDER;
    }

    return CreatedPlayerEvent.PlayerTypeEnum.ATTACKER;
  }

}
