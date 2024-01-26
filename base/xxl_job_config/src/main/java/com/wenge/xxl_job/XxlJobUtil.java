package com.wenge.xxl_job;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.wenge.common.constants.AuthConstants;
import com.wenge.common.exception.BusinessException;
import com.wenge.common.util.CommonUtil;
import com.wenge.common.util.SpringUtil;
import com.wenge.redis.Lock;
import com.xxl.job.core.biz.model.ReturnT;

import java.net.URI;
import java.util.Map;

/**
 * @author HAO灏 2020/12/23 20:45
 */
public class XxlJobUtil {

	private final static XxlJobService xxlJobService;

	private final static String AUTHOR = "quesoar";
	private final static String APP_NAME;

	private final static String TOKEN;
	private final static URI ADMIN_ADDRESSES;

	static {
		xxlJobService = SpringUtil.getBean(XxlJobService.class);
		XxlJobConfig config = SpringUtil.getBean(XxlJobConfig.class);
		assert config != null;
		TOKEN = config.getToken();
		ADMIN_ADDRESSES = URI.create(config.getAdminAddresses());
		APP_NAME = config.getAppName();
	}


	public static XxlJobInfo getJobInfo(String jobDesc, String executorHandler, XxlJobGroup xxlJobGroup) {
		CommonUtil.requireNonNull(executorHandler);
		Map<String, Object> getJobInfoResult = xxlJobService.getJobInfo(ADMIN_ADDRESSES, TOKEN,
				CommonUtil.ofMap("jobGroup", xxlJobGroup.getId(),
						"jobDesc", jobDesc + "-" + AuthConstants.CURRENT_TENANT_ID.get(),
						"executorHandler", executorHandler,
						"triggerStatus", -1,
						"author", AUTHOR));
		JSONObject result = new JSONObject(getJobInfoResult);
		JSONArray data = result.getJSONArray("data");
		if (CommonUtil.isEmpty(data)) {
			return null;
		}
		return data.getObject(0, XxlJobInfo.class);
	}

	public static void saveOrUpdateJobInfo(String executorHandler, String scheduleType, String scheduleConf, String param) throws Exception {
		saveOrUpdateJobInfo(executorHandler, executorHandler, scheduleType, scheduleConf, param);
	}

	private static void addJobInfo(String jodDesc, String executorHandler, String scheduleType, String scheduleConf, String param, XxlJobGroup xxlJobGroup) throws Exception {
		CommonUtil.requireNonNull(scheduleConf);
		CommonUtil.requireNonNull(scheduleType);
		CommonUtil.requireNonNull(executorHandler);
		try (Lock lock = new Lock(executorHandler)) {
			XxlJobInfo xxlJobInfo = new XxlJobInfo();
			xxlJobInfo.setJobGroup(xxlJobGroup.getId());
			xxlJobInfo.setJobDesc(jodDesc + "-" + AuthConstants.CURRENT_TENANT_ID.get());
			xxlJobInfo.setExecutorRouteStrategy("ROUND");
			xxlJobInfo.setExecutorTimeout(0);
			xxlJobInfo.setExecutorFailRetryCount(0);
			xxlJobInfo.setAuthor(AUTHOR);
			xxlJobInfo.setExecutorParam(param);
			xxlJobInfo.setScheduleType(scheduleType);
			xxlJobInfo.setScheduleConf(scheduleConf);
			xxlJobInfo.setMisfireStrategy("DO_NOTHING");
			xxlJobInfo.setGlueType("BEAN");
			xxlJobInfo.setExecutorHandler(executorHandler);
			xxlJobInfo.setExecutorBlockStrategy("DISCARD_LATER");
			ReturnT<?> addResult = xxlJobService.addJobInfo(ADMIN_ADDRESSES, TOKEN, xxlJobInfo);
			checkReturnT(addResult);
			ReturnT<?> startResult = xxlJobService.start(ADMIN_ADDRESSES, TOKEN, CommonUtil.toInt(addResult.getContent()));
			checkReturnT(startResult);
		}
	}

	public static void removeJobInfo(String executorHandler) throws Exception {
		removeJobInfo(executorHandler, executorHandler);
	}

	public static void removeJobInfo(String jobDesc, String executorHandler) throws Exception {
		CommonUtil.requireNonNull(executorHandler);
		try (Lock lock = new Lock(executorHandler)) {
			XxlJobInfo jobInfo = getJobInfo(jobDesc, executorHandler, getJobGroup());
			if (jobInfo == null) {
				return;
			}
			ReturnT<?> removeResult = xxlJobService.removeJobInfo(ADMIN_ADDRESSES, TOKEN, jobInfo.getId());
			checkReturnT(removeResult);
		}
	}

	public static void saveOrUpdateJobInfo(String jobDesc, String executorHandler, String scheduleType, String scheduleConf, String param) throws Exception {
		CommonUtil.requireNonNull(scheduleConf);
		CommonUtil.requireNonNull(scheduleType);
		CommonUtil.requireNonNull(executorHandler);
		XxlJobGroup xxlJobGroup = getJobGroup();
		XxlJobInfo jobInfo = getJobInfo(jobDesc, executorHandler, xxlJobGroup);
		if (jobInfo == null) {
			addJobInfo(jobDesc, executorHandler, scheduleType, scheduleConf, param, xxlJobGroup);
		} else if (!jobInfo.getScheduleType().equals(scheduleType) || !jobInfo.getScheduleConf().equals(scheduleConf)) {
			try (Lock lock = new Lock(executorHandler)) {
				jobInfo.setScheduleType(scheduleType);
				jobInfo.setScheduleConf(scheduleConf);
				jobInfo.setExecutorParam(param);
				jobInfo.setGlueUpdatetime(null);
				jobInfo.setAddTime(null);
				jobInfo.setUpdateTime(null);
				ReturnT<?> updateResult = xxlJobService.updateJobInfo(ADMIN_ADDRESSES, TOKEN, jobInfo);
				checkReturnT(updateResult);
			}
		}
	}


	private static void checkReturnT(ReturnT<?> returnT) {
		if (returnT.getCode() == ReturnT.FAIL_CODE) {
			throw new BusinessException(returnT.getMsg());
		}
	}

	private static XxlJobGroup getJobGroup() {
		Map<String, Object> getJobGroupResult = xxlJobService.getGroup(ADMIN_ADDRESSES, TOKEN, CommonUtil.ofMap("appname", APP_NAME));
		JSONObject result = new JSONObject(getJobGroupResult);
		JSONArray data = result.getJSONArray("data");
		if (CommonUtil.isEmpty(data)) {
			throw new BusinessException("xxl-job:please add job group");
		}
		return data.getObject(0, XxlJobGroup.class);
	}


}
