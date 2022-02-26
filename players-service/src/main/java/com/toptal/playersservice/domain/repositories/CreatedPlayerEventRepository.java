package com.toptal.playersservice.domain.repositories;

import com.toptal.playersservice.domain.events.CreatedPlayerEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatedPlayerEventRepository extends MongoRepository<CreatedPlayerEvent, String> {

}
