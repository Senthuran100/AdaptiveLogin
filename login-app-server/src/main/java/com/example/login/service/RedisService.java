package com.example.login.service;

import com.example.login.repository.UserFingerprintRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
    private UserFingerprintRepo userFingerprintRepo;

    public void checkUserInfo(){
        
    }


}
