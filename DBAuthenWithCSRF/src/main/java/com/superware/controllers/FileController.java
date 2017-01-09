package com.superware.controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.superware.domain.ApplicationUser;
import com.superware.exception.ApplicationException;
import com.superware.services.FileService;

@Controller
public class FileController {
	
	@Autowired
	private FileService fileService;

	@PreAuthorize("hasRole('ROLE_USER')")
	@RequestMapping(value = "/file")
	public String getLdapPage(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String userName = ((ApplicationUser)principal).getUsername();
		
		model.addAttribute("username", userName);
		model.addAttribute("currentMenu", "file");
        return "home";
    }
	
	@PreAuthorize("hasRole('ROLE_POWERUSER')")
	@RequestMapping(value = "/fileupload", method = RequestMethod.POST)
	public @ResponseBody Map<Object, Object> getUploadedFile(MultipartHttpServletRequest request, HttpServletResponse response) throws ApplicationException{
		//get the files from the request object
		Iterator<String> itr = request.getFileNames();
		Map<Object, Object> result = new HashMap<Object, Object>();
		
		while(itr.hasNext()){
			MultipartFile file = request.getFile(itr.next());
			
			result.put("fileContent", fileService.readExcel(file));
		}
		return result;
	}
}
