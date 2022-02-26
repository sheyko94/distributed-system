package com.toptal.playersservice.services;

import com.toptal.playersservice.resources.dtos.TeamDTO;
import com.toptal.playersservice.resources.dtos.TeamUpdateDTO;

public interface TeamService {

  TeamDTO update(String teamId, TeamUpdateDTO teamUpdateDTO);

}
