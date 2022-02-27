package com.toptal.marketservice.resources.controllers;

import com.toptal.marketservice.resources.dtos.MarketBuyPlayerDTO;
import com.toptal.marketservice.resources.dtos.MarketSellPlayerDTO;
import com.toptal.marketservice.services.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(MarketController.MARKET_CONTROLLER_PREFIX)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketController {

  public final static String MARKET_CONTROLLER_PREFIX = "/v1/market";

  private final MarketService marketService;

  @GetMapping
  public ResponseEntity<Void> getMarketStatus() {
    return ResponseEntity.ok().build();
  }

  @PostMapping("player/{id}/sell")
  public ResponseEntity<Void> sellPlayer(@PathVariable("id") final String id, @RequestBody final MarketSellPlayerDTO marketSellPlayerDTO) {
    marketService.sellPlayer(id, marketSellPlayerDTO);
    return ResponseEntity.ok().build();
  }

  @PostMapping("player/{id}/buy")
  public ResponseEntity<Void> buyPlayer(@PathVariable("id") final String id, @RequestBody final MarketBuyPlayerDTO marketBuyPlayerDTO) {
    log.info("@@@@@@@");
    marketService.buyPlayer(id, marketBuyPlayerDTO);
    return ResponseEntity.ok().build();
  }

}