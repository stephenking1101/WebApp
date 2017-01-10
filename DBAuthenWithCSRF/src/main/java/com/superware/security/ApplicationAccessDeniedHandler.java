package com.superware.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.InvalidCsrfTokenException;
import org.springframework.security.web.csrf.MissingCsrfTokenException;

public class ApplicationAccessDeniedHandler implements AccessDeniedHandler {

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		if(accessDeniedException instanceof MissingCsrfTokenException || accessDeniedException instanceof InvalidCsrfTokenException){
			if (isAjax(request)){
				response.setStatus(HttpServletResponse.SC_FORBIDDEN);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.getWriter().append(String.format("{\"error\":\"%s\"}", "SESSION_TIME_OUT"));
			} else{
				redirectStrategy.sendRedirect(request, response, "/sessiontimeout");
			}
		} else if(isAjax(request)){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().append(String.format("{\"error\":\"%s\",\"message\":\"You don't have permission to access this resource! Please login with authorized user.\"}", "ACCESS_DENIED"));
		} else {
			redirectStrategy.sendRedirect(request, response, "/403");
		}

	}
	
	private boolean isAjax(HttpServletRequest request){
		String header = request.getHeader("X-Requested-With");
		//System.out.println(header);
		return "XMLHttpRequest".equals(header);
	}

}
