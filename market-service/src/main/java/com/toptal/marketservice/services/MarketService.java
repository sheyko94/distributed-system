package com.toptal.marketservice.services;

import com.toptal.marketservice.resources.dtos.MarketBuyPlayerDTO;
import com.toptal.marketservice.resources.dtos.MarketSellPlayerDTO;

public interface MarketService {

  void sellPlayer(String playerId, MarketSellPlayerDTO marketSellPlayerDTO);

  void buyPlayer(String playerId, MarketBuyPlayerDTO marketBuyPlayerDTO);

}
