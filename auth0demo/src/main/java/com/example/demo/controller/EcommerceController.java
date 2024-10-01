package com.example.demo.controller;



import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
@Controller
public class EcommerceController {

    @GetMapping("/home")
    public String home() {
    	System.out.println("home");
            return "home";  
    }

    @GetMapping("/home1")
    public String home1(@AuthenticationPrincipal OidcUser oidcUser) {
    	String email = oidcUser.getEmail();
    	if(email.endsWith("@divein.com")) {
    		return "redirect:/seller-dashboard";
    	}
            return "home1";  
    }
    @GetMapping("/categories")
    public String categories() {
        return "categories"; 
    }

    @GetMapping("/cart")
    public String cart() {
        return "cart";  
    }
    @GetMapping("/viewAllProducts")
    public String products() {
        return "productmanagement";  
    }

    @GetMapping("/orders")
    public String orders() {
        return "orders";  
    }

    @GetMapping("/login")
    public String login() {
        return "login";  
    }
    @RequestMapping("/buyer-dashboard")
    public String buyerDashboardJsp() {
        return "buyer-dashboard";  
    }
    @GetMapping("/seller-dashboard")
    public String sellerDashboardJsp() {
        return "seller-dashboard";  
    }
 

    
    @GetMapping("/profile")
    public String getProfile(Model model, @AuthenticationPrincipal OidcUser oidcUser , HttpSession session) {
//        // Get authenticated user details
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        
//        if (principal instanceof UserDetails) {
//            String email = ((UserDetails) principal).getUsername(); // This should be the email
//            model.addAttribute("email", email);
//        } else {
//            model.addAttribute("email", "anonymousUser");
//        }

    	model.addAttribute("profile", oidcUser.getClaims());
    	if (oidcUser.getUserInfo() != null) {
            OidcUserInfo userInfo = oidcUser.getUserInfo();
            System.out.println("User Info Claims: " + userInfo.getClaims());
            System.out.println("User Email: " + userInfo.getEmail());  // Example to get the email
            System.out.println("User Name: " + userInfo.getFullName()); 
            System.out.println("User Name: " + userInfo.getGender()); // Example to get the name
        } else {
            System.out.println("No UserInfo available");
        }
    
        // Print specific claims from OidcIdToken
        OidcIdToken idToken = oidcUser.getIdToken();
        System.out.println("ID Token Claims: " + idToken.getClaims());
        System.out.println("ID Token Subject: " + idToken.getSubject()); 
        Collection<? extends GrantedAuthority> authorities = oidcUser.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            System.out.println("Role: " + authority.getAuthority());
        }        session.setAttribute("user_id", idToken.getSubject());
        String user_id = idToken.getSubject(); // or whatever you need to do with user_id
        System.out.println(user_id);
        System.out.println("ID Token Issuer: " + idToken.getIssuer()); 
        System.out.println(idToken.getTokenValue());
    	
    	
        return "profile"; // Return the view name
    }

}

