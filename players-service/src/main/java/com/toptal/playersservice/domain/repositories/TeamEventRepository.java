package com.toptal.playersservice.domain.repositories;

import com.toptal.playersservice.domain.events.TeamEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamEventRepository extends MongoRepository<TeamEvent, String> {

  List<TeamEvent> findByOwnerId(String ownerId);

  List<TeamEvent> findByTeamIdIn(List<String> ids);

  List<TeamEvent> findByTeamId(String id);

}
