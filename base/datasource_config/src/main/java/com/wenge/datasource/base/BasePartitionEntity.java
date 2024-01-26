package com.wenge.datasource.base;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wenge.common.constants.AuthConstants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePartitionEntity extends BaseEntity {

	private static final long serialVersionUID = 6664920298133452684L;

	@TableField("tenant_id")
	private long tenantId = AuthConstants.CURRENT_TENANT_ID.get();
}
