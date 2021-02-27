package com.example.login.model;

import java.util.Date;

public class UserLoginParam {

    private Long id;

    private String username;

    private Date datetime;

    private String browser;

    private String location;

    private String mouseevent;

    private String keyboardevent;

    private String browser_info;

    public UserLoginParam(String username, Date datetime, String browser, String location,
                          String mouseevent, String keyboardevent, String browser_info) {
        this.username = username;
        this.datetime = datetime;
        this.browser = browser;
        this.location = location;
        this.mouseevent = mouseevent;
        this.keyboardevent = keyboardevent;
        this.browser_info = browser_info;
    }

    public UserLoginParam() {

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

    public String getBrowser_info() {
        return browser_info;
    }

    public void setBrowser_info(String browser_info) {
        this.browser_info = browser_info;
    }
}
