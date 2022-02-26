package com.toptal.playersservice.handlers;

import com.toptal.playersservice.domain.events.CreatedTeamEvent;
import com.toptal.playersservice.domain.repositories.CreatedTeamEventRepository;
import com.toptal.playersservice.services.PlayersService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.toptal.playersservice.domain.events.CreatedTeamEvent.DEFAULT_TEAM_BUDGET;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreatedTeamEventHandler {

  private final CreatedTeamEventRepository createdTeamEventRepository;
  private final PlayersService playersService;

  public CreatedTeamEvent generate(final String userId) {

    final String teamId = UUID.randomUUID().toString();
    final CreatedTeamEvent createdTeamEvent = createdTeamEventRepository.insert(CreatedTeamEvent.builder()
      .teamId(teamId)
      .ownerId(userId)
      .budget(DEFAULT_TEAM_BUDGET)
      .country(RandomStringUtils.randomAlphabetic(10))
      .name(RandomStringUtils.randomAlphabetic(10))
      .build());

    playersService.batchGenerate(teamId, CreatedTeamEvent.DEFAULT_TEAM_SIZE);

    return createdTeamEvent;
  }

}