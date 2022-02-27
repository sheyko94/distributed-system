package com.toptal.marketservice.services.impl;

import com.toptal.marketservice.clients.PlayersServiceClient;
import com.toptal.marketservice.clients.dtos.PlayerFullDTO;
import com.toptal.marketservice.clients.dtos.TeamFullDTO;
import com.toptal.marketservice.domain.events.MarketEvent;
import com.toptal.marketservice.domain.repositories.MarketEventRepository;
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

import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MarketServiceImpl implements MarketService {

  private final MarketEventRepository marketEventRepository;
  private final PlayersServiceClient playersServiceClient;
  private final SecurityUtils securityUtils;

  @Override
  public void sellPlayer(final String playerId, final MarketSellPlayerDTO marketSellPlayerDTO) {

    final PlayerFullDTO playerFullDTO = playersServiceClient.fetchPlayerFullInformation(playerId);

    if (!playerFullDTO.getOwnerId().equals(securityUtils.getLoggedUserID())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("You can not sell the Player with ID %s because you do not own it", playerId));
    }

    // TODO add validations: you can not sell the player if it is already being sold.

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
  public void buyPlayer(final String playerId, final MarketBuyPlayerDTO marketBuyPlayerDTO) {

    // TODO add validations: you can not buy the player if it is not being sold.

    final TeamFullDTO teamFullDTO = playersServiceClient.fetchTeamFullInformation(marketBuyPlayerDTO.getTeamId());

    if (!teamFullDTO.getOwnerId().equals(securityUtils.getLoggedUserID())) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, String.format("You can not buy the Player with ID %s for the Team with ID %s because you do not own the team", playerId, teamFullDTO.getId()));
    }

    // TODO add validations: you can not buy the player with the same team that started the sale
    // TODO add validations: you can not buy the player if the team budget is not enough

    final MarketEvent marketEvent = marketEventRepository.save(MarketEvent.builder()
      .teamId(teamFullDTO.getId())
      .playerId(playerId)
      .date(new Date())
      .eventType(MarketEvent.MarketEventType.PLAYER_BOUGHT)
      .value(null) // TODO fetch the value from the selling event
      .build());

    log.info("Player bought event generated with ID {}", marketEvent.getId());
  }
}
