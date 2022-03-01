package com.toptal.authservice.mappers;

import com.toptal.authservice.domain.models.User;
import com.toptal.authservice.resources.dtos.UserDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class UserToUserDTOMapper {

  @Autowired
  private ModelMapper modelMapper;

  PropertyMap<User, UserDTO> mappings = new PropertyMap<>() {
    protected void configure() {
    }
  };

  public UserDTO map(User user) {
    TypeMap<User, UserDTO> typeMap = modelMapper.getTypeMap(User.class, UserDTO.class);
    if (Objects.isNull(typeMap)) {
      modelMapper.addMappings(mappings);
    }
    return modelMapper.map(user, UserDTO.class);
  }

}
