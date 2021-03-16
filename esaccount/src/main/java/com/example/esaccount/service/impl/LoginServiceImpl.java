package com.example.esaccount.service.impl;

import com.example.esaccount.model.Account;
import com.example.esaccount.model.AuthenticationRequest;
import com.example.esaccount.model.AuthenticationResponse;
import com.example.esaccount.repositories.AccountRepository;
import com.example.esaccount.service.LoginService;
import com.example.esaccount.service.MyUserDetailsService;
import com.example.esaccount.service.StatusService;
import com.example.esaccount.util.JwtUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.DefaultClaims;

@Service
public class LoginServiceImpl implements LoginService {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private StatusService statusService;

    @Override
    public Object login(Account account, HttpServletResponse response, HttpServletRequest request) {
        // BoolQueryBuilder query =
        // QueryBuilders.boolQuery().must(QueryBuilders.matchQuery("id",
        // account.getId()))
        // .must(QueryBuilders.matchQuery("password", account.getPassword()));

        // Iterable<Account> accountInfo = accountRepo.search(query);
        // List<Map<String, String>> accountInfo2 = new ArrayList<>();
        // for (Account account2 : accountInfo) {
        // Map<String, String> aa = new HashMap<>();
        // aa.put("name", account2.getName());
        // aa.put("id", account2.getId());
        // aa.put("email", account2.getEmail());
        // accountInfo2.add(aa);
        // }
        // return accountInfo2;

        String idBody = account.getId();
        String passBody = account.getPassword();
        boolean accountExists = accountRepo.existsById(idBody);
        Account account2 = accountRepo.findByid(idBody);

        statusService.setDefaultStatus();
        if (account2.getStatus() == false){
            if (accountExists) {
                if ((passBody == null) && (passBody == "")) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return "Please insert your password";
                } else if (passBody.hashCode() != account2.getPassword().hashCode()) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return "Wrong password. Please check your password.";
                } else
                    account2.setStatus(true);
                    accountRepo.save(account2);
                    response.setStatus(HttpServletResponse.SC_OK);
                return "Login Success! Welcome " + account2.getName() + "!" + jwtTokenUtil.extractExpiration(account2.getToken()).toString();
            } else
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "Account not found.";
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "Account is not active.";
    }

    @Override
    public ResponseEntity<?> createAuthenticationToken(AuthenticationRequest authenticationRequest) throws Exception {
        // jwtTokenUtil.setSecretKey(authenticationRequest.getUsername());
        statusService.setDefaultStatus();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtTokenUtil.generateToken(userDetails);
        Account account = accountRepo.findByid(authenticationRequest.getUsername());
        account.setStatus(true);
        account.setToken(jwt);
        accountRepo.save(account);
        return ResponseEntity.ok(new AuthenticationResponse(jwt));

    }

    @Override
    public String logoutToken(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization").substring(7);
        String idToken = jwtTokenUtil.extractUsername(token);
        Account account = accountRepo.findByid(idToken);

        statusService.setDefaultStatus();

        if (account.getStatus() == false) {
            // account.setStatus(true);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return "Can not process because you already logged out.";
            // return "(in the block list) token block list: " + tokenBlocklist;
        } else {
            // tokenBlocklist.add(token);
            account.setToken(null);
            account.setStatus(false);
            accountRepo.save(account);
            response.setStatus(HttpServletResponse.SC_OK);
            return "Logout Successful.";
        }
    }
    
    @Override
    public ResponseEntity<?> refreshToken(HttpServletRequest request) throws Exception {
        DefaultClaims claims = (io.jsonwebtoken.impl.DefaultClaims) request.getAttribute("claims");

        statusService.setDefaultStatus();
        Map<String, Object> expectedMap = getMapFromIoJsonwebtokenClaims(claims);
        String token = jwtTokenUtil.doGenerateRefreshToken(expectedMap, expectedMap.get("sub").toString());
        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
    
    public Map<String, Object> getMapFromIoJsonwebtokenClaims(DefaultClaims claims) {
        statusService.setDefaultStatus();
        
        Map<String, Object> expectedMap = new HashMap<String, Object>();
        for (Entry<String, Object> entry : claims.entrySet()) {
            expectedMap.put(entry.getKey(), entry.getValue());
        }
        return expectedMap;
    }

}
