package com.example.demo.config;

import org.springframework.context.annotation.Bean;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import jakarta.servlet.Filter;

@Configuration
@EnableWebSecurity
public class SecurtityConfig {

//	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//		httpSecurity.authorizeHttpRequests(auth -> {
//			try {
//				auth.requestMatchers("/home","/products","/categories").permitAll()
//						.anyRequest().authenticated().and().oauth2Login();
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		});
//		httpSecurity.formLogin(Customizer.withDefaults())
//					.oauth2Login(Customizer.withDefaults());
		
//		httpSecurity
//        .authorizeRequests()
//        .requestMatchers("/cart", "/orders").authenticated()  // Require authentication for these paths
//        .requestMatchers("/home", "/categories", "/products").permitAll()  // No authentication required
//        .and()
//        .oauth2Login()  // Use OAuth2 Login (Auth0 in this case)
//        .loginPage("/login")  // Redirect to Auth0 login page
//        .defaultSuccessUrl("/home", true);
//		return httpSecurity.build();
		
//	}
	
//	 @Bean
//	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//	        http
//	            .authorizeHttpRequests(auth -> auth
//	                .requestMatchers("/home", "/products", "/categories").permitAll() // Allow these paths
//	                .anyRequest().authenticated() // Require authentication for any other request
//	            )
//	            .oauth2Login() // Use OAuth2 Login
//	            .defaultSuccessUrl("/home", true); // Redirect to home after successful login
//
//	        return http.build();
//	    }
	
	@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers("/home", "/products","/profile", "/categories").permitAll() // Allow access to these paths
                                .requestMatchers("/cart", "/orders", "/login","/register","/profile").authenticated() // Require auth for these
                                .anyRequest().permitAll() 
                )
                .oauth2Login(login -> login 
                        .defaultSuccessUrl("/home1", true))
        		.logout(logout -> logout
        				.logoutSuccessHandler(oidcLogoutSuccessHandler()) // Custom Auth0 logout handler
        				.invalidateHttpSession(true) // Invalidate session
        				.clearAuthentication(true)
        				//.deleteCookies("JSESSIONID") // Remove session cookie
        				.logoutSuccessUrl("/home")); // Redirect to home after logout
//        		.addFilterBefore(new Auth0JwtFilter(), UsernamePasswordAuthenticationFilter.class);
        
       // http.addFilterAfter(new NoCacheFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(new Auth0JwtFilter(), UsernamePasswordAuthenticationFilter.class);
      
       
        

        return http.build();
    }
	private LogoutSuccessHandler oidcLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            String logoutUrl = "https://dev-zvtq3ma4e4zf35qf.us.auth0.com/v2/logout?client_id=8eCnCZ9J67NWJCLY1mr1Gpr8fP6wn7ep&returnTo=http://localhost:3001/home";
            response.sendRedirect(logoutUrl); // Redirect to Auth0 logout and then back to home
        };
    }
}
