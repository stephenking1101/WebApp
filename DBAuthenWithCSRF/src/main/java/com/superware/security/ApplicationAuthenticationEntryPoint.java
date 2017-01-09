package com.superware.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

public class ApplicationAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		if (isAjax(request)){
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().append(String.format("{\"error\":\"%s\",\"message\":\"You don't have permission to access this resource (Your session may have been timeout) ! Please login with authorized user.\"}", "ACCESS_DENIED"));
		} else {
			redirectStrategy.sendRedirect(request, response, "/login");
		}
	}

	private boolean isAjax(HttpServletRequest request){
		String header = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equals(header);
	}
}
