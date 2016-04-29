package com.guru.vo.temp;

/**
 * Created by Anton on 29.04.2016.
 */
public class Account {
    private String id;
    private String login;
    private String password;
    private String pin;
    private boolean proxy = false;
    private String ip;
    private String port;
    private String proxy_login;
    private String proxy_password;

    public Account() {
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPin() {
        return this.pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isProxy() {
        return this.proxy;
    }

    public void setProxy(boolean proxy) {
        this.proxy = proxy;
    }

    public String getIp() {
        return this.ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return this.port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProxy_login() {
        return this.proxy_login;
    }

    public void setProxy_login(String proxy_login) {
        this.proxy_login = proxy_login;
    }

    public String getProxy_password() {
        return this.proxy_password;
    }

    public void setProxy_password(String proxy_password) {
        this.proxy_password = proxy_password;
    }
}

