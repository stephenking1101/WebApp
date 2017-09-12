package com.superware.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Collection;

@Service
public class JWTAuthentication {

    @Autowired
    private JwtUtil jwtUtil;

    public boolean loginAs(String accessToken, HttpSession session) {
        session.setAttribute(ADMIN, getCurrentUser());

        UserByToken userByToken = jwtUtil.parseToken(accessToken);
        if (userByToken != null) {
            SpringUserDetailsAdapter adapter = new SpringUserDetailsAdapter(userByToken);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userByToken, userByToken.getPassword(), (Collection) adapter.getAuthorities());
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            return true;
        }
        return false;
    }

    public String loginToAdmin(HttpSession session) {
        UserByToken admin = (UserByToken) session.getAttribute(ADMIN);

        if (admin != null) {
            String token = jwtUtil.generateToken(admin);
            authentificateUserByToken(token);

            session.removeAttribute(ADMIN);

            return token;
        }

        return null;
    }

    public boolean authentificateUserByToken(String jwtToken){
        UserByToken userByToken = jwtUtil.parseToken(jwtToken);
        if (userByToken != null && userByToken.isActive()) {
            SpringUserDetailsAdapter adapter = new SpringUserDetailsAdapter(userByToken);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userByToken, userByToken.getPassword(), (Collection) adapter.getAuthorities());
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);
            return true;
        }
        return false;
    }

    public UserByToken getCurrentUser(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Object principal = securityContext.getAuthentication().getPrincipal();
        UserByToken userByToken = null;
        if (principal != null && principal instanceof UserByToken) {
            userByToken = (UserByToken)principal;
        }

        return userByToken;
    }

    public void updateCurrentUser(UserByToken userByToken){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        SpringUserDetailsAdapter adapter = new SpringUserDetailsAdapter(userByToken);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userByToken, userByToken.getPassword(), (Collection) adapter.getAuthorities());
        securityContext.setAuthentication(authentication);
    }

    public void logOut(HttpServletRequest request, HttpServletResponse response) {
        CookieClearingLogoutHandler cookieClearingLogoutHandler = new CookieClearingLogoutHandler(AbstractRememberMeServices.SPRING_SECURITY_REMEMBER_ME_COOKIE_KEY);
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        cookieClearingLogoutHandler.logout(request, response, null);
        securityContextLogoutHandler.logout(request, response, null);

        request.getSession().removeAttribute(ADMIN);
    }

    public String updateToken(UserByToken user){
        return jwtUtil.generateToken(user);
    }


    private final String ADMIN = "current_user.admin";
}
