package com.superware.services;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.superware.domain.ApplicationUser;
import com.superware.domain.UserCredential;
import com.superware.exception.ApplicationException;

public interface AdminService {

	public boolean checkCredentials(UserCredential userCredentials);
	
	public List<String> getAllRoles();
	
	public Map<Object, Object> getAllUsers(Pageable page);
	
	public ApplicationUser getUserByName(String userName);
	
	public boolean deleteUser(String userId) throws ApplicationException;
	
	public ApplicationUser createUser(ApplicationUser user) throws ApplicationException;
	
	public ApplicationUser editUser(ApplicationUser user) throws ApplicationException;
}
