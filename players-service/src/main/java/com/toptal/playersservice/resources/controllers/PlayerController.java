package com.toptal.playersservice.resources.controllers;

import com.toptal.playersservice.aggregates.PlayerFullAggregate;
import com.toptal.playersservice.resources.dtos.PlayerDTO;
import com.toptal.playersservice.resources.dtos.PlayerFullDTO;
import com.toptal.playersservice.resources.dtos.PlayerUpdateDTO;
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

  @PutMapping("{id}")
  public ResponseEntity<PlayerDTO> update(@PathVariable("id") final String id, @RequestBody final PlayerUpdateDTO playerUpdateDTO) {
    return ResponseEntity.ok(playerService.update(id, playerUpdateDTO));
  }

  @GetMapping("{id}")
  public ResponseEntity<PlayerFullDTO> fetch(@PathVariable("id") final String id) {
    return ResponseEntity.ok(playerFullAggregate.fetchPlayerFullInformation(id));
  }

}
