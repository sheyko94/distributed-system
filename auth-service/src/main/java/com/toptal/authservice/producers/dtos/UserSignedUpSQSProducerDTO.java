package com.toptal.authservice.producers.dtos;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignedUpSQSProducerDTO {

  private String id;

}
