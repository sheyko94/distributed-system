package com.toptal.playersservice.resources.controllers;

import com.toptal.playersservice.aggregates.PlayerFullAggregate;
import com.toptal.playersservice.aggregates.PlayersWithTeamAggregate;
import com.toptal.playersservice.resources.dtos.PlayerFullDTO;
import com.toptal.playersservice.resources.dtos.PlayerUpdateDTO;
import com.toptal.playersservice.resources.dtos.PlayersWithTeamGroupDTO;
import com.toptal.playersservice.resources.dtos.StringsWrapperDTO;
import com.toptal.playersservice.services.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(PlayerController.PLAYER_CONTROLLER_PREFIX)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerController {

  public final static String PLAYER_CONTROLLER_PREFIX = "/v1/player";

  private final PlayerService playerService;
  private final PlayerFullAggregate playerFullAggregate;
  private final PlayersWithTeamAggregate playersWithTeamAggregate;

  @PutMapping("{id}")
  public ResponseEntity<PlayerFullDTO> update(@PathVariable("id") final String id, @RequestBody final PlayerUpdateDTO playerUpdateDTO) {
    return ResponseEntity.ok(playerService.update(id, playerUpdateDTO));
  }

  @GetMapping("{id}")
  public ResponseEntity<PlayerFullDTO> fetchById(@PathVariable("id") final String id) {
    return ResponseEntity.ok(playerFullAggregate.fetchByPlayerId(id));
  }

  @PostMapping("extended-players")
  public ResponseEntity<PlayersWithTeamGroupDTO> fetch(@RequestBody final StringsWrapperDTO stringsWrapperDTO) {
    return ResponseEntity.ok(PlayersWithTeamGroupDTO.builder()
      .players(playersWithTeamAggregate.fetchByPlayerIds(stringsWrapperDTO.getIds()))
      .build());
  }

}
