package com.toptal.marketservice.aggregates;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toptal.marketservice.clients.PlayersServiceClient;
import com.toptal.marketservice.clients.dtos.PlayerWithTeamEntryDTO;
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
  private final PlayersServiceClient playersServiceClient;

  public List<MarketPlayerSellingDTO> fetchMarket() throws JsonProcessingException {

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

    final List<PlayerWithTeamEntryDTO> playerWithTeamEntryDTOs = playersServiceClient.fetchPlayersWithTeam(result
      .stream()
      .map(MarketPlayerSellingDTO::getId)
      .collect(Collectors.toList()))
      .getPlayers();

    for (MarketPlayerSellingDTO marketPlayerSellingDTO : result) {

      final PlayerWithTeamEntryDTO playerWithTeamEntry = playerWithTeamEntryDTOs
        .stream()
        .filter(entry -> entry.getId().equals(marketPlayerSellingDTO.getId()))
        .findFirst()
        .get();

      marketPlayerSellingDTO.setTeamName(playerWithTeamEntry.getTeamName());
      marketPlayerSellingDTO.setFirstName(playerWithTeamEntry.getFirstName());
      marketPlayerSellingDTO.setLastName(playerWithTeamEntry.getLastName());
      marketPlayerSellingDTO.setCountry(playerWithTeamEntry.getCountry());
      marketPlayerSellingDTO.setAge(playerWithTeamEntry.getAge());
      marketPlayerSellingDTO.setType(playerWithTeamEntry.getType());
    }

    return result;
  }

}
