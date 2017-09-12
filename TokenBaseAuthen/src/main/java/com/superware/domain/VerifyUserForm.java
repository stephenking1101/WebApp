package com.superware.domain;

public class VerifyUserForm {
	public String email;
    public String code;

    public VerifyUserForm(String email, String code){
        this.email = email;
        this.code = code;
    }
}
