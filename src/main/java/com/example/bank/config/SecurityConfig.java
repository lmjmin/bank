package com.example.bank.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.bank.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.sessionManagement(session -> 
	        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

	    http.csrf(csrf -> csrf.disable());
	    http.formLogin(form -> form.disable());
	    http.httpBasic(AbstractHttpConfigurer::disable);

	    http.authorizeHttpRequests(auth -> auth
	    		.requestMatchers("/**").permitAll()
//	            .requestMatchers("/login", "/regist", "/signup", "/js/**", "/css/**","/loginForm","/regForm","/").permitAll()
//	            .requestMatchers("/admin/**").hasRole("ADMIN")
//	            .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
//	            .anyRequest().authenticated()
	    
	    );

	    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

	    return http.build();
	}
}
