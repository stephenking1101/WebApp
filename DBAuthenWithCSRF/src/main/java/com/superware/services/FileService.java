package com.superware.services;

import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.superware.exception.ApplicationException;

public interface FileService {
	
	public Map<Object, Object> readExcel(MultipartFile file) throws ApplicationException;

}
