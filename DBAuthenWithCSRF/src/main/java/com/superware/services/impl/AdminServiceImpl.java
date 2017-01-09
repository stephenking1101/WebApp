package com.superware.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.superware.domain.ApplicationUser;
import com.superware.domain.Role;
import com.superware.domain.UserCredential;
import com.superware.domain.User;
import com.superware.exception.ApplicationException;
import com.superware.repositories.RolesRepository;
import com.superware.repositories.UsersRepository;
import com.superware.services.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private RolesRepository rolesRepository;
	
	@Override
	public boolean checkCredentials(UserCredential userCredentials) {
		User user = usersRepository.findByName(userCredentials.getUsername());
		
		if(user != null){
			for(Role role : user.getRoles()){
				if(role.getName().equals("ROLE_ADMIN")){
					return new BCryptPasswordEncoder().matches(userCredentials.getPassword(), user.getPassword());
				}
			}
		}

		return false;
	}

	@Override
	public Map<Object, Object> getAllUsers(Pageable page) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		Page<User> userPage = usersRepository.findAll(page);
		List<User> userList = userPage.getContent();
		List<ApplicationUser> userVO = new ArrayList<ApplicationUser>();
		
		for(User user : userList) {
			ApplicationUser applicationUser = new ApplicationUser();
			applicationUser.setId(Long.toString(user.getId()));
			applicationUser.setUsername(user.getName());
			applicationUser.setPassword(user.getPassword());
			
			List<GrantedAuthority> groupAuthorities = new ArrayList<GrantedAuthority>();
			for (Role authority : user.getRoles()) {
				groupAuthorities.add(new SimpleGrantedAuthority(authority.getName()));
			}
			applicationUser.setAuthorities(groupAuthorities);
			userVO.add(applicationUser);
		}
		result.put("user", userVO);
		result.put("totalPages", userPage.getTotalPages());
		result.put("currentPage", userPage.getNumber() + 1);
		
		return result;
	}

	@Override
	public ApplicationUser getUserByName(String userName) {
		User user = usersRepository.findByName(userName);
		
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
		} 

		return null;
	}

	@Override
	@Transactional
	public boolean deleteUser(String userId) throws ApplicationException {
		try{
			usersRepository.delete(Long.valueOf(userId));
		} catch(IllegalArgumentException e) {
			throw new ApplicationException("User ID must not be null. Failed to delete user with ID [" + userId + "] !");
		}

		return true;
	}

	@Override
	@Cacheable(value = "roles",key = "\"role_all\"")
	public List<String> getAllRoles() {
		List<Role> roleList = (List<Role>) rolesRepository.findAll();;
		List<String> result = new ArrayList<String>();
		
		for(Role role : roleList) {
			result.add(role.getName());
		}
		return result;
	}

	@Override
	@Transactional
	public ApplicationUser createUser(ApplicationUser user) throws ApplicationException {
		if(StringUtils.isEmpty(user.getUsername())){
			throw new ApplicationException("Please input username!");
		}
		if(StringUtils.isEmpty(user.getPassword())){
			throw new ApplicationException("Please input password!");
		}
		if(user.getRoles() == null || user.getRoles().isEmpty()){
			throw new ApplicationException("Please choose the roles!");
		}
		if(usersRepository.findByName(user.getUsername()) != null){
			throw new ApplicationException("Username " + user.getUsername() + " has been used! Please use another name.");
		}
		User userSave = new User();
		userSave.setName(user.getUsername());
		userSave.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
		List<Role> roles = new ArrayList<Role>();
		for(String roleName : user.getRoles()){
			Role role = rolesRepository.findByName(roleName);
			roles.add(role);
		}
		userSave.setRoles(roles);
		if(usersRepository.save(userSave) == null){
			throw new ApplicationException("Failed to add user [" + user.getUsername() + "] !");
		}
		user.setId(Long.toString(userSave.getId()));
		return user;
	}

	@Override
	@Transactional
	public ApplicationUser editUser(ApplicationUser user) throws ApplicationException {
		if(StringUtils.isEmpty(user.getId())){
			throw new ApplicationException("No user selected!");
		}
		if(user.getRoles() == null || user.getRoles().isEmpty()){
			throw new ApplicationException("Please choose the roles!");
		}
		User userSave = usersRepository.findOne(Long.valueOf(user.getId()));
		if(userSave != null){
			//userSave.setName(user.getUsername());
			if(StringUtils.isNotEmpty(user.getPassword())){
				userSave.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
			}
			List<Role> roles = new ArrayList<Role>();
			for(String roleName : user.getRoles()){
				Role role = rolesRepository.findByName(roleName);
				roles.add(role);
			}
			userSave.setRoles(roles);
			if(usersRepository.save(userSave) == null){
				throw new ApplicationException("Failed to edit user [" + user.getUsername() + "] !");
			}
		}
		
		return user;
	}
}
