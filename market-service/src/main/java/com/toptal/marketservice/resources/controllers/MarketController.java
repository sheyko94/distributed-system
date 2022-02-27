package com.toptal.marketservice.resources.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toptal.marketservice.aggregates.MarketAggregate;
import com.toptal.marketservice.aggregates.MarketPlayerSellingDTO;
import com.toptal.marketservice.resources.dtos.MarketBuyPlayerDTO;
import com.toptal.marketservice.resources.dtos.MarketSellPlayerDTO;
import com.toptal.marketservice.services.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(MarketController.MARKET_CONTROLLER_PREFIX)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketController {

  public final static String MARKET_CONTROLLER_PREFIX = "/v1/market";

  private final MarketService marketService;
  private final MarketAggregate marketAggregate;

  @GetMapping
  public ResponseEntity<List<MarketPlayerSellingDTO>> getActiveMarket() throws JsonProcessingException {
    return ResponseEntity.ok(marketAggregate.fetchMarket());
  }

  @PostMapping("player/{id}/sell")
  public ResponseEntity<Void> sellPlayer(@PathVariable("id") final String id, @RequestBody final MarketSellPlayerDTO marketSellPlayerDTO) {
    marketService.sellPlayer(id, marketSellPlayerDTO);
    return ResponseEntity.ok().build();
  }

  @PostMapping("player/{id}/buy")
  public ResponseEntity<Void> buyPlayer(@PathVariable("id") final String id, @RequestBody final MarketBuyPlayerDTO marketBuyPlayerDTO) throws JsonProcessingException {
    marketService.buyPlayer(id, marketBuyPlayerDTO);
    return ResponseEntity.ok().build();
  }

}
