package com.superware.config;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class CacheControlFilter implements Filter {

	private FilterConfig fc; 
	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse res = (HttpServletResponse) response;
        // set the provided HTTP response parameters
        for (Enumeration<String> e = fc.getInitParameterNames(); e.hasMoreElements();) {
            String headerName = (String) e.nextElement();
            //System.out.println(headerName);
            //System.out.println(fc.getInitParameter(headerName));
            res.addHeader(headerName, fc.getInitParameter(headerName));
        }
        // pass the request/response on
        chain.doFilter(request, response); 
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.fc = config;
	}

}
