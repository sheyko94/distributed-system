package com.toptal.playersservice.handlers;

import com.toptal.playersservice.aggregates.PlayerFullAggregate;
import com.toptal.playersservice.aggregates.TeamFullAggregate;
import com.toptal.playersservice.consumers.dtos.PlayerBoughtSQSConsumerDTO;
import com.toptal.playersservice.domain.events.PlayerEvent;
import com.toptal.playersservice.domain.events.TeamEvent;
import com.toptal.playersservice.domain.repositories.PlayerEventRepository;
import com.toptal.playersservice.domain.repositories.TeamEventRepository;
import com.toptal.playersservice.resources.dtos.PlayerFullDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayerBoughtEventHandler {

  private final TeamEventRepository teamEventRepository;
  private final TeamFullAggregate teamFullAggregate;
  private final PlayerFullAggregate playerFullAggregate;
  private final PlayerEventRepository playerEventRepository;

  public void handle(final String sqsEventId, final PlayerBoughtSQSConsumerDTO playerBoughtSQSConsumerDTO) {

    final BigDecimal value = playerBoughtSQSConsumerDTO.getValue();

    final String sellingTeamId = playerBoughtSQSConsumerDTO.getSellingTeamId();
    final TeamEvent updatedSellingTeamEvent = TeamEvent.builder()
      .date(new Date())
      .eventType(TeamEvent.TeamEventType.TEAM_UPDATE)
      .eventSubtype(TeamEvent.TeamEventSubtype.TEAM_UPDATE_BUDGET)
      .teamId(sellingTeamId)
      .budget(teamFullAggregate.fetchByTeamId(sellingTeamId).getBudget().add(value))
      .build();

    final String buyingTeamId = playerBoughtSQSConsumerDTO.getBuyingTeamId();
    final TeamEvent updatedBuyingTeamEvent = TeamEvent.builder()
      .date(new Date())
      .eventType(TeamEvent.TeamEventType.TEAM_UPDATE)
      .eventSubtype(TeamEvent.TeamEventSubtype.TEAM_UPDATE_BUDGET)
      .teamId(buyingTeamId)
      .budget(teamFullAggregate.fetchByTeamId(buyingTeamId).getBudget().subtract(value))
      .build();

    teamEventRepository.saveAll(Arrays.asList(updatedSellingTeamEvent, updatedBuyingTeamEvent));

    final String playerId = playerBoughtSQSConsumerDTO.getPlayerId();
    final PlayerFullDTO playerFullDTO = playerFullAggregate.fetchByPlayerId(playerId);

    final PlayerEvent updatedPlayerEvent = PlayerEvent.builder()
      .date(new Date())
      .eventType(PlayerEvent.PlayerEventType.PLAYER_UPDATE)
      .eventSubtype(PlayerEvent.PlayerEventSubtype.PLAYER_TRANSFER)
      .playerId(playerId)
      .teamId(buyingTeamId)
      .marketValue(getMarketValueRandomlyIncremented(playerFullDTO.getMarketValue()))
      .build();

    playerEventRepository.save(updatedPlayerEvent);

    log.info("Successfully handled PlayerBoughtEvent with SQS event ID {}", sqsEventId);
  }

  private BigDecimal getMarketValueRandomlyIncremented(final BigDecimal value) {

    final int randomIncrement = RandomUtils.nextInt(PlayerEvent.MINIMUM_PLAYER_MARKET_VALUE_PERCENT_INCREMENT, PlayerEvent.MAXIMUM_PLAYER_MARKET_VALUE_PERCENT_INCREMENT);

    log.info("Random increment of {}%", randomIncrement);

    return (value.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)).multiply(BigDecimal.valueOf(100).add(BigDecimal.valueOf(randomIncrement)));
  }

}
