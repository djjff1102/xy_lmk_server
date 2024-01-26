package com.wenge.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserDTO implements Serializable {
	private static final long serialVersionUID = -5418292478210206462L;

	private long id;

	private long tenantId;
	private String name;

	private String workNo;

	private String realName;

	private String phone;

	private String workPhone;

	private String password;


	private String salt;

	private Date createDate;

	private Date modifyDate;

	private Boolean isAdmin;

	private Set<Long> roleId;

	private Integer roleLevel;

	private List<String> roleName;

	private Set<String> dataPermissionCode;

	private Set<AuthorityDTO> authority;

	private OrganizationDTO organization;

	private Boolean enableFlag;


	private OrganizationDTO province;
	private OrganizationDTO city;
	private OrganizationDTO area;
	private OrganizationDTO street;
	private OrganizationDTO community;
	private Integer systemType;
	private Set<String> authorityCode;

	public Set<String> getAuthorityCode() {
		if (this.authorityCode == null && this.authority!=null) {
			this.authorityCode = this.authority.stream().map(AuthorityDTO::getCode).collect(Collectors.toSet());
		}
		return this.authorityCode;
	}



}
