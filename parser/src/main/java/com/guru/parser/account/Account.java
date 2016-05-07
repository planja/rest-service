package com.guru.parser.account;

/**
 * Created by Никита on 06.05.2016.
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

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the name
     */
    public String getPin() {
        return pin;
    }

    /**
     * @param name the name to set
     */
    public void setPin(String pin) {
        this.pin = pin;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the proxy
     */
    public boolean isProxy() {
        return proxy;
    }

    /**
     * @param proxy the proxy to set
     */
    public void setProxy(boolean proxy) {
        this.proxy = proxy;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the proxy_login
     */
    public String getProxy_login() {
        return proxy_login;
    }

    /**
     * @param proxy_login the proxy_login to set
     */
    public void setProxy_login(String proxy_login) {
        this.proxy_login = proxy_login;
    }

    /**
     * @return the proxy_password
     */
    public String getProxy_password() {
        return proxy_password;
    }

    /**
     * @param proxy_password the proxy_password to set
     */
    public void setProxy_password(String proxy_password) {
        this.proxy_password = proxy_password;
    }

}

