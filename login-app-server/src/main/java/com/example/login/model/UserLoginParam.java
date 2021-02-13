package com.example.login.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "user_login_params")
public class UserLoginParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String username;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern="dd-MM-yyyy HH:mm:ss")
    private Date datetime;

    @Column(columnDefinition = "TEXT")
    private String browser;

    @Column(columnDefinition = "TEXT")
    private String location;

    @Column(columnDefinition = "TEXT")
    private String mouseevent;

    @Column(columnDefinition = "TEXT")
    private String keyboardevent;

    public UserLoginParam(@NotBlank @Size(max = 50) String username, Date datetime, String browser, String location,
                          String mouseevent, String keyboardevent) {
        this.username = username;
        this.datetime = datetime;
        this.browser = browser;
        this.location = location;
        this.mouseevent = mouseevent;
        this.keyboardevent = keyboardevent;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMouseevent() {
        return mouseevent;
    }

    public void setMouseevent(String mouseevent) {
        this.mouseevent = mouseevent;
    }

    public String getKeyboardevent() {
        return keyboardevent;
    }

    public void setKeyboardevent(String keyboardevent) {
        this.keyboardevent = keyboardevent;
    }
}
