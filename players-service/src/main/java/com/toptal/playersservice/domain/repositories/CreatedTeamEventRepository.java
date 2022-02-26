package com.toptal.playersservice.domain.repositories;

import com.toptal.playersservice.domain.events.CreatedTeamEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatedTeamEventRepository extends MongoRepository<CreatedTeamEvent, String> {

}
