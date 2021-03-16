package com.example.esaccount.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.esaccount.model.Account;
import com.example.esaccount.model.AuthenticationRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.esaccount.service.AccountService;
import com.example.esaccount.service.LoginService;

@CrossOrigin(origins = "*")
@RestController
@Primary
public class AccountController {
    
    @Autowired
    public AccountService accountService;

    @Autowired
    public LoginService loginService;

    @PostMapping("/create")
    public String createAccount(@RequestBody Account account, HttpServletResponse response, HttpServletRequest request){
        return accountService.create(account, response, request);
    }

    @PostMapping("/createMany")
    public Object createManyAccount(@RequestBody Iterable<Account> account, HttpServletResponse response,
            HttpServletRequest request) {
        return accountService.createMany(account, response, request);
    }

    @PutMapping("/update/{id}")
    public Object updateAccount(@RequestBody Account account, @PathVariable("id") String id, 
            HttpServletResponse response, HttpServletRequest request){  
        return accountService.update(account, id, response, request);
    }
    
    @GetMapping("/findId/{id}")
    public Object findId(@PathVariable("id") String id, HttpServletResponse response, HttpServletRequest request){
        return accountService.findId(id, response, request);
    }

    @GetMapping("/findName/{name}")
    public Object findName(@PathVariable("name") String name, HttpServletResponse response,
            HttpServletRequest request) {
        return accountService.findName(name, response, request);
    }

    @DeleteMapping("/deleteId/{id}")
    public String deleteId(@PathVariable("id") String id, HttpServletResponse response, HttpServletRequest request) {
        return accountService.deleteById(id, response, request);
    }

    @DeleteMapping("/deleteName/{name}")
    public String deleteName(@PathVariable("name") String name, HttpServletResponse response,
            HttpServletRequest request) {
        return accountService.deleteByName(name, response, request);
    }

    @GetMapping("/all")
    public Object getAllAccount(HttpServletRequest request, HttpServletResponse response) {
        return accountService.getAll(request, response);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return loginService.createAuthenticationToken(authenticationRequest);
    }

    @GetMapping("/login")
    public Object login(@RequestBody Account account, HttpServletResponse response, HttpServletRequest request) {
        return loginService.login(account, response, request);
    }

    @GetMapping("/logoutToken")
    public String logoutToken(HttpServletRequest request, HttpServletResponse response) {
        return loginService.logoutToken(request, response);
    }

    @GetMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception{
        return loginService.refreshToken(request);
    }

    @PostMapping("/wordcount")
    public Map<String, Long> count(@RequestParam(required = true) String words) {
        List<String> wordList = Arrays.asList(words.split("\\|"));
        return accountService.getCount(wordList);
    }

}
