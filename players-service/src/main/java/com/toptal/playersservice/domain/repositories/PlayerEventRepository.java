package com.toptal.playersservice.domain.repositories;

import com.toptal.playersservice.domain.events.PlayerEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerEventRepository extends MongoRepository<PlayerEvent, String> {

  List<PlayerEvent> findByTeamIdIn(List<String> teamIds);

  List<PlayerEvent> findByPlayerId(String playerId);

  List<PlayerEvent> findByPlayerIdIn(List<String> playerIds);

}
