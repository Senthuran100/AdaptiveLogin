package com.example.login.payload;

import javax.validation.constraints.NotBlank;


public class LoginRequest {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;

    private Object browser;

    private Object location;

    private Object mouseEvent;

    private Object keyBoardEvent;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Object getBrowser() {
        return browser;
    }

    public void setBrowser(Object browser) {
        this.browser = browser;
    }

    public Object getLocation() {
        return location;
    }

    public void setLocation(Object location) {
        this.location = location;
    }

    public Object getMouseEvent() {
        return mouseEvent;
    }

    public void setMouseEvent(Object mouseEvent) {
        this.mouseEvent = mouseEvent;
    }

    public Object getKeyBoardEvent() {
        return keyBoardEvent;
    }

    public void setKeyBoardEvent(Object keyBoardEvent) {
        this.keyBoardEvent = keyBoardEvent;
    }
}
