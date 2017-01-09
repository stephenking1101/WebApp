package com.superware.config;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.session.SessionManagementFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.GenericFilterBean;

import com.superware.security.ApplicationAccessDeniedHandler;
import com.superware.security.ApplicationAuthenticationEntryPoint;
import com.superware.security.ApplicationInvalidSessionStrategy;
import com.superware.security.LoginAuthenticationSuccessHandler;
import com.superware.security.LoginFilter;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	/**
     * This filter makes sure we have an authenticated token before going ahead
     */
    @Autowired
    private GenericFilterBean loginFilter;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		//.csrf().disable()
		.addFilterBefore(this.loginFilter, UsernamePasswordAuthenticationFilter.class)
		.addFilterAfter(expiredSessionFilter(), SessionManagementFilter.class)
		.exceptionHandling()
			.accessDeniedHandler(new ApplicationAccessDeniedHandler())
			.authenticationEntryPoint(new ApplicationAuthenticationEntryPoint())
			.and()
		.authorizeRequests()
			.antMatchers("/resources/**").permitAll()
			.antMatchers("/loginfailed").permitAll()
			.antMatchers("/sessiontimeout").permitAll()
			.antMatchers("/403").permitAll()
			//.antMatchers("/").permitAll().and().authorizeRequests().antMatchers("/console/**").access("hasRole('ROLE_ADMIN')")
			.anyRequest().authenticated()
			.and()
		.formLogin()
			.loginPage("/login")
			.permitAll()
			.loginProcessingUrl("/auth")
			.and()
		.logout()                                    
        	.permitAll()
        	.logoutSuccessUrl("/login")
        	.invalidateHttpSession(true)
        	.deleteCookies("APPLICATION")
        	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
		
		//enable H2 console
		/*
		http.authorizeRequests().antMatchers("/").permitAll().and().authorizeRequests().antMatchers("/console/**").permitAll();
		http.headers().frameOptions().disable();
		http.csrf().disable();*/
		//end enable H2 console
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder());
	}
	
	@Bean(name="dataSource")
	public DriverManagerDataSource dataSource(){
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName("org.h2.Driver");
		driverManagerDataSource.setUrl("jdbc:h2:~/test");
		driverManagerDataSource.setUsername("sa");
		driverManagerDataSource.setPassword("");
		
		return driverManagerDataSource;
	}

	@Bean(name="passwordEncoder")
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#authenticationManagerBean()
     *
     * This method defines the <code>authenticationManager</code> bean used in filters.
     */
    @Override
    @Bean(name = "authenticationManager")
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean(name = "authenticationSuccessHandler")
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new LoginAuthenticationSuccessHandler("/home", false);
	}
    
    @Bean(name = "authenticationFailureHandler")
    public AuthenticationFailureHandler authenticationFailureHandler() {
		return new SimpleUrlAuthenticationFailureHandler("/loginfailed");
	}
    
    /**
     *  We do this to ensure our Filter is only loaded once into Application Context
     *
     */
    @Bean(name = "authenticationFilterRegistration")
    public FilterRegistrationBean myAuthenticationFilterRegistration(final LoginFilter filter) {
        final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(filter);
        filterRegistrationBean.setEnabled(false);
        return filterRegistrationBean;
    }
    
    private Filter expiredSessionFilter(){
    	SessionManagementFilter filter = new SessionManagementFilter(new HttpSessionSecurityContextRepository());
    	filter.setInvalidSessionStrategy(new ApplicationInvalidSessionStrategy());
    	return filter;
    }
    
    @Bean
    public ServletContextInitializer servletContextInitializer() {
		return new ServletContextInitializer(){

			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
				servletContext.getSessionCookieConfig().setName("APPLICATION");
			}
		};
	}
}
