package com.toptal.marketservice.shared;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OAuth2RestTemplate {

  public static final String BEARER_TYPE = "bearer ";
  public static final String AUTHORIZATION_HEADER = "Authorization";

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;

  public <T> ResponseEntity<T> get(String url, Class<T> responseType) {

    return restTemplate.exchange(url, HttpMethod.GET, buildHttpEntity(getTokenFromHeader()), responseType);
  }

  public <T> ResponseEntity<T> get(String url, String token, Class<T> responseType) {

    return restTemplate.exchange(url, HttpMethod.GET, buildHttpEntity(token), responseType);
  }

  public <T> ResponseEntity<T> post(String url, Object payload, Class<T> responseType) throws JsonProcessingException {

    return restTemplate.exchange(url, HttpMethod.POST, buildHttpEntity(payload, getTokenFromHeader()), responseType);
  }

  public <T> ResponseEntity<T> put(String url, Object payload, Class<T> responseType) throws JsonProcessingException {

    return restTemplate.exchange(url, HttpMethod.PUT, buildHttpEntity(payload, getTokenFromHeader()), responseType);
  }

  private String getTokenFromHeader() {
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String token = request.getHeader(AUTHORIZATION_HEADER);

    if (Objects.isNull(token) || !StringUtils.startsWithIgnoreCase(token, BEARER_TYPE)) {
      throw new SecurityException(String.format("Invalid token: %s", Optional.ofNullable(token).orElse("empty token")));
    }

    return token.substring(BEARER_TYPE.length());
  }

  private HttpEntity<Object> buildHttpEntity(Object payload, String token) throws JsonProcessingException {
    final String json = objectMapper.writeValueAsString(payload);
    return new HttpEntity<>(json, httpHeaders(token));
  }

  private HttpEntity<Object> buildHttpEntity(String token) {
    return new HttpEntity<>(httpHeaders(token));
  }

  private HttpHeaders httpHeaders(String token) {
    final HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set(AUTHORIZATION_HEADER, BEARER_TYPE + token);
    return headers;
  }

}
