package com.example.esaccount.service;

import java.util.ArrayList;

import com.example.esaccount.model.Account;
import com.example.esaccount.model.AuthenticationRequest;
import com.example.esaccount.repositories.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;

// import com.example.esaccount.model.Account;
// import com.example.esaccount.model.AuthenticationRequest;
// import com.example.esaccount.repositories.AccountRepository;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account _account = accountRepo.findByid(username);
        return new User(_account.getId(), _account.getPassword(), new ArrayList<>());
        // return new User("budi", "budi", new ArrayList<>());
    }
    
}
