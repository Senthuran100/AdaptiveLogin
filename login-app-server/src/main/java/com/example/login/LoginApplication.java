package com.example.login;

import com.example.login.model.UserFingerprint;
import com.example.login.repository.UserFingerprintRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.example.login.model.Role;
import com.example.login.model.RoleName;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import com.example.login.repository.RoleRepository;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        LoginApplication.class,
        Jsr310JpaConverters.class
})
public class LoginApplication {
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private UserFingerprintRepo userFingerprintRepo;

    @PostConstruct
    void init() {
        Boolean userRoleExists = roleRepository.findByName(RoleName.ROLE_USER).isPresent();
        Boolean userAdminExists = roleRepository.findByName(RoleName.ROLE_ADMIN).isPresent();
        if (!userRoleExists) {
            Role r = new Role();
            r.setName(RoleName.ROLE_USER);
            roleRepository.save(r);
        }
        if (!userAdminExists) {
            Role r = new Role();
            r.setName(RoleName.ROLE_ADMIN);
            roleRepository.save(r);
        }
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(LoginApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void triggerMail() throws MessagingException {
        Set<String> hs = new HashSet<String>();
        hs.add("12345677");
        hs.add("7839930003");
        UserFingerprint userFingerprint=new UserFingerprint(2L,"ABC", hs,hs,hs);
        UserFingerprint userFingerprint1=new UserFingerprint(2L,"ABC1", hs,hs,hs);
        UserFingerprint userFingerprint3=new UserFingerprint(3L,"ABC1", hs,hs,hs);


        userFingerprintRepo.save(userFingerprint);
        userFingerprintRepo.save(userFingerprint1);
        userFingerprintRepo.save(userFingerprint3);


        System.out.println(userFingerprintRepo.findAll());

    }


}
