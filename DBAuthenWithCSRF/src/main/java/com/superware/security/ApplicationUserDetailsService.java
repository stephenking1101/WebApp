package com.superware.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.superware.domain.ApplicationUser;
import com.superware.domain.Role;
import com.superware.domain.User;
import com.superware.repositories.UsersRepository;

@Service("applicationUserDetailsService")
public class ApplicationUserDetailsService implements UserDetailsService{

	@Autowired
	private UsersRepository usersRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = usersRepository.findByName(username);
		
		if(user != null){
			ApplicationUser applicationUser = new ApplicationUser();
			applicationUser.setId(Long.toString(user.getId()));
			applicationUser.setUsername(user.getName());
			applicationUser.setPassword(user.getPassword());
			
			List<GrantedAuthority> groupAuthorities = new ArrayList<GrantedAuthority>();
			for (Role authority : user.getRoles()) {
				groupAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
			}
			applicationUser.setAuthorities(groupAuthorities);
			return applicationUser;
		} else {
			throw new UsernameNotFoundException("No user present with user name: " + username);
		}
	}

}
