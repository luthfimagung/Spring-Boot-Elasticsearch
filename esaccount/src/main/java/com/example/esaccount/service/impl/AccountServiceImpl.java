package com.example.esaccount.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.esaccount.model.Account;
import com.example.esaccount.repositories.AccountRepository;
import com.example.esaccount.service.AccountService;
import com.example.esaccount.service.StatusService;
import com.example.esaccount.util.JwtUtil;

// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.apache.spark.api.java.JavaRDD;
// import org.apache.spark.api.java.JavaSparkContext;
// import org.elasticsearch.index.query.BoolQueryBuilder;
// import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.data.domain.Sort;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
// import org.springframework.web.bind.annotation.RequestBody;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Service
public class AccountServiceImpl implements AccountService {

    // @Autowired
    // JavaSparkContext sc;
    
    @Autowired
    private StatusService statusService;
    
    private AccountRepository accountRepo;

    @Autowired
    public void setAccountRepository(AccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    @Override
    public String create(Account account, HttpServletResponse response, HttpServletRequest request) {
        boolean accountIdExists = accountRepo.existsById(account.getId());
        
        if (!accountIdExists) {
            if (accountRepo.findByName(account.getName()).isEmpty()) {
                accountRepo.save(account);
                response.setStatus(HttpServletResponse.SC_CREATED);
                return "Account created.";
            } else
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            return "Account with name '" + account.getName() + "' is already exists. Please use another name.";
        } else
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return "Account with id '" + account.getId() + "' is already exists. Please use another Id.";
    }

    @Override
    public Object createMany(Iterable<Account> account, HttpServletResponse response, HttpServletRequest request) {
        for (Account a : account) {
            while ((accountRepo.findById(a.getId()).isPresent()) || (accountRepo.findByName(a.getName()).isPresent())) {
                if (accountRepo.findById(a.getId()).isPresent()) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    return "Account with id '" + a.getId() + "' is already exists. Please use another id.";
                }

                if (accountRepo.findByName(a.getName()).isPresent()) {
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    return "Account with name '" + a.getName() + "' is already exists. Please use another name.";
                }
            }
        }
        response.setStatus(HttpServletResponse.SC_CREATED);
        return accountRepo.saveAll(account);
    }

    @Override
    public Object update(Account account, String id, HttpServletResponse response, HttpServletRequest request) {
        Account accountDetail = accountRepo.findByid(id);
        
        statusService.setDefaultStatus();
        if (account.getStatus() == false) {
            if (accountRepo.findById(id).isPresent()) {
                if ((account.getName() == null || account.getName() == "")
                        && (account.getPassword() == null || account.getPassword() == "")
                        && (account.getEmail() == null || account.getEmail() == "")) {

                    accountDetail.setName(accountDetail.getName());
                    accountDetail.setPassword(accountDetail.getPassword());
                    accountDetail.setEmail(accountDetail.getEmail());

                } else {
                    if (account.getName() == null || account.getName() == "") {
                        accountDetail.setName(accountDetail.getName());
                    } else
                        accountDetail.setName(account.getName());

                    if (account.getPassword() == null || account.getPassword() == "") {
                        accountDetail.setPassword(accountDetail.getPassword());
                    } else
                        accountDetail.setPassword(account.getPassword());

                    if (account.getEmail() == null || account.getEmail() == "") {
                        accountDetail.setEmail(accountDetail.getEmail());
                    } else
                        accountDetail.setEmail(account.getEmail());
                }
                accountRepo.save(accountDetail);
                response.setStatus(HttpServletResponse.SC_OK);
                return accountDetail;
            } else
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "Account not found.";
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "Account is not active.";
    }

    @Override
    public Object getAll(HttpServletRequest request, HttpServletResponse response) {
        Iterable<Account> listIterableAccount = null;
        List<Account> arrayListAccount = new ArrayList<>();
        
        statusService.setDefaultStatus();
        try {
            listIterableAccount = accountRepo.findAll();

            for (Account acc : listIterableAccount) {
                arrayListAccount.add(acc);
            }
        } catch (Exception e) {

        }
        return arrayListAccount;
        // return jwtTokenUtil.extractUsername(request.getHeader("Authorization").substring(7));
    }

    @Override
    public Optional<Account> findId(String id, HttpServletResponse response, HttpServletRequest request) {
        Account account = accountRepo.findByid(id);
        
        statusService.setDefaultStatus();
        if (account.getStatus() == false) {
            if (accountRepo.existsById(id)) {
                response.setStatus(HttpServletResponse.SC_OK);
                return accountRepo.findById(id);
            } else
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return null;
    }

    @Override
    public Optional<Account> findName(String name, HttpServletResponse response, HttpServletRequest request) {
        Account account = accountRepo.findByname(name);
        
        statusService.setDefaultStatus();
        if (account.getStatus() == false) {
            if (accountRepo.findByName(name).isPresent()) {
                response.setStatus(HttpServletResponse.SC_OK);
                return accountRepo.findByName(name);
            } else
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return null;
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return null;
    }

    @Override
    public String deleteByName(String name, HttpServletResponse response, HttpServletRequest request) {
        Account account = accountRepo.findByname(name);
        
        statusService.setDefaultStatus();
        if (account.getStatus() == false) {
            if (accountRepo.findByName(name).isPresent()) {
                response.setStatus(HttpServletResponse.SC_OK);
                accountRepo.deleteByName(name);
                return "Account '" + name + "' deleted.";
            } else
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "Account '" + name + "' not found.";
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "Account is not active.";
    }

    @Override
    public String deleteById(String id, HttpServletResponse response, HttpServletRequest request) {
        Account account = accountRepo.findByid(id);
        
        statusService.setDefaultStatus();
        if (account.getStatus() == false) {
            if (accountRepo.findById(id).isPresent()) {
                response.setStatus(HttpServletResponse.SC_OK);
                accountRepo.deleteById(id);
                return "Account '" + id + "' deleted.";
            } else
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return "Account '" + id + "' not found.";
        }
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return "Account is not active.";
    }

    @Override
    public Map<String, Long> getCount(List<String> wordList) {
        // JavaRDD<String> words = sc.parallelize(wordList);
        // Map<String, Long> wordCounts = words.countByValue();
        // return wordCounts;
        return null;
    }
}
