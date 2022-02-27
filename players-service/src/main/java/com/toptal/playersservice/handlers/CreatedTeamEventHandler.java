package com.toptal.playersservice.handlers;

import com.toptal.playersservice.domain.events.TeamEvent;
import com.toptal.playersservice.domain.repositories.TeamEventRepository;
import com.toptal.playersservice.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.UUID;

import static com.toptal.playersservice.domain.events.TeamEvent.DEFAULT_TEAM_BUDGET;

@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreatedTeamEventHandler {

  private final TeamEventRepository teamEventRepository;
  private final PlayerService playersService;

  public TeamEvent generate(final String userId) {

    final String teamId = UUID.randomUUID().toString();
    final TeamEvent createdTeamEvent = teamEventRepository.save(TeamEvent.builder()
      .date(new Date())
      .eventType(TeamEvent.TeamEventType.TEAM_CREATE)
      .teamId(teamId)
      .ownerId(userId)
      .budget(DEFAULT_TEAM_BUDGET)
      .country(RandomStringUtils.randomAlphabetic(10))
      .name(RandomStringUtils.randomAlphabetic(10))

      .build());

    playersService.batchGenerate(teamId, TeamEvent.DEFAULT_TEAM_SIZE);

    return createdTeamEvent;
  }

}
