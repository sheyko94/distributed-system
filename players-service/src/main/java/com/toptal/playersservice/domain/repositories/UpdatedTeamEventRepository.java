package com.toptal.playersservice.domain.repositories;

import com.toptal.playersservice.domain.events.UpdatedTeamEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdatedTeamEventRepository extends MongoRepository<UpdatedTeamEvent, String> {

  List<UpdatedTeamEvent> findByTeamIdIn(List<String> teamIds);

}
