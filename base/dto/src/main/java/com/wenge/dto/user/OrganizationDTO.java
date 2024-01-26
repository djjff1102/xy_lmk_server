package com.wenge.dto.user;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class OrganizationDTO implements Serializable {

	private static final long serialVersionUID = 775698308666766358L;
	private long id;

	private String code;

	private String name;

	private OrganizationDTO parent;

	private OrganizationDTO child;

	private int type;

	private Integer systemType;

	private String remark;
}
