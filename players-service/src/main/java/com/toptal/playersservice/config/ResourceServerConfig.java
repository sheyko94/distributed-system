package com.toptal.playersservice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Slf4j
@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  public static final String OPEN_ENDPOINTS_MATCHER = "**";

  @Value("${hosts.auth-service.url}")
  private String authServiceUrl;

  @Value("${security.oauth2.client-id}")
  private String clientId;

  @Value("${security.oauth2.client-secret}")
  private String clientSecret;

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http
      .csrf()
      .disable()
      .anonymous()
      .and()
      .authorizeRequests()
      .antMatchers(OPEN_ENDPOINTS_MATCHER).permitAll()
      .and()
      .sessionManagement()
      .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
  }

  @Primary
  @Bean
  public RemoteTokenServices tokenService() {
    RemoteTokenServices tokenService = new RemoteTokenServices();
    final String checkTokenEndpointUrl = String.format("%s/auth-service/oauth/check_token", authServiceUrl);
    log.info("Set CheckTokenEndpointUrl as {}", checkTokenEndpointUrl);
    tokenService.setCheckTokenEndpointUrl(checkTokenEndpointUrl);
    tokenService.setClientId(clientId);
    tokenService.setClientSecret(clientSecret);
    return tokenService;
  }

}
