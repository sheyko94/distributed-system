package com.toptal.marketservice.domain.repositories;

import com.toptal.marketservice.domain.events.MarketEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MarketEventRepository extends MongoRepository<MarketEvent, String> {

  List<MarketEvent> findByPlayerIdAndEventTypeIn(String playerId, List<MarketEvent.MarketEventType> eventTypes);
}
