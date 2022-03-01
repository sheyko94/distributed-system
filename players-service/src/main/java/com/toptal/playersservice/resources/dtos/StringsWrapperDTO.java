package com.toptal.playersservice.resources.dtos;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StringsWrapperDTO {

  @NonNull
  private List<String> ids;

}
