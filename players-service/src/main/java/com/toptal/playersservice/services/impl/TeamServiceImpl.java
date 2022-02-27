package com.toptal.playersservice.services.impl;

import com.toptal.playersservice.aggregates.TeamFullAggregate;
import com.toptal.playersservice.domain.events.TeamEvent;
import com.toptal.playersservice.domain.repositories.TeamEventRepository;
import com.toptal.playersservice.resources.dtos.TeamFullDTO;
import com.toptal.playersservice.resources.dtos.TeamUpdateDTO;
import com.toptal.playersservice.services.TeamService;
import com.toptal.playersservice.shared.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamServiceImpl implements TeamService {

  private final TeamEventRepository teamEventRepository;
  private final SecurityUtils securityUtils;
  private final TeamFullAggregate teamFullAggregate;

  @Override
  public TeamFullDTO update(String teamId, TeamUpdateDTO teamUpdateDTO) {

    final TeamEvent createdTeamEvent = Optional.ofNullable(teamEventRepository.findByTeamIdAndEventType(teamId, TeamEvent.TeamEventType.TEAM_CREATE))
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Team with ID %s not found", teamId)));

    final String loggedUserId = securityUtils.getLoggedUserID();

    if (!createdTeamEvent.getOwnerId().equals(loggedUserId)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("User with ID %s can not update the Team with ID %s", loggedUserId, teamId));
    }

    final String newName = teamUpdateDTO.getName();
    final String newCountry = teamUpdateDTO.getCountry();

    final TeamEvent updatedTeamEvent = teamEventRepository.save(TeamEvent.builder()
      .date(new Date())
      .eventType(TeamEvent.TeamEventType.TEAM_UPDATE)
      .eventSubtype(TeamEvent.TeamEventSubtype.TEAM_UPDATE_INFO)
      .teamId(teamId)
      .name(newName)
      .country(newCountry)
      .build());

    log.info("Team with ID {} has been updated. Event ID {}", teamId, updatedTeamEvent.getId());

    return teamFullAggregate.fetchByTeamId(teamId);
  }

}
