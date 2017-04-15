package com.business.model.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author Vasanth
 * 
 *         POJO bean which creates table with the available fields in the
 *         database table on server load-up.
 * 
 */
@Entity
@Table(name="roles")
public class RoleEntity {
	@Id
	@GeneratedValue
	@Column(name="roleid")
	private Integer roleId;
	@Column(name="rolename")
	private String roleName;
	
	public Integer getRoleId() {
		return roleId;
	}
	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	

}
