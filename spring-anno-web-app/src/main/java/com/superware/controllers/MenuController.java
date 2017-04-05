package com.superware.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.superware.exception.ApplicationException;
import com.superware.services.DataManagementService;

@Controller
public class MenuController {
	@Autowired
	private DataManagementService dataManagementService;
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MenuController.class);
    
	@RequestMapping(value = "/menus", method = RequestMethod.GET)
	public @ResponseBody Map<Object, Object> getMenus(ModelMap model) throws ApplicationException {
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		result.put("menus", dataManagementService.getAllMenus());
		logger.debug("Propagate the menus");
		return result;
	}
}
