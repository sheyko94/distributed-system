package com.toptal.playersservice.domain.repositories;

import com.toptal.playersservice.domain.events.UpdatedPlayerEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdatedPlayerEventRepository extends MongoRepository<UpdatedPlayerEvent, String> {

  List<UpdatedPlayerEvent> findByPlayerIdIn(List<String> playerIds);

}
