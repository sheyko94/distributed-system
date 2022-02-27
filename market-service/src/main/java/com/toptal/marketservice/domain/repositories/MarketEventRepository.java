package com.toptal.marketservice.domain.repositories;

import com.toptal.marketservice.domain.events.MarketEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketEventRepository extends MongoRepository<MarketEvent, String> {

}
