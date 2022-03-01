package com.toptal.marketservice.clients;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.toptal.marketservice.clients.dtos.PlayerFullDTO;
import com.toptal.marketservice.clients.dtos.PlayersWithTeamGroupDTO;
import com.toptal.marketservice.clients.dtos.StringsWrapperDTO;
import com.toptal.marketservice.clients.dtos.TeamFullDTO;
import com.toptal.marketservice.shared.OAuth2RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayersServiceClient {

  @Value("${hosts.players-service.url}")
  private String playersServiceUrl;

  private final OAuth2RestTemplate oAuth2RestTemplate;

  public PlayerFullDTO fetchPlayerFullInformation(final String playerId) {

    final String url = String.format("%s/players-service/v1/player/%s", playersServiceUrl, playerId);

    log.info("Fetching PlayerFullDTO from url {}", url);

    return oAuth2RestTemplate.get(url, PlayerFullDTO.class).getBody();
  }

  public PlayersWithTeamGroupDTO fetchPlayersWithTeam(final List<String> playerIds) throws JsonProcessingException {

    final String url = String.format("%s/players-service/v1/player/extended", playersServiceUrl);

    log.info("Fetching PlayerWithTeamGroupDTO from url {}", url);

    return oAuth2RestTemplate.post(url, StringsWrapperDTO.builder().ids(playerIds).build(), PlayersWithTeamGroupDTO.class).getBody();
  }

  public TeamFullDTO fetchTeamFullInformation(final String teamId) {

    final String url = String.format("%s/players-service/v1/team/%s", playersServiceUrl, teamId);

    log.info("Fetching TeamFullDTO from url {}", url);

    return oAuth2RestTemplate.get(url, TeamFullDTO.class).getBody();
  }

}
