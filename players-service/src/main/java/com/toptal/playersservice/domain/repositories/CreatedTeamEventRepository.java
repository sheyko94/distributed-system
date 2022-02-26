package com.toptal.playersservice.domain.repositories;

import com.toptal.playersservice.domain.events.CreatedTeamEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreatedTeamEventRepository extends MongoRepository<CreatedTeamEvent, String> {

  List<CreatedTeamEvent> findByOwnerId(String ownerId);

}
