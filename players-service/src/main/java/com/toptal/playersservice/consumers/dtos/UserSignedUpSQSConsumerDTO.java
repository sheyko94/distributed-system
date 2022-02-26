package com.toptal.playersservice.consumers.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignedUpSQSConsumerDTO {

  private String id;

}
