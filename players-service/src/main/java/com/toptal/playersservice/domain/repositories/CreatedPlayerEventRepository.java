package com.toptal.playersservice.domain.repositories;

import com.toptal.playersservice.domain.events.CreatedPlayerEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreatedPlayerEventRepository extends MongoRepository<CreatedPlayerEvent, String> {

  List<CreatedPlayerEvent> findByTeamIdIn(List<String> teamIds);

}
