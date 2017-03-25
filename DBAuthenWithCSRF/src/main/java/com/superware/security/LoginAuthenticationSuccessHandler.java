package com.superware.security;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.StringUtils;

public class LoginAuthenticationSuccessHandler extends 
		AbstractAuthenticationTargetUrlRequestHandler implements 
		AuthenticationSuccessHandler {

	@Override
	protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
		if (isAlwaysUseDefaultTargetUrl()) {
			return getDefaultTargetUrl();
		}

		String targetUrl = (String) request.getSession().getAttribute("currentMenu");
		//System.out.println("Direct to page :" + targetUrl);
		
		if (!StringUtils.hasText(targetUrl) || targetUrl.startsWith("/resources") || targetUrl.startsWith("/admin")) {
			targetUrl = getDefaultTargetUrl();
		} 

		request.getSession().removeAttribute("currentMenu");

		return targetUrl;
	}

	public LoginAuthenticationSuccessHandler() {
	}

	/**
	 * Constructor which sets the <tt>defaultTargetUrl</tt> property of the base class.
	 * @param defaultTargetUrl the URL to which the user should be redirected on
	 * successful authentication.
	 */
	public LoginAuthenticationSuccessHandler(String defaultTargetUrl, boolean alwaysUseDefaultTargetUrl) {
		setDefaultTargetUrl(defaultTargetUrl);
		setAlwaysUseDefaultTargetUrl(alwaysUseDefaultTargetUrl);
	}
	
	@Override
	/**
	 * Calls the parent class {@code handle()} method to forward or redirect to the target
	 * URL, and then calls {@code clearAuthenticationAttributes()} to remove any leftover
	 * session data.
	 */
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date now = new Date();
		Cookie loginTime = new Cookie("lastlogintime", URLEncoder.encode(dateFormat.format(now), "UTF-8"));
		response.addCookie(loginTime);
		handle(request, response, authentication);
		clearAuthenticationAttributes(request);
	}
	
	/**
	 * Removes temporary authentication-related data which may have been stored in the
	 * session during the authentication process.
	 */
	protected final void clearAuthenticationAttributes(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
	
		if (session == null) {
			return;
		}
	
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
}
