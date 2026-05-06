package br.com.avsistems.dto.request;

public class UserCredentials {
    public String email;
    public String password;

    public UserCredentials() {}

    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }
}