package com.superware.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.superware.config.JWTAuthentication;
import com.superware.config.RoleEntity;
import com.superware.config.RolesEnum;
import com.superware.config.UserByToken;
import com.superware.domain.AuthBody;
import com.superware.domain.VerifyUserForm;

import ch.qos.logback.classic.Logger;

@Controller
public class HomeController {
	
	private static int counter = 0;
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	private JWTAuthentication jwtAuthentication;
    
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(ModelMap model) {

		model.addAttribute("message", "Welcome");
		model.addAttribute("counter", ++counter);
		logger.debug("[welcome] counter : {}", counter);

		// Spring uses InternalResourceViewResolver and return back index.jsp
		return "index";

	}
	
	@RequestMapping(value = "/apitoken", method = RequestMethod.GET)
	public @ResponseBody Map<Object, Object> getToken(ModelMap model) {

		Map<Object, Object> result = new HashMap<Object, Object>();
		RoleEntity role = new RoleEntity();
		role.setId(0);
		role.setName(RolesEnum.user);
		
		UserByToken userByToken = new UserByToken();
		userByToken.setId(0);
		userByToken.setPassword("test");
		userByToken.setAuthorId(0);
		userByToken.setPublisherId(0);
		userByToken.setRole(role);
		userByToken.setActive(true);
		String token = jwtAuthentication.updateToken(userByToken);
		logger.debug(token);
		result.put("token", token);

		return result;
	}
	
	@RequestMapping(value = "/api/test", method = RequestMethod.GET)
	public @ResponseBody Map<Object, Object> testToken(ModelMap model) {

		Map<Object, Object> result = new HashMap<Object, Object>();
		result.put("name", "mkyong");
		return result;
	}
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public @ResponseBody String test(ModelMap model) {

		return "{'name' : 'mkyong'}";
	}
	
	@RequestMapping(value = "/sign_in", method =  RequestMethod.PUT)
    public void saveUserRole(@RequestBody AuthBody authBody,
                             HttpServletResponse response){
        boolean res = jwtAuthentication.authentificateUserByToken(authBody.getAccess_token());
        if (res)
            response.setStatus(HttpServletResponse.SC_OK);
        else
            response.setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping(value = "/login_as", method = RequestMethod.GET)
    public void loginAs(
            @RequestParam("access_token") String accessToken,
            HttpServletResponse response,
            HttpServletRequest request
    ) {
        boolean res = jwtAuthentication.loginAs(accessToken, request.getSession());
        if (res)
            response.setStatus(HttpServletResponse.SC_OK);
        else
            response.setStatus(HttpServletResponse.SC_CONFLICT);
    }

    @RequestMapping(value = "/to_admin", method = RequestMethod.GET)
    public @ResponseBody String loginToAdmin(
            HttpServletRequest request
    ) {
        return jwtAuthentication.loginToAdmin(request.getSession());
    }

    @RequestMapping(value = "/sign_out", method = RequestMethod.PUT)
    public void signOut(HttpServletResponse response, HttpServletRequest request){
        jwtAuthentication.logOut(request, response);

        response.setStatus(HttpServletResponse.SC_OK);
    }

    @PreAuthorize("!isAuthenticated()")
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signUp(){
        return "auth/signup";
    }

    @RequestMapping(value = "/{role}/{email}/verify/{code}")
    public String verifyFromLetter(
            @PathVariable("role") String role,
            @PathVariable("email") String email,
            @PathVariable("code") String code,
            HttpServletRequest request){

        VerifyUserForm verifyUserForm = new VerifyUserForm(email, code);
        request.getSession().setAttribute(VERIFY_FORM, verifyUserForm);

        return "redirect:/verify";
    }

    @PreAuthorize("!isAuthenticated()")
    @RequestMapping(value = "/verify")
    public String verify(Model model, HttpServletRequest request){
        VerifyUserForm verifyUserForm;
        try {
            verifyUserForm = (VerifyUserForm)request.getSession().getAttribute(VERIFY_FORM);
            request.getSession().removeAttribute(VERIFY_FORM);
        } catch (Exception e){
            verifyUserForm = null;
        }
        if(verifyUserForm == null){
            return "redirect:/";
        }
        model.addAttribute("email", verifyUserForm.email);
        model.addAttribute("code", verifyUserForm.code);
        return "auth/verified";
    }

    @RequestMapping(value = "/error/403", method = RequestMethod.GET)
    public String error403(){
        return "errors/403";
    }
    
    private static final String VERIFY_FORM = "verify_form";
}
