package com.toptal.playersservice.domain.repositories;

import com.toptal.playersservice.domain.events.PlayerEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerEventRepository extends MongoRepository<PlayerEvent, String> {

  List<PlayerEvent> findByTeamIdInAndEventType(List<String> teamIds, PlayerEvent.PlayerEventType eventType);

  PlayerEvent findByPlayerIdAndEventType(String playerId, PlayerEvent.PlayerEventType eventType);

  List<PlayerEvent> findByPlayerIdInAndEventType(List<String> playerIds, PlayerEvent.PlayerEventType eventType);

  List<PlayerEvent> findByPlayerId(String playerId);

}
