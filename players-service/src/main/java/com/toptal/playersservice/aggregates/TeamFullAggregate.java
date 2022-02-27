package com.toptal.playersservice.aggregates;

import com.toptal.playersservice.domain.events.TeamEvent;
import com.toptal.playersservice.domain.repositories.TeamEventRepository;
import com.toptal.playersservice.resources.dtos.TeamFullDTO;
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
public class TeamFullAggregate {

  private final TeamEventRepository teamEventRepository;

  public TeamFullDTO fetchTeamFullInformation(final String teamId) {

    final List<TeamEvent> teamEvents = teamEventRepository.findByTeamId(teamId);

    final TeamEvent createTeamEvent = teamEvents
      .stream()
      .filter(teamEvent -> TeamEvent.TeamEventType.TEAM_CREATE.equals(teamEvent.getEventType()))
      .findFirst()
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Team with ID %s not found", teamId)));

    TeamFullDTO teamFullDTO = TeamFullDTO.builder()
      .id(teamId)
      .ownerId(createTeamEvent.getOwnerId())
      .name(createTeamEvent.getName())
      .country(createTeamEvent.getCountry())
      .budget(createTeamEvent.getBudget())
      .build();

    final List<TeamEvent> updatedTeamEvents = teamEvents
      .stream()
      .filter(playerEvent -> TeamEvent.TeamEventType.TEAM_UPDATE.equals(playerEvent.getEventType()))
      .collect(Collectors.toList());

    for (TeamEvent update : updatedTeamEvents) {

      if (TeamEvent.TeamEventSubtype.TEAM_UPDATE_INFO.equals(update.getEventSubtype())) {
        teamFullDTO.setName(update.getName());
        teamFullDTO.setCountry(update.getCountry());
      } else if (TeamEvent.TeamEventSubtype.TEAM_UPDATE_BUDGET.equals(update.getEventSubtype())) {
        teamFullDTO.setBudget(update.getBudget());
      }
    }

    return teamFullDTO;
  }

}
