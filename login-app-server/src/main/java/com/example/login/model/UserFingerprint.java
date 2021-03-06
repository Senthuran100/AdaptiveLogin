package com.example.login.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("UserFingerprint")
public class UserFingerprint implements Serializable {
    @Id
    private Long id;
    private String username;
    private Set<String> canvasFingerprint = new HashSet<String>();
    private Set<String> browserFingerprint = new HashSet<String>();
    private Set<String> deviceFingerprint = new HashSet<String>();

}
