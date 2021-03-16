package com.example.esaccount.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = ".account")
public class Account {
    
    @Id
    private String id;
    private String name;
    private String password;
    private String email;
    private String token;
    private Boolean status = false;

    public Account(){
    }

    public Account(String id, String name, String password, String email, String token, Boolean status){
        this.id = id;
        this.name = name;
        this.password = password;
        this.email = email;
        this.token = token;
        this.status = status;
    }

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
