/*package com.superware.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/").hasAnyRole("ADMIN","USER").
		and().formLogin().
		and().csrf().disable();
	}
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		  auth.inMemoryAuthentication().withUser("ram").password("ram123").roles("ADMIN");
		  auth.inMemoryAuthentication().withUser("ravan").password("ravan123").roles("USER");
		  auth.inMemoryAuthentication().withUser("kans").password("kans123").roles("USER");
	}
}   */