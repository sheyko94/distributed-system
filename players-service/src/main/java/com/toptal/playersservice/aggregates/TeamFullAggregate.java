package com.toptal.playersservice.aggregates;

import com.toptal.playersservice.aggregates.processors.TeamProcessor;
import com.toptal.playersservice.aggregates.processors.dtos.TeamProcessorDTO;
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
  private final TeamProcessor teamProcessor;

  public TeamFullDTO fetchByTeamId(final String teamId) {

    log.info("Calling TeamFullAggregate.fetchByTeamId for team ID {}", teamId);

    final List<TeamEvent> teamEvents = teamEventRepository.findByTeamId(teamId);
    final TeamProcessorDTO teamProcessed = teamProcessor.process(teamEvents)
      .stream()
      .filter(teamProcessorDTO -> teamProcessorDTO.getId().equals(teamId))
      .findFirst()
      .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Team with ID %s not found", teamId)));

    return TeamFullDTO.builder()
      .id(teamId)
      .ownerId(teamProcessed.getOwnerId())
      .name(teamProcessed.getName())
      .country(teamProcessed.getCountry())
      .budget(teamProcessed.getBudget())
      .build();
  }

  public List<TeamFullDTO> fetchByTeamIds(final List<String> teamIds) {

    log.info("Calling TeamFullAggregate.fetchByTeamIds for team IDs {}", teamIds);

    final List<TeamEvent> teamEvents = teamEventRepository.findByTeamIdIn(teamIds);
    return teamProcessor.process(teamEvents)
      .stream()
      .map(teamProcessorDTO -> TeamFullDTO.builder()
        .id(teamProcessorDTO.getId())
        .ownerId(teamProcessorDTO.getOwnerId())
        .name(teamProcessorDTO.getName())
        .country(teamProcessorDTO.getCountry())
        .budget(teamProcessorDTO.getBudget())
        .build()
      ).collect(Collectors.toList());

  }

}
