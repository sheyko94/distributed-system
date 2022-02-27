package com.toptal.marketservice.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toptal.marketservice.domain.events.MarketEvent;
import com.toptal.marketservice.resources.dtos.MarketBuyPlayerDTO;
import com.toptal.marketservice.resources.dtos.MarketSellPlayerDTO;

import java.util.List;

public interface MarketService {

  void sellPlayer(String playerId, MarketSellPlayerDTO marketSellPlayerDTO);

  void buyPlayer(String playerId, MarketBuyPlayerDTO marketBuyPlayerDTO) throws JsonProcessingException;

  MarketEvent getSellingEventOrNull(final List<MarketEvent> events);

}
