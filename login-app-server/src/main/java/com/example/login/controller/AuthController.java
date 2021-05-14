package com.example.login.controller;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonObject;
import com.example.login.exception.AppException;
import com.example.login.model.Role;
import com.example.login.model.RoleName;
import com.example.login.model.User;
import com.example.login.model.UserLoginParam;
import com.example.login.payload.*;
import com.example.login.repository.RoleRepository;
import com.example.login.repository.UserLoginParamRepo;
import com.example.login.repository.UserRepository;
import com.example.login.security.JwtTokenProvider;

import com.example.login.service.EmailSenderService;
import com.example.login.service.RedisService;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Date;

import org.json.JSONObject;

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

    @Autowired
    private EmailSenderService service;

    @Autowired
    private RedisService redisService;

    public String authenticationMethod = "normal";  // security_question  OTP   normal

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("--- loginRequest browser ----" + loginRequest.getBrowser());
        logger.info("--- loginRequest location ---" + loginRequest.getLocation());
        logger.info("--- loginRequest mouseEvent ---" + loginRequest.getMouseEvent());
        logger.info("--- loginRequest keyBoardEvent ---" + loginRequest.getKeyBoardEvent());
        logger.info("--- loginRequest BrowserInfo-" + loginRequest.getBrowserInfo());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        if (userRepository.existsByUsername(loginRequest.getUsernameOrEmail()) || userRepository.existsByEmail(loginRequest.getUsernameOrEmail())) {
            User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail(), loginRequest.getUsernameOrEmail())
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with username or email : " + loginRequest.getUsernameOrEmail()));
            Date date = new Date(System.currentTimeMillis());
            // creating userLoginEvent Param
            UserLoginParam userLoginParam = new UserLoginParam(user.getUsername(), date, loginRequest.getBrowser().toString(),
                    loginRequest.getLocation().toString(), loginRequest.getMouseEvent().toString(), loginRequest.getKeyBoardEvent().toString(), loginRequest.getBrowserInfo().toString());
            userLoginParamRepo.save(userLoginParam);
            browserObject browserObject = (com.example.login.payload.browserObject) loginRequest.getBrowserInfo();
            redisService.checkUserInfo(user.getId(), user.getUsername(), browserObject.getCanvasFingerPrint(), browserObject.getBrowserAttribute(), browserObject.getDeviceAttribute());
            logger.info("==== browserInfo ====" + browserObject.getBrowserAttribute() + " " + browserObject.getCanvasFingerPrint() + " " + browserObject.getDeviceAttribute());
            if (authenticationMethod.equals("OTP")) {
                return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, "Event is Stored", authenticationMethod, user.getUsername()));
            } else if (authenticationMethod.equals("security_question")) {
                return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, "Event is Stored", authenticationMethod, user.getUsername()));
            } else {

                return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, "Event is Stored", "normal", user.getUsername()));
            }
        }

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/secondLogin")
    public ResponseEntity<?> secondLogin(@Valid @RequestBody AdaptiveAuthRequest adaptiveAuthRequest) {
        logger.info("----Adaptive-----" + adaptiveAuthRequest.getUsername(), adaptiveAuthRequest.getQuestion(), adaptiveAuthRequest.getAnswer());

        if (userRepository.existsByUsername(adaptiveAuthRequest.getUsername()) || userRepository.existsByEmail(adaptiveAuthRequest.getUsername())) {
            User user = userRepository.findByUsernameOrEmail(adaptiveAuthRequest.getUsername(), adaptiveAuthRequest.getUsername())
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with username or email : " + adaptiveAuthRequest.getUsername()));
            if (adaptiveAuthRequest.getAuthFactor().equals("security_question")) {
                if (user.getQuestion().equals(adaptiveAuthRequest.getQuestion()) && user.getAnswer().toLowerCase().equals(adaptiveAuthRequest.getAnswer().toLowerCase())) {
                    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Verified"));
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Not Verified"));
                }
            } else if (adaptiveAuthRequest.getAuthFactor().equals("OTP")) {
                if (user.getCode().equals(adaptiveAuthRequest.getCode())) {
                    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Verified"));
                } else {
                    return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Not Verified"));
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Not Verified"));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword(),
                signUpRequest.getQuestion(), signUpRequest.getAnswer());

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

    @PostMapping("/generateCode")
    public ResponseEntity<?> generateOTPCode(@Valid @RequestBody GenerateCode generateCode) {

        if (userRepository.existsByUsername(generateCode.getUsername()) || userRepository.existsByEmail(generateCode.getUsername())) {

            User user = userRepository.findByUsernameOrEmail(generateCode.getUsername(), generateCode.getUsername())
                    .orElseThrow(() ->
                            new UsernameNotFoundException("User not found with username or email : " + generateCode.getUsername()));

            String response = service.generateCode(user.getEmail());
            if (response.equals("Success")) {
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Success"));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Failure"));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse(true, "Username/Email does not exsist"));
    }
}
