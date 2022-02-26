package com.toptal.playersservice.services;

import com.toptal.playersservice.domain.events.CreatedPlayerEvent;

import java.util.List;

public interface PlayersService {

  List<CreatedPlayerEvent> batchGenerate(String teamId, int noPlayers);

}
