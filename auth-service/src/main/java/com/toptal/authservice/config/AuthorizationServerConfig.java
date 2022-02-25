package com.toptal.authservice.config;

import com.toptal.authservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Collections;

@Slf4j
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

  public static final String SCOPE_READ = "read";
  public static final String SCOPE_WRITE = "write";
  public static final String AUTHORIZED_GRANT_TYPE_PASSWORD = "password";
  public static final String AUTHORIZED_GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

  @Qualifier("authenticationManagerBean")
  private final AuthenticationManager authenticationManager;
  private final PasswordEncoder passwordEncoder;
  private final UserService userService;

  @Value("${security.oauth2.access-token-validity-seconds}")
  private Integer accessTokenValiditySeconds;

  @Value("${security.oauth2.refresh-token-validity-seconds}")
  private Integer refreshTokenValiditySeconds;

  @Value("${security.oauth2.client-id}")
  private String clientId;

  @Value("${security.oauth2.client-secret}")
  private String clientSecret;

  @Value("${security.oauth2.public-key}")
  private String publicKey;

  @Value("${security.oauth2.private-key}")
  private String privateKey;

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
    converter.setSigningKey(privateKey);
    converter.setVerifierKey(publicKey);
    return converter;
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
    oauthServer.allowFormAuthenticationForClients();
  }

  @Bean
  public JwtTokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
      .authenticationManager(authenticationManager)
      .userDetailsService(userService)
      .tokenStore(tokenStore())
      .tokenEnhancer(accessTokenConverter());
  }

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
      .inMemory()
      .withClient(clientId)
      .secret(passwordEncoder.encode(clientSecret))
      .scopes(SCOPE_READ, SCOPE_WRITE)
      .authorizedGrantTypes(AUTHORIZED_GRANT_TYPE_PASSWORD, AUTHORIZED_GRANT_TYPE_REFRESH_TOKEN)
      .accessTokenValiditySeconds(accessTokenValiditySeconds)
      .refreshTokenValiditySeconds(refreshTokenValiditySeconds);
  }

  @Bean
  @Primary
  public DefaultTokenServices tokenServices() {

    TokenEnhancerChain chain = new TokenEnhancerChain();
    chain.setTokenEnhancers(Collections.singletonList(accessTokenConverter()));

    DefaultTokenServices defaultTokenServices = new DefaultTokenServices();

    defaultTokenServices.setTokenStore(tokenStore());
    defaultTokenServices.setSupportRefreshToken(Boolean.TRUE);
    defaultTokenServices.setAccessTokenValiditySeconds(accessTokenValiditySeconds);
    defaultTokenServices.setRefreshTokenValiditySeconds(refreshTokenValiditySeconds);
    defaultTokenServices.setAuthenticationManager(authenticationManager);
    defaultTokenServices.setTokenEnhancer(chain);

    return defaultTokenServices;
  }

}
