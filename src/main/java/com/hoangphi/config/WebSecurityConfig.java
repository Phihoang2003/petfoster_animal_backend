package com.hoangphi.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()  // Disable CSRF protection for simplicity.
                .authorizeRequests()
                .antMatchers("/api/register").permitAll()  // Allow public access to the register endpoint.
                .anyRequest().authenticated()  // Require authentication for all other requests.
                .and()
                .formLogin().disable();  // Disable form login.
    }
}
