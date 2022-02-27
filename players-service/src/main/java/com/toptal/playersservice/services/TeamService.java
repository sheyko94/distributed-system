package com.toptal.playersservice.services;

import com.toptal.playersservice.resources.dtos.TeamFullDTO;
import com.toptal.playersservice.resources.dtos.TeamUpdateDTO;

public interface TeamService {

  TeamFullDTO update(String teamId, TeamUpdateDTO teamUpdateDTO);

}
