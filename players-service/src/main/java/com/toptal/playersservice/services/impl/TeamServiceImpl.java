package com.toptal.playersservice.services.impl;

import com.toptal.playersservice.domain.events.CreatedTeamEvent;
import com.toptal.playersservice.domain.events.UpdatedTeamEvent;
import com.toptal.playersservice.domain.repositories.CreatedTeamEventRepository;
import com.toptal.playersservice.domain.repositories.UpdatedTeamEventRepository;
import com.toptal.playersservice.resources.dtos.TeamDTO;
import com.toptal.playersservice.resources.dtos.TeamUpdateDTO;
import com.toptal.playersservice.services.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamServiceImpl implements TeamService {

  private final CreatedTeamEventRepository createdTeamEventRepository;
  private final UpdatedTeamEventRepository updatedTeamEventRepository;

  @Override
  public TeamDTO update(String teamId, TeamUpdateDTO teamUpdateDTO) {

    final CreatedTeamEvent createdTeamEvent = Optional.ofNullable(createdTeamEventRepository.findByTeamId(teamId))
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Team with ID %s not found", teamId)));

    final String newName = teamUpdateDTO.getName();
    final String newCountry = teamUpdateDTO.getCountry();

    final UpdatedTeamEvent updatedTeamEvent = updatedTeamEventRepository.save(UpdatedTeamEvent.builder()
      .teamId(teamId)
      .name(newName)
      .country(newCountry)
      .build());

    log.info("Team with ID {} has been updated. Event ID {}", teamId, updatedTeamEvent.getId());

    // TODO this return should return an element from an aggregate with the last version of a team based on all the events
    return TeamDTO.builder()
      .id(teamId)
      .budget(createdTeamEvent.getBudget())
      .name(newName)
      .country(newCountry)
      .build();
  }

}
