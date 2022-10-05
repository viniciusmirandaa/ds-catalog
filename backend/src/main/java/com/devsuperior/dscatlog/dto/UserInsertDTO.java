package com.devsuperior.dscatlog.dto;

import java.io.Serializable;

import com.devsuperior.dscatlog.services.validation.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserInsertDTO() {
		super();
	}
}
