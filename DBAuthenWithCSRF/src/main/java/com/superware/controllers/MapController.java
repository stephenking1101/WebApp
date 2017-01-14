package com.superware.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.superware.domain.ApplicationUser;

@Controller
public class MapController {

	@RequestMapping(value = "/maps")
    public String getMapsPage(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = ((ApplicationUser)principal).getUsername();
		
		model.addAttribute("username", userName);
		model.addAttribute("currentMenu", "maps");
		return "maps";
    }
}
