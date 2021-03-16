package com.example.esaccount.repositories;

// import java.util.List;
import java.util.Optional;

import com.example.esaccount.model.Account;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends ElasticsearchRepository<Account, String> {

    boolean existsByName(String name);
    String deleteByName(String name);
    Optional<Account> findByName(String name);
    Account findByid(String id);
    Account findByname(String name);
    // String findTheId(String id);
    // String findThePassword(String password);
    // Optional<Account> findById(String id);
	// Iterable<Account> findAll();
    
}
