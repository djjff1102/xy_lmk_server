package com.wenge.datasource.base;

import cn.hutool.core.lang.Pair;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.wenge.common.constants.AuthConstants;
import com.wenge.common.util.CommonUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author HAO灏 2022/11/26 16:30
 */

public class PartitionManager {

	//Map<数据源名, Map<表名, 分区数>>
	private final static Map<String, Map<String, Integer>> datasourceTablePartitionNum = new HashMap<>();
	/**
	 * sql分区参数控制Pair<开启分区, 只禁用一次>
	 */
	private final static ThreadLocal<Pair<Boolean, Boolean>> PARTITION = ThreadLocal.withInitial(() -> Pair.of(false, false));
	//Map<数据源名, 数据库名>
	private final static Map<String, String> DATABASE = new HashMap<>();

	public static void registerDatabase(String dataSourceName, String databaseName) {
		DATABASE.put(dataSourceName, databaseName);
	}

	public static String getDatabaseName() {
		return DATABASE.get(DynamicDataSourceContextHolder.peek());
	}

	public static Map<String, String> getDatabase() {
		return DATABASE;
	}

	public static void registerPartition(String dataSourceName, String tableName, int partitionNum) {
		Map<String, Integer> tablePartitionNum = datasourceTablePartitionNum.computeIfAbsent(dataSourceName, k -> new HashMap<>());
		tablePartitionNum.put(tableName, partitionNum);
	}

	public static String getPartitionTableName(String tableName) {
		String dataSourceName = DynamicDataSourceContextHolder.peek();
		Map<String, Integer> tablePartitionNum = datasourceTablePartitionNum.get(dataSourceName);
		Integer partitionNum;
		if (CommonUtil.isEmpty(tablePartitionNum) || (partitionNum = tablePartitionNum.get(tableName)) == null) {
			return tableName;
		}
		Pair<Boolean, Boolean> p = PARTITION.get();
		//开启分区
		long tenantId = AuthConstants.CURRENT_TENANT_ID.get();
		if (p.getKey() && tenantId != -1L && partitionNum > 0) {
			return tableName + " PARTITION ( p" + (tenantId % partitionNum) + " ) ";
		}
		//只禁用一次
		if (p.getValue()) {
			PARTITION.remove();
		}
		return tableName;
	}

	public static boolean tableNameNotExist(String tableName) {
		Map<String, Integer> tableMap = datasourceTablePartitionNum.get(DynamicDataSourceContextHolder.peek());
		return CommonUtil.isEmpty(tableMap) || !tableMap.containsKey(tableName);
	}

	public static Set<String> getCurrentTableNames() {
		Map<String, Integer> tableMap = datasourceTablePartitionNum.get(DynamicDataSourceContextHolder.peek());
		if (CommonUtil.isEmpty(tableMap)) {
			return CommonUtil.emptySet();
		}
		return tableMap.keySet();
	}

	public static void disablePartitionOnce() {
		PARTITION.set(Pair.of(false, true));
	}

	public static void disablePartition() {
		PARTITION.set(Pair.of(false, false));
	}

	public static void clearPartition() {
		PARTITION.remove();
	}
}
