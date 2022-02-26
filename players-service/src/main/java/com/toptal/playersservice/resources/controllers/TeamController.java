package com.toptal.playersservice.resources.controllers;

import com.toptal.playersservice.aggregates.TeamWithPlayersAggregate;
import com.toptal.playersservice.aggregates.dtos.TeamWithPlayersDTO;
import com.toptal.playersservice.resources.dtos.TeamDTO;
import com.toptal.playersservice.resources.dtos.TeamUpdateDTO;
import com.toptal.playersservice.services.TeamService;
import com.toptal.playersservice.shared.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(TeamController.TEAM_CONTROLLER_PREFIX)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeamController {

  public final static String TEAM_CONTROLLER_PREFIX = "/v1/team";

  private final TeamWithPlayersAggregate teamAndPlayersAggregate;
  private final SecurityUtils securityUtils;
  private final TeamService teamService;

  @GetMapping
  public ResponseEntity<List<TeamWithPlayersDTO>> fetchTeamWithPlayersByLoggedUser() {
    return ResponseEntity.ok(teamAndPlayersAggregate.fetchTeamWithPlayersByOwnerID(securityUtils.getLoggedUserID()));
  }

  @PutMapping("{id}")
  public ResponseEntity<TeamDTO> updateTeam(@PathVariable("id") final String id, @RequestBody final TeamUpdateDTO teamUpdateDTO) {
    return ResponseEntity.ok(teamService.update(id, teamUpdateDTO));
  }

}