package com.wenge.datasource.base.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author HAO灏 2020/8/31 10:38
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema
public class OrderBy {

	@Schema(description = "排序列名")
	private String column_name;

	@Schema(description = "是否正序")
	private boolean asc;

}
