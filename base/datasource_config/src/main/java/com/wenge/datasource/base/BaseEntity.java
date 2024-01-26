package com.wenge.datasource.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class BaseEntity implements Serializable {

	private static final long serialVersionUID = -4618435674493009857L;

	@TableField("create_date")
	private Date createDate;

	@TableField("modify_date")
	private Date modifyDate;

	@TableId(value = "id", type = IdType.ASSIGN_ID)
	private Long id;

	@TableLogic
	@TableField("delete_flag")
	private Long deleteFlag = 0L;

	@TableField(exist = false)
	private boolean allowModifySystemData = false;

}
