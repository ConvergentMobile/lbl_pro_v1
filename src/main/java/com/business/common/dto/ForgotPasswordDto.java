package com.business.common.dto;

public class ForgotPasswordDto {

	private Integer id;

	private String emailCode;

	private boolean validOrNot;

	private String email;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmailCode() {
		return emailCode;
	}

	public void setEmailCode(String emailCode) {
		this.emailCode = emailCode;
	}

	public boolean isValidOrNot() {
		return validOrNot;
	}

	public void setValidOrNot(boolean validOrNot) {
		this.validOrNot = validOrNot;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
