package com.toptal.playersservice.shared;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class SecurityUtils {

  public static final String TOKEN_ENHANCE_USER_ID = "uid";

  public String getLoggedUserID() {
    Authentication authentication = getAuthentication();
    final OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
    JsonParser parser = JsonParserFactory.getJsonParser();
    Map<String, ?> tokenData = parser.parseMap(JwtHelper.decode(details.getTokenValue()).getClaims());
    return (String) tokenData.get(TOKEN_ENHANCE_USER_ID);
  }

  private Authentication getAuthentication() {
    return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
      .orElseThrow(() -> new UnauthorizedUserException("Authentication can't be null"));
  }

}
