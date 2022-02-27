package com.toptal.marketservice.clients;

import com.toptal.marketservice.clients.dtos.PlayerFullDTO;
import com.toptal.marketservice.clients.dtos.TeamFullDTO;
import com.toptal.marketservice.shared.OAuth2RestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PlayersServiceClient {

  @Value("${hosts.players-service}")
  private String playersServiceHost;

  private final OAuth2RestTemplate oAuth2RestTemplate;

  public PlayerFullDTO fetchPlayerFullInformation(final String playerId) {

    final String url = String.format("%s/v1/player/%s", playersServiceHost, playerId);

    log.info("Fetching PlayerFullDTO from url {}", url);

    return oAuth2RestTemplate.get(url, PlayerFullDTO.class).getBody();
  }

  public TeamFullDTO fetchTeamFullInformation(final String teamId) {

    final String url = String.format("%s/v1/team/%s", playersServiceHost, teamId);

    log.info("Fetching TeamFullDTO from url {}", url);

    return oAuth2RestTemplate.get(url, TeamFullDTO.class).getBody();
  }

}
