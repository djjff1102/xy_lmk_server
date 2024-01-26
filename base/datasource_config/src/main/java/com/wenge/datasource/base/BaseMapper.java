package com.wenge.datasource.base;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.Map;

public interface BaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

	@UpdateProvider(value = BaseSqlProvider.class, method = "executeSql")
	void executeSql(String sql, @Param("params") Map<String, Object> params);
}
