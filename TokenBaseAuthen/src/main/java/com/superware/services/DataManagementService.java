package com.superware.services;

import java.util.List;

import com.superware.domain.Menu;
import com.superware.exception.ApplicationException;

public interface DataManagementService {
	
	public List<Menu> getAllMenus() throws ApplicationException;
	
}
