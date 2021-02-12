package com.example.login.repository;


import com.example.login.model.User;
import com.example.login.model.UserLoginParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLoginParamRepo extends JpaRepository<UserLoginParam, Long> {

    Boolean existsByUsername(String username);

    List<UserLoginParam> findByIdIn(String username);
}
