package com.toptal.authservice.resources.controllers;

import com.toptal.authservice.shared.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(StatusController.STATUS_CONTROLLER_PREFIX)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StatusController {

  public final static String STATUS_CONTROLLER_PREFIX = "/v1/status";

  private final SecurityUtils securityUtils;

  @GetMapping
  public ResponseEntity<Void> getStatus() {
    log.info(String.format("StatusController - status check for user with ID %s and Username %s", securityUtils.getLoggedUserID(), securityUtils.getLoggedUserUsername()));
    return ResponseEntity.ok().build();
  }

}
