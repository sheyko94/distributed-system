package com.toptal.playersservice.aggregates.processors;

import com.toptal.playersservice.aggregates.processors.dtos.TeamProcessorDTO;
import com.toptal.playersservice.domain.events.TeamEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamProcessor {

  public List<TeamProcessorDTO> process(final List<TeamEvent> teamEvents) {

    log.info("Calling TeamProcessor...");

    final Map<String, List<TeamEvent>> teamEventsGroupedByTeamId = teamEvents
      .stream()
      .collect(Collectors.groupingBy(TeamEvent::getTeamId));

    final List<TeamProcessorDTO> result = new ArrayList<>();

    for (Map.Entry<String, List<TeamEvent>> entry : teamEventsGroupedByTeamId.entrySet()) {

      final String currentTeamId = entry.getKey();
      final List<TeamEvent> currentTeamEvents = entry.getValue();

      final TeamEvent createdTeamEvent = currentTeamEvents
        .stream()
        .filter(teamEvent -> TeamEvent.TeamEventType.TEAM_CREATE.equals(teamEvent.getEventType()))
        .findFirst()
        .get();

      TeamProcessorDTO teamProcessorDTO = TeamProcessorDTO.builder()
        .id(currentTeamId)
        .ownerId(createdTeamEvent.getOwnerId())
        .name(createdTeamEvent.getName())
        .country(createdTeamEvent.getCountry())
        .budget(createdTeamEvent.getBudget())
        .build();

      final List<TeamEvent> updatedTeamEvents = currentTeamEvents
        .stream()
        .filter(teamEvent -> TeamEvent.TeamEventType.TEAM_UPDATE.equals(teamEvent.getEventType()))
        .collect(Collectors.toList());

      for (TeamEvent update : updatedTeamEvents) {
        if (TeamEvent.TeamEventSubtype.TEAM_UPDATE_INFO.equals(update.getEventSubtype())) {
          teamProcessorDTO.setName(update.getName());
          teamProcessorDTO.setCountry(update.getCountry());
        } else if (TeamEvent.TeamEventSubtype.TEAM_UPDATE_BUDGET.equals(update.getEventSubtype())) {
          teamProcessorDTO.setBudget(update.getBudget());
        }
      }

      result.add(teamProcessorDTO);
    }

    return result;
  }

}
