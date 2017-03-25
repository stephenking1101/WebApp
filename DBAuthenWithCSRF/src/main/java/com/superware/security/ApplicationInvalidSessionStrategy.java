package com.superware.security;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.MediaType;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UrlPathHelper;

public class ApplicationInvalidSessionStrategy implements InvalidSessionStrategy {
	
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String resourcePath = new UrlPathHelper().getPathWithinApplication(request);
		//System.out.println("Session timeout when access: " + resourcePath);
		String currentMenu = request.getParameter("currentMenu");
		//System.out.println("Current menu is: " + currentMenu);
		if(StringUtils.hasText(currentMenu)){
			resourcePath = "/" + currentMenu;
		}
		String time = "";
		Date lastTime = null;
		for(Cookie cookie : request.getCookies()){
			if(cookie.getName().equals("lastlogintime")){
				time = cookie.getValue();
				break;
			}
		}
		if(StringUtils.hasText(time)){
			//System.out.println("Last login time is : " + URLDecoder.decode(time, "UTF-8"));
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				lastTime = dateFormat.parse(URLDecoder.decode(time, "UTF-8"));
			} catch (ParseException e) {
			}
		}
		//create new session
		HttpSession session = request.getSession();
		
		if (isAjax(request)){
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().append(String.format("{\"error\":\"%s\"}", "SESSION_TIME_OUT"));
			//response.sendError(HttpServletResponse.SC_FORBIDDEN, "Your session has been timeout!");
		} else if( !resourcePath.equals("/") 
				&& !resourcePath.equals("/login")
				&& !resourcePath.equals("/loginfailed")
				&& !resourcePath.equals("/sessiontimeout")
				&& lastTime != null){
					session.setAttribute("currentMenu", resourcePath);
					long diff = new Date().getTime() - lastTime.getTime();
					long diffHours = diff/(60 * 60 * 1000);
					//System.out.println("Last login is " + diffHours + " hours ago");
					if (diffHours > 12){
						redirectStrategy.sendRedirect(request, response, "/login");
					} else {
						redirectStrategy.sendRedirect(request, response, "/sessiontimeout");
					}
		} else {
			redirectStrategy.sendRedirect(request, response, "/login");
		}
	}
	
	private boolean isAjax(HttpServletRequest request){
		String header = request.getHeader("X-Requested-With");
		//System.out.println(header);
		return "XMLHttpRequest".equals(header);
	}
}
