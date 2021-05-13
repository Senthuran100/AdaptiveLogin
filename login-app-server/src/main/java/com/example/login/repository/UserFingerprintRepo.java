package com.example.login.repository;

import com.example.login.model.UserFingerprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserFingerprintRepo {

    public static final String HASH_KEY = "UserFingerprint";
    @Autowired
    private RedisTemplate template;

    public UserFingerprint save(UserFingerprint userFingerprint){
        template.opsForHash().put(HASH_KEY,userFingerprint.getUsername(),userFingerprint);
        System.out.println("Record Saved");
        return userFingerprint;
    }

    public List<UserFingerprint> findAll(){
        return template.opsForHash().values(HASH_KEY);
    }

    public UserFingerprint findProductById(int id){
        return (UserFingerprint) template.opsForHash().get(HASH_KEY,id);
    }


    public String deleteProduct(int id){
        template.opsForHash().delete(HASH_KEY,id);
        return "UserFingerprint removed !!";
    }

}
