package com.superware.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
	@RequestMapping(value = "/loginfailed", method = RequestMethod.GET)
    public String loginError(final ModelMap model) {
        model.addAttribute("error", "true");
        model.addAttribute("message", "Incorrect Staff ID or Password");
        return "index";
    }
	
	@RequestMapping(value = "/sessiontimeout")
    public String getHomePage(final ModelMap model, HttpServletRequest request, HttpServletResponse response) {
		String currentMenu = request.getParameter("currentMenu");
		System.out.println("Current menu is: " + currentMenu);
		if(StringUtils.hasText(currentMenu)){
			HttpSession session = request.getSession();
			session.setAttribute("currentMenu", "/" + currentMenu);
		}
		
		model.addAttribute("error", "true");
        model.addAttribute("message", "Your session has been timeout!");
        return "index";
    }
	
	@RequestMapping(value = "/403")
    public String accessDenied(final ModelMap model) {
		model.addAttribute("error", "true");
        model.addAttribute("message", "You don't have permission to access this resource!");
        return "403";
    }
}
