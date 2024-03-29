package com.example.login.service;

import com.example.login.controller.AuthController;
import com.example.login.model.UserFingerprint;
import com.example.login.repository.UserFingerprintRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class RedisService {

    Logger logger = LoggerFactory.getLogger(RedisService.class);

    @Autowired
    private UserFingerprintRepo userFingerprintRepo;

    // Method used to check if the fingerprint exsist in the redis cache.
    public String checkUserInfo(Long userID, String username, Long CanvasFingerprint, Long BrowserFingerprint, Long DeviceFingerprint) {
        if (userFingerprintRepo.findFingerprintByUsername(username) != null) {
            logger.info("Username found in redis");
            if (!userFingerprintRepo.findFingerprintByUsername(username).getDeviceFingerprint().contains(DeviceFingerprint.toString()) ||
                    !userFingerprintRepo.findFingerprintByUsername(username).getCanvasFingerprint().contains(CanvasFingerprint.toString()) ||
                    !userFingerprintRepo.findFingerprintByUsername(username).getBrowserFingerprint().contains(BrowserFingerprint.toString())) {
                UserFingerprint userFingerprint = userFingerprintRepo.findFingerprintByUsername(username);
                userFingerprint.getCanvasFingerprint().add(CanvasFingerprint.toString());
                userFingerprint.getDeviceFingerprint().add(DeviceFingerprint.toString());
                userFingerprint.getBrowserFingerprint().add(BrowserFingerprint.toString());

                userFingerprintRepo.save(userFingerprint);
                return "Fingerprint Doesn't Exsist";
            }
            return "Fingerprint Exsist";
        } else {
            Set<String> canvasFingerprint = new HashSet<String>();
            Set<String> browserFingerprint = new HashSet<String>();
            Set<String> deviceFingerprint = new HashSet<String>();

            canvasFingerprint.add(CanvasFingerprint.toString());
            browserFingerprint.add(BrowserFingerprint.toString());
            deviceFingerprint.add(DeviceFingerprint.toString());

            UserFingerprint userFingerprint = new UserFingerprint(userID, username, canvasFingerprint, browserFingerprint, deviceFingerprint);
            userFingerprintRepo.save(userFingerprint);
            logger.info("Data inserted in Redis Cache");
            return "Fingerprint inserted in Redis Cache";
        }
    }


}
