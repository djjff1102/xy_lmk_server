package com.wenge.common.config.feign;

import com.alibaba.fastjson2.JSON;
import com.wenge.common.config.log.Log;
import com.wenge.common.exception.BusinessException;
import com.wenge.common.result.Result;
import com.wenge.common.result.ResultType;
import com.wenge.common.util.CommonUtil;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import java.nio.charset.StandardCharsets;

/**
 * @author HAO灏 2020/9/30 15:05
 */
@Configuration
public class FeignErrorDecoder implements ErrorDecoder {

	@Override
	public Exception decode(String methodKey, Response response) {
		String message = null;
		try {
			message = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
			if (response.status() != HttpStatus.OK.value()) {
				return new RuntimeException(message);
			}
			if (CommonUtil.isBlank(message)) {
				return new BusinessException(message);
			}
			Result<?> result = JSON.parseObject(message, Result.class);
			if (!result.succeeded() && result.getFail_type() != null && result.getFail_type().equals(ResultType.FAIL_TYPE_SYSTEM)) {
				return new RuntimeException(result.getMessage());
			}
			return new BusinessException(result.getMessage(), result.getCode());
		} catch (Exception e) {
			Log.logger.warn(e.getMessage(), e);
			return e;
		}
	}
}
