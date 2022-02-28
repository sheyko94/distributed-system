package com.toptal.gatewayservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

@Configuration
@EnableResourceServer
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GatewaySecurityConfig extends ResourceServerConfigurerAdapter {

  private static final String[] AUTH_WHITELIST = {
    "/auth-service/v1/sign-up",
    "/auth-service/oauth/token",
    "/auth-service/oauth/check_token"
  };

  @Override
  public void configure(final HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .antMatchers(AUTH_WHITELIST)
      .permitAll()
      .antMatchers("/**")
      .authenticated();
  }

}
