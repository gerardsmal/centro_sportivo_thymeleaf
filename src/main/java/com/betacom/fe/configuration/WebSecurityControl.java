package com.betacom.fe.configuration;


import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatchers;

@Configuration
public class WebSecurityControl {
	
	 @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
			http.authorizeHttpRequests((requests) -> requests
					.requestMatchers("/admin", "/admin/**").hasRole("ADMIN")  // url with admin only for user admin
					.requestMatchers("/", "/registra", "/saveNuovoUtente", "/login").permitAll()                         // other url are not protected
					.anyRequest().authenticated()                             // all request must be autyentificated
			)
			.formLogin((form) -> form
					.loginPage("/login")                                      // url di login
					.permitAll()                                              // login url is always available
					)
			.logout((logout) -> logout.permitAll());
			
			return http.build();
		}
	
	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
//	@Bean
//  UserDetailsService userDetailsService() {
//	
//	List<UserDetails> userDetailsList = new ArrayList<UserDetails>();
//	
//	
//	
//	UserDetails user = 
//			User.withUsername("user")
//				.password(passwordEncoder().encode("pwd").toString())
//				.roles("USER")
//				.build();
//	
//	UserDetails admin = 
//			User.withUsername("admin")
//				.password(passwordEncoder().encode("admin").toString())
//				.roles("ADMIN")
//				.build();
//	
//	userDetailsList.add(user);
//	userDetailsList.add(admin);
//	
//	return new InMemoryUserDetailsManager(userDetailsList);
//}

	
	
	
		
			

}
