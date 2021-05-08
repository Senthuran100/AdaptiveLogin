package com.example.login;

import com.example.login.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.example.login.model.Role;
import com.example.login.model.RoleName;

import javax.annotation.PostConstruct;
import java.util.TimeZone; 
import com.example.login.repository.RoleRepository;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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
	private EmailSenderService service;

	@PostConstruct
	void init() {
		Boolean userRoleExists = roleRepository.findByName(RoleName.ROLE_USER).isPresent();
		Boolean userAdminExists = roleRepository.findByName(RoleName.ROLE_ADMIN).isPresent();
		if(!userRoleExists) {
			Role r=new Role();
			r.setName(RoleName.ROLE_USER);		
			roleRepository.save(r);			
		}
		if(!userAdminExists) {
			Role r=new Role();
			r.setName(RoleName.ROLE_ADMIN);		
			roleRepository.save(r);			
		}
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
	}

	public static void main(String[] args) {
		SpringApplication.run(LoginApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	public void triggerMail() throws MessagingException {

		service.sendSimpleEmail("senthuran.manoharan@gmail.com","Hello","Hello world");

	}
}
