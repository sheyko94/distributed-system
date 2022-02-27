package com.toptal.playersservice.domain.repositories;

import com.toptal.playersservice.domain.events.TeamEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamEventRepository extends MongoRepository<TeamEvent, String> {

  List<TeamEvent> findByOwnerId(String ownerId);

  List<TeamEvent> findByOwnerIdAndEventType(String ownerId, TeamEvent.TeamEventType eventType);

  TeamEvent findByTeamIdAndEventType(String id, TeamEvent.TeamEventType eventType);

  List<TeamEvent> findByTeamIdInAndEventType(List<String> ids, TeamEvent.TeamEventType eventType);

}
