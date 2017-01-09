package com.superware.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.superware.domain.ApplicationUser;
import com.superware.domain.UserCredential;
import com.superware.exception.ApplicationException;
import com.superware.services.AdminService;

@Controller
@RequestMapping(value = "/admin")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping
    public String getAdminPage(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = ((ApplicationUser)principal).getUsername();
		
		model.addAttribute("username", userName);
		model.addAttribute("currentMenu", "admin/users");
		return "admin";
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
    public @ResponseBody Map<Object, Object> authorize(@RequestBody UserCredential userCredentials) throws ApplicationException {
		Map<Object, Object> result = new HashMap<Object, Object>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = ((ApplicationUser)principal).getUsername();
		boolean isValid = adminService.checkCredentials(userCredentials);

		if(isValid){
			result.put("username", userName);
			result.put("currentMenu", "admin/users");
		} else {
			//result.put("error", "User name or password is incorrect!");
			throw new ApplicationException("You are not an admin. User name or password is incorrect!");
		}		
		return result;
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users")
    public String getUsersPage(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = ((ApplicationUser)principal).getUsername();
		
		model.addAttribute("username", userName);
		model.addAttribute("currentMenu", "admin/users");
		return "admin";
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/maps")
    public String getMapsPage(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = ((ApplicationUser)principal).getUsername();
		
		model.addAttribute("username", userName);
		model.addAttribute("currentMenu", "admin/maps");
		return "admin";
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/list")
    public @ResponseBody Map<Object, Object> getUsers(@RequestBody ApplicationUser user, 
    		@PageableDefault(value=15, sort={"id"}, direction=Sort.Direction.ASC) Pageable pageable) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = ((ApplicationUser)principal).getUsername();
		
		result.put("username", userName);
		result.put("currentMenu", "admin/users");
		
		int totalPages = 1;
		int currentPage = 1;
		String userInput = user.getUsername();
		ArrayList<ApplicationUser> userList = new ArrayList<ApplicationUser>();
		//System.out.println(pageable.getPageNumber());
		//System.out.println(pageable.getPageSize());
		if(StringUtils.isEmpty(userInput.trim())){
			Map<Object, Object> userPages = adminService.getAllUsers(pageable);
			userList = (ArrayList<ApplicationUser>) userPages.get("user");
			totalPages = (int) userPages.get("totalPages");
			currentPage = (int) userPages.get("currentPage");
		} else {
			ApplicationUser userR = adminService.getUserByName(userInput.trim());
			if(userR!=null) {
				userList.add(userR);
			} else {
				result.put("error", true);
				result.put("message", "No user was found for user [" + userInput + "] !");
			}
		}
		
		result.put("users", userList);
		result.put("totalPages", totalPages);
		result.put("currentPage", currentPage);
		result.put("roles", adminService.getAllRoles());
        return result;
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/delete")
    public @ResponseBody Map<Object, Object> deleteUser(@RequestBody ApplicationUser user) throws ApplicationException {
		Map<Object, Object> result = new HashMap<Object, Object>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = ((ApplicationUser)principal).getUsername();
		
		result.put("username", userName);
		result.put("currentMenu", "admin/users");
		
		String userId = user.getId();
		//if(StringUtils.isEmpty(userId.trim())){
		if(adminService.deleteUser(userId)){
			result.put("message", "success");
		} /*else {
				throw new Exception("Failed to delete user with ID [" + userId + "] !");
			}
		} else {
			throw new Exception("User ID [" + userId + "] not found !");
		}*/
		
        return result;
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/add")
    public @ResponseBody Map<Object, Object> addUser(@RequestBody ApplicationUser user) throws ApplicationException {
		Map<Object, Object> result = new HashMap<Object, Object>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = ((ApplicationUser)principal).getUsername();
		
		result.put("username", userName);
		result.put("currentMenu", "admin/users");
		
		if(adminService.createUser(user) != null){
			result.put("message", "success");
			result.put("user", user);
		} /*else {
			throw new Exception("Failed to add user [" + user.getUsername() + "] !");
		}*/
		
        return result;
    }
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/users/update")
    public @ResponseBody Map<Object, Object> updateUser(@RequestBody ApplicationUser user) throws ApplicationException {
		Map<Object, Object> result = new HashMap<Object, Object>();
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = ((ApplicationUser)principal).getUsername();
		
		result.put("username", userName);
		result.put("currentMenu", "admin/users");
		
		if(adminService.editUser(user) != null){
			result.put("message", "success");
			result.put("user", user);
		} /*else {
			throw new Exception("Failed to edit user [" + user.getUsername() + "] !");
		}*/
		
        return result;
    }
	/*
	private PageRequest buildPageRequest(int pageNumber, int pageSize, String sortCol){
		Sort sort = new Sort(Direction.ASC, sortCol);
		return new PageRequest(pageNumber - 1, pageSize, sort);
	}*/
}
