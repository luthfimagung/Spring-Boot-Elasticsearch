package com.example.esaccount.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.esaccount.model.Account;
import com.example.esaccount.model.AuthenticationRequest;

import org.springframework.http.ResponseEntity;

public interface LoginService {
    
    Object login(Account account, HttpServletResponse response, HttpServletRequest request);
    ResponseEntity<?> createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception;
    String logoutToken(HttpServletRequest request, HttpServletResponse response);
    ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception;
    
}
