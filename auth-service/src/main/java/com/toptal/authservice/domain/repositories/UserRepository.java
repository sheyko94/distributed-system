package com.toptal.authservice.domain.repositories;

import com.toptal.authservice.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsernameIgnoreCase(String username);

  User findById(String id);

}
