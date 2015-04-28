package com.business.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author Vasanth
 * POJO bean which creates table with the available fields in the
 * database table on server load-up.
 *
 */
@Entity
@Table(name="forgotpassword")
public class ForgotPasswordEntity {

	@Id
	@GeneratedValue
	private Integer id;
	@Column(name = "emailcode")
	private String emailCode;
	@Column(name = "validornot")
	private boolean validOrNot;
	@Column(name = "email")
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
