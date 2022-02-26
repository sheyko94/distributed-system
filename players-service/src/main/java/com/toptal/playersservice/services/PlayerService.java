package com.toptal.playersservice.services;

import com.toptal.playersservice.domain.events.CreatedPlayerEvent;
import com.toptal.playersservice.resources.dtos.PlayerDTO;
import com.toptal.playersservice.resources.dtos.PlayerUpdateDTO;

import java.util.List;

public interface PlayerService {

  List<CreatedPlayerEvent> batchGenerate(String teamId, int noPlayers);

  PlayerDTO update(String playerId, PlayerUpdateDTO playerUpdateDTO);

}
