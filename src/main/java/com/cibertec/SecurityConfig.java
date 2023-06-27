package com.cibertec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	@Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests()
            .antMatchers("/api/login").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/productos/").permitAll()
            .antMatchers("/productos/{id}").permitAll()
            .antMatchers("/cart").permitAll()
            .antMatchers("/cart/{id}").permitAll()
            .antMatchers("/order").permitAll()
            .antMatchers("/search").permitAll()
            .antMatchers("/administrador/usuarios").permitAll()
            .antMatchers("/administrador/ordenes").permitAll()
            .antMatchers("/administrador/detalle/{id}").permitAll()
            .anyRequest().authenticated();
    }
}
