package com.superware.services.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.superware.domain.Menu;
import com.superware.exception.ApplicationException;
import com.superware.repositories.MenuRepository;
import com.superware.services.DataManagementService;

@Service
public class DataManagementServiceImpl implements DataManagementService{

	@Autowired
	private MenuRepository menuRepository;
	
	@Override
	@Cacheable(value = "menus",key = "\"menu_all\"")
	public List<Menu> getAllMenus() throws ApplicationException {
		Menu menu = new Menu();
		menu.setName("test");
		menu.setParent("parent");
		menu.setType("type");
		menuRepository.save(menu);
		return (List<Menu>) menuRepository.findAll();
	}
	
	
}
