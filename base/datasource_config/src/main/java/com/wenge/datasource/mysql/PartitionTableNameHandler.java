package com.wenge.datasource.mysql;

import com.baomidou.mybatisplus.extension.plugins.handler.TableNameHandler;
import com.wenge.datasource.base.PartitionManager;

/**
 * @author HAO灏 2022/11/26 16:30
 */
public class PartitionTableNameHandler implements TableNameHandler {


	@Override
	public String dynamicTableName(String sql, String tableName) {
		return PartitionManager.getPartitionTableName(tableName);
	}


}
