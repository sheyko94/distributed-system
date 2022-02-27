package com.toptal.playersservice.services;

import com.toptal.playersservice.domain.events.PlayerEvent;
import com.toptal.playersservice.resources.dtos.PlayerFullDTO;
import com.toptal.playersservice.resources.dtos.PlayerUpdateDTO;

import java.util.List;

public interface PlayerService {

  List<PlayerEvent> batchGenerate(String teamId, int noPlayers);

  PlayerFullDTO update(String playerId, PlayerUpdateDTO playerUpdateDTO);

}
