package com.superware.controllers;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.superware.domain.ApplicationUser;

@Controller
public class HomeController {
    
	@RequestMapping(value = "/home")
    public String getHomePage(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = ((ApplicationUser)principal).getUsername();
		for(GrantedAuthority au : ((UserDetails)principal).getAuthorities()){
			System.out.println(au.getAuthority());
		}
		
		model.addAttribute("username", userName);
		model.addAttribute("currentMenu", "home");
        return "home";
    }
}
