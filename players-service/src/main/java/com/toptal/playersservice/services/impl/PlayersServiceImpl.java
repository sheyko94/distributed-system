package com.toptal.playersservice.services.impl;

import com.toptal.playersservice.domain.events.CreatedPlayerEvent;
import com.toptal.playersservice.domain.repositories.CreatedPlayerEventRepository;
import com.toptal.playersservice.services.PlayersService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayersServiceImpl implements PlayersService {

  private final CreatedPlayerEventRepository createdPlayerEventRepository;

  @Override
  public List<CreatedPlayerEvent> batchGenerate(String teamId, int noPlayers) {
    List<CreatedPlayerEvent> playersToCreate = generatePlayersToCreate(teamId, noPlayers);
    return createdPlayerEventRepository.saveAll(playersToCreate);
  }

  private List<CreatedPlayerEvent> generatePlayersToCreate(String teamId, int noPlayers) {

    List<CreatedPlayerEvent> playersToCreate = new ArrayList<>();
    int index = 1;

    while (index <= noPlayers) {
      playersToCreate.add(CreatedPlayerEvent.builder()
        .teamId(teamId)
        .type(getNextPlayerType(index))
        .marketValue(CreatedPlayerEvent.DEFAULT_PLAYER_MARKET_VALUE)
        .firstName(RandomStringUtils.randomAlphabetic(10))
        .lastName(RandomStringUtils.randomAlphabetic(10))
        .age(RandomUtils.nextInt())
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
