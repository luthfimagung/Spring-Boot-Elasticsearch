package com.example.esaccount.config;

import com.example.esaccount.filters.JwtRequestFilter;
import com.example.esaccount.service.AccountService;
import com.example.esaccount.service.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    // @Autowired
    // private AccountService accountService;

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        // auth.inMemoryAuthentication()
        //     .withUser("user1")
        //     .password(passwordEncoder().encode("user1pass"))
        //     .roles("USER")
        //     .and()
        //     .withUser("admin")
        //     .password(passwordEncoder().encode("adminpass"))
        //     .roles("ADMIN");
        auth.userDetailsService(myUserDetailsService);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception { 
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // return new BCryptPasswordEncoder();
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
            .authorizeRequests()
            // .antMatchers("/findId/**").hasRole("USER")
            // .antMatchers("/findName/**").hasRole("USER")
            // .antMatchers("/deleteId/**").permitAll()
            // .antMatchers("/deleteName/**").permitAll()
            // .antMatchers("/update/**").permitAll()
            // .antMatchers("/create").permitAll()
            // .antMatchers("/createMany").permitAll()
            .antMatchers("/all").permitAll()
            // .antMatchers("/wordcount").permitAll()
            // .antMatchers("/login").permitAll()
            // .antMatchers("/logoutToken").permitAll()
            .antMatchers("/authenticate").permitAll()
            .anyRequest().authenticated()
            .and().sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            // .and().formLogin();

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }
}