package com.example.login.controller;

import com.example.login.exception.AppException;
import com.example.login.model.Role;
import com.example.login.model.RoleName;
import com.example.login.model.User;
import com.example.login.model.UserLoginParam;
import com.example.login.payload.ApiResponse;
import com.example.login.payload.JwtAuthenticationResponse;
import com.example.login.payload.LoginRequest;
import com.example.login.payload.SignUpRequest;
import com.example.login.repository.RoleRepository;
import com.example.login.repository.UserLoginParamRepo;
import com.example.login.repository.UserRepository;
import com.example.login.security.JwtTokenProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Date;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @Autowired
    UserLoginParamRepo userLoginParamRepo;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("--- loginRequest browser ----"+loginRequest.getBrowser());
        logger.info("--- loginRequest location ---"+loginRequest.getLocation());
        logger.info("--- loginRequest mouseEvent ---"+loginRequest.getMouseEvent());
        logger.info("--- loginRequest keyBoardEvent ---"+loginRequest.getKeyBoardEvent());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        if(userRepository.existsByUsername(loginRequest.getUsernameOrEmail()) || userRepository.existsByEmail(loginRequest.getUsernameOrEmail())) {
            Date date = new Date(System.currentTimeMillis());
            // creating userLoginEvent Param
            UserLoginParam userLoginParam= new UserLoginParam(loginRequest.getUsernameOrEmail(),date,loginRequest.getBrowser().toString(),
                    loginRequest.getLocation().toString(),loginRequest.getMouseEvent().toString(),loginRequest.getKeyBoardEvent().toString());
            userLoginParamRepo.save(userLoginParam);
            return ResponseEntity.ok(new JwtAuthenticationResponse(jwt,"Event is Stored"));
        }

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
