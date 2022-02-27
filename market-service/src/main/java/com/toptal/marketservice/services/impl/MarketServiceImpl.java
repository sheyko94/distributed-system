package com.toptal.marketservice.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toptal.marketservice.clients.PlayersServiceClient;
import com.toptal.marketservice.clients.dtos.PlayerFullDTO;
import com.toptal.marketservice.clients.dtos.TeamFullDTO;
import com.toptal.marketservice.domain.events.MarketEvent;
import com.toptal.marketservice.domain.repositories.MarketEventRepository;
import com.toptal.marketservice.producers.PlayerBoughtSQSProducer;
import com.toptal.marketservice.producers.dtos.PlayerBoughtSQSProducerDTO;
import com.toptal.marketservice.resources.dtos.MarketBuyPlayerDTO;
import com.toptal.marketservice.resources.dtos.MarketSellPlayerDTO;
import com.toptal.marketservice.services.MarketService;
import com.toptal.marketservice.shared.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketServiceImpl implements MarketService {

  private final MarketEventRepository marketEventRepository;
  private final PlayersServiceClient playersServiceClient;
  private final SecurityUtils securityUtils;
  private final PlayerBoughtSQSProducer playerBoughtSQSProducer;

  @Override
  public void sellPlayer(final String playerId, final MarketSellPlayerDTO marketSellPlayerDTO) {

    final PlayerFullDTO playerFullDTO = playersServiceClient.fetchPlayerFullInformation(playerId);
    final TeamFullDTO buyingTeamFullDTO = playersServiceClient.fetchTeamFullInformation(playerFullDTO.getTeamId());

    if (!buyingTeamFullDTO.getOwnerId().equals(securityUtils.getLoggedUserID())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("You can not sell the Player with ID %s because you do not own it", playerId));
    }

    final List<MarketEvent> events = marketEventRepository.findByPlayerIdAndEventTypeIn(playerId, Arrays.asList(
      MarketEvent.MarketEventType.PLAYER_SALE, MarketEvent.MarketEventType.PLAYER_BOUGHT));

    if (Objects.nonNull(getSellingEventOrNull(events))) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The Player with ID %s is already being sold", playerId));
    }

    final MarketEvent marketEvent = marketEventRepository.save(MarketEvent.builder()
      .teamId(playerFullDTO.getTeamId())
      .playerId(playerId)
      .date(new Date())
      .eventType(MarketEvent.MarketEventType.PLAYER_SALE)
      .value(marketSellPlayerDTO.getValue())
      .build());

    log.info("Player sell event generated with ID {}", marketEvent.getId());
  }

  @Override
  public void buyPlayer(final String playerId, final MarketBuyPlayerDTO marketBuyPlayerDTO) throws JsonProcessingException {

    final List<MarketEvent> events = marketEventRepository.findByPlayerIdAndEventTypeIn(playerId, Arrays.asList(
      MarketEvent.MarketEventType.PLAYER_SALE, MarketEvent.MarketEventType.PLAYER_BOUGHT));

    final MarketEvent sellingPlayerEvent = getSellingEventOrNull(events);

    if (Objects.isNull(sellingPlayerEvent)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The Player with ID %s is not being sold", playerId));
    }

    final TeamFullDTO buyingTeamFullDTO = playersServiceClient.fetchTeamFullInformation(marketBuyPlayerDTO.getTeamId());

    if (!buyingTeamFullDTO.getOwnerId().equals(securityUtils.getLoggedUserID())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("You can not buy the Player with ID %s for the Team with ID %s because you do not own the team", playerId, buyingTeamFullDTO.getId()));
    }

    final String teamSellingId = sellingPlayerEvent.getTeamId();
    final String teamBuyingId = marketBuyPlayerDTO.getTeamId();

    if (teamSellingId.equals(teamBuyingId)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("You can not sell and buy the Player with ID %s with the same Team with ID %s", playerId, teamBuyingId));
    }

    final BigDecimal sellingValue = sellingPlayerEvent.getValue();
    final BigDecimal buyingTeamBudget = buyingTeamFullDTO.getBudget();

    if (sellingValue.compareTo(buyingTeamBudget) > 0) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("The team with ID %s does not have budget enough to buy the player with ID %s. Selling price is %s and budget is %s", teamBuyingId, playerId, sellingValue, buyingTeamBudget));
    }

    final MarketEvent marketEvent = marketEventRepository.save(MarketEvent.builder()
      .teamId(teamBuyingId)
      .playerId(playerId)
      .date(new Date())
      .eventType(MarketEvent.MarketEventType.PLAYER_BOUGHT)
      .value(sellingValue)
      .build());

    log.info("Player bought event generated with ID {}", marketEvent.getId());

    playerBoughtSQSProducer.send(PlayerBoughtSQSProducerDTO.builder()
      .playerId(playerId)
      .sellingTeamId(teamSellingId)
      .buyingTeamId(teamBuyingId)
      .value(sellingValue)
      .build());
  }

  @Override
  public MarketEvent getSellingEventOrNull(final List<MarketEvent> events) {

    final List<MarketEvent> marketEventsForThePlayer = events
      .stream()
      .sorted(Comparator.comparing(MarketEvent::getDate))
      .collect(Collectors.toList());

    if (marketEventsForThePlayer.isEmpty()) {
      return null;
    }

    final MarketEvent lastEvent = marketEventsForThePlayer.get(marketEventsForThePlayer.size() - 1);

    if (MarketEvent.MarketEventType.PLAYER_BOUGHT.equals(lastEvent.getEventType())) {
      return null;
    }

    return lastEvent;
  }
}
