package com.superware.controllers;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.superware.domain.Menu;
import com.superware.exception.ApplicationException;
import com.superware.services.DataManagementService;

@Controller
public class MenuController {
	
	@Autowired
    ServletContext context; 
	
	@Autowired
	private DataManagementService dataManagementService;
	
	private final static org.slf4j.Logger logger = LoggerFactory.getLogger(MenuController.class);
    
	@RequestMapping(value = "/menus", method = RequestMethod.GET)
	public @ResponseBody Map<Object, Object> getMenus(ModelMap model, HttpServletRequest request, HttpServletResponse response) throws ApplicationException {
		Map<Object, Object> result = new HashMap<Object, Object>();
		String filePath = context.getRealPath("/"); 
		System.out.println(filePath);
		filePath = context.getRealPath("/WEB-INF/resources/data/"); 
		System.out.println(filePath);
		//MultipartFile multipartFile
		//multipartFile.transferTo(new File(filePath));
		
		List<Menu> menus = dataManagementService.getAllMenus();
		result.put("menus", menus);
		logger.debug("Propagate the menus");
		Gson gson = new Gson();
		//Gson gson = new GsonBuilder().setPrettyPrinting().create();

		// 1. Java object to JSON, and save into a file
		try (FileWriter writer = new FileWriter(filePath+"staff.json")) {

            gson.toJson(menus, writer);

        } catch (IOException e) {
            e.printStackTrace();
        }

		// 2. Java object to JSON, and assign to a String
		String jsonInString = gson.toJson(menus);
		logger.debug(jsonInString);
		
		try (Reader reader = new FileReader(filePath+"staff.json")) {
			// 1. JSON to Java object, read it from a file.
			Menu staff = gson.fromJson(reader, Menu.class);
			logger.debug(staff.getName());

			// 2. JSON to Java object, read it from a Json String.
			jsonInString = "{'name' : 'mkyong'}";
			staff = gson.fromJson(jsonInString, Menu.class);
			logger.debug(staff.getName());

			// JSON to JsonElement, convert to String later.
			//JsonElement json = gson.fromJson(reader, JsonElement.class);
			    
			//logger.debug(gson.toJson(json));
		} catch (IOException e) {
            e.printStackTrace();
        }
		
		/*
		//Convert a JSON Array to a List, using TypeToken
		String json = "[{\"name\":\"mkyong\"}, {\"name\":\"laplap\"}]";
		List<Menu> list = gson.fromJson(json, new TypeToken<List<Menu>>(){}.getType());
		list.forEach(x -> System.out.println(x));
		
		//Convert a JSON to a Map
		json = "{\"name\":\"mkyong\", \"age\":33}";
		Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
		map.forEach((x,y)-> System.out.println("key : " + x + " , value : " + y));
		*/
		return result;
	}
}
