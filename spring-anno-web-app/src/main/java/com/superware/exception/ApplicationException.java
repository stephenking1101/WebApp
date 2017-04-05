package com.superware.exception;

public class ApplicationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1327206309072493866L;

	public ApplicationException(){
		super();
	}
	
	public ApplicationException(String msg){
		super(msg);
	}
}
