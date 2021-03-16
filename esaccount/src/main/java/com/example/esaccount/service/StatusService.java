package com.example.esaccount.service;

import javax.servlet.http.HttpServletRequest;

import com.example.esaccount.model.Account;
import com.example.esaccount.repositories.AccountRepository;
import com.example.esaccount.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Service
public class StatusService {
    
    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private AccountRepository accountRepo;

    public void setDefaultStatus() {
        Iterable<Account> allAccount = accountRepo.findAll();

        for (Account acc : allAccount) {
            try {
                if (acc.getToken() != null) {
                    if (jwtTokenUtil.isTokenExpired(acc.getToken())) {
                        acc.setToken(null);
                        acc.setStatus(false);
                        accountRepo.save(acc);
                    }
                }
            } catch (ExpiredJwtException e) {
                acc.setToken(null);
                acc.setStatus(false);
                accountRepo.save(acc);
            } catch (SignatureException e) {
                acc.setToken(null);
                acc.setStatus(false);
                accountRepo.save(acc);
            } catch (Exception e){
                acc.setToken(null);
                acc.setStatus(false);
                accountRepo.save(acc);
            }
        }
    }
}
