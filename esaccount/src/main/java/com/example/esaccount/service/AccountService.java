package com.example.esaccount.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.esaccount.model.Account;
import com.example.esaccount.model.AuthenticationRequest;

import org.springframework.http.ResponseEntity;

public interface AccountService {

    String create(Account account, HttpServletResponse response, HttpServletRequest request);
    Object createMany(Iterable<Account> account, HttpServletResponse response, HttpServletRequest request);
    Object update(Account account, String id, HttpServletResponse response, HttpServletRequest request);
    Object getAll(HttpServletRequest request, HttpServletResponse response);
    Optional<Account> findId(String id, HttpServletResponse response, HttpServletRequest request);
    Optional<Account> findName(String name, HttpServletResponse response, HttpServletRequest request);
    String deleteByName(String name, HttpServletResponse response, HttpServletRequest request);
    String deleteById(String id, HttpServletResponse response, HttpServletRequest request);
    Map<String, Long> getCount(List<String> wordList);

}
