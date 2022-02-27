package com.toptal.marketservice.aggregates;

import com.toptal.marketservice.domain.events.MarketEvent;
import com.toptal.marketservice.domain.repositories.MarketEventRepository;
import com.toptal.marketservice.services.MarketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketAggregate {

  private final MarketEventRepository marketEventRepository;
  private final MarketService marketService;

  public List<MarketPlayerSellingDTO> fetchMarket() {

    log.info("Calling fetchMarket...");

    final Map<String, List<MarketEvent>> marketEventsGroupedByPlayerId = marketEventRepository.findAll()
      .stream()
      .collect(Collectors.groupingBy(MarketEvent::getPlayerId));

    final List<MarketPlayerSellingDTO> result = new ArrayList<>();

    for (Map.Entry<String, List<MarketEvent>> entry : marketEventsGroupedByPlayerId.entrySet()) {

      final MarketEvent sellingEvent = marketService.getSellingEventOrNull(entry.getValue());

      if (Objects.nonNull(sellingEvent)) {
        result.add(MarketPlayerSellingDTO.builder()
          .id(entry.getKey())
          .teamId(sellingEvent.getTeamId())
          .marketValue(sellingEvent.getValue())
          .build());
      }
    }

    return result;
  }

}
