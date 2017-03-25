package com.superware.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalDefaultExceptionHandler {

	public static final String DEFAULT_ERROR_VIEW = "error";
	
	@ExceptionHandler(ApplicationException.class)
	@ResponseStatus(value=HttpStatus.PRECONDITION_FAILED)
	public @ResponseBody Map<Object, Object> handleApplicationException(HttpServletRequest req, Exception ex) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		result.put("error", ex.getMessage());
		result.put("message", ex.getMessage());
	    return result;
	} 
	 
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		if (isAjax(req)){
			throw e;
		}
	    // If the exception is annotated with @ResponseStatus rethrow it and let
	    // the framework handle it.
	    // AnnotationUtils is a Spring Framework utility class.
	    if (AnnotationUtils.findAnnotation
	                (e.getClass(), ResponseStatus.class) != null)
	      throw e;

	    // Otherwise setup and send the user to a default error-view.
	    ModelAndView mav = new ModelAndView();
	    mav.addObject("exception", e);
	    mav.addObject("url", req.getRequestURL());
	    mav.setViewName(DEFAULT_ERROR_VIEW);
	    return mav;
	}
	
	private boolean isAjax(HttpServletRequest request){
		String header = request.getHeader("X-Requested-With");
		//System.out.println(header);
		return "XMLHttpRequest".equals(header);
	}
}
