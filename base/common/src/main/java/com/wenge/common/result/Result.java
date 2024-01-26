package com.wenge.common.result;


import com.wenge.common.exception.BusinessException;
import com.wenge.common.util.CommonUtil;
import com.wenge.common.util.MessageUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

@Getter
@Setter
@Schema
public class Result<T> implements Serializable {


	private static final long serialVersionUID = 1061966325414442493L;

	@Schema(description = "数据")
	private T data;

	@Schema(description = "信息")
	private String message;

	@Schema(description = "结果：0：失败；1：成功；2：警告")
	private int result;

	@Schema(description = "成功计数")
	private Integer success_count;

	@Schema(description = "失败计数")
	private Integer fail_count;

	@Schema(description = "错误码")
	private String code;

	/**
	 * 异常类型：1：系统异常；2：业务异常
	 */
	private Integer fail_type;

	public Result() {
	}

	public Result(int result) {
		this.result = result;
	}

	private Result(T data, String message, String[] args, int result, Integer successCount, Integer failCount, String code, Integer failType) {
		this.data = data;
		this.setMessage(message, args);
		this.result = result;
		this.success_count = successCount;
		this.fail_count = failCount;
		this.code = code;
		this.fail_type = failType;
	}

	public static <T> Result<T> fail(String message) {
		return new Result<>(null, message, null, ResultType.FAIL, null, null, null, null);
	}

	public static <T> Result<T> fail(int failCount) {
		return new Result<>(null, null, null, ResultType.FAIL, null, failCount, null, null);
	}

	public static <T> Result<T> fail(String message, String code) {
		return new Result<>(null, message, null, ResultType.FAIL, null, null, code, null);
	}

	public static <T> Result<T> fail(String message, String code, String[] args) {
		return new Result<>(null, message, args, ResultType.FAIL, null, null, code, null);
	}

	public static <T> Result<T> fail(String message, String code, String[] args, Integer fail_type) {
		return new Result<>(null, message, args, ResultType.FAIL, null, null, code, fail_type);
	}

	public static <T> Result<T> fail(String message, T data) {
		return new Result<>(data, message, null, ResultType.FAIL, null, null, null, null);
	}

	public static <T> Result<T> fail(String message, Integer failType) {
		return new Result<>(null, message, null, ResultType.FAIL, null, null, null, failType);
	}

	public static <T> Result<T> fail(String message, String code, T data) {
		return new Result<>(data, message, null, ResultType.FAIL, null, null, code, null);
	}

	public static <T> Result<T> fail(String message, String[] args, T data) {
		return new Result<>(data, message, args, ResultType.FAIL, null, null, null, null);
	}

	public static <T> Result<T> fail(String message, String[] args, T data, String code) {
		return new Result<>(data, message, args, ResultType.FAIL, null, null, code, null);
	}

	public static <T> Result<T> success() {
		return new Result<>(ResultType.SUCCESS);
	}

	public static <T> Result<T> success(int successCount) {
		return new Result<>(null, null, null, ResultType.SUCCESS, successCount, null, null, null);
	}

	public static <T> Result<T> success(T data) {
		return new Result<>(data, null, null, ResultType.SUCCESS, null, null, null, null);
	}

	public static <T> Result<T> success(T data, String message) {
		return new Result<>(data, message, null, ResultType.SUCCESS, null, null, null, null);
	}

	public static <T> Result<T> success(T data, String message, String[] args) {
		return new Result<>(data, message, args, ResultType.SUCCESS, null, null, null, null);

	}

	public static <T> Result<T> warn(String message) {
		return new Result<>(null, message, null, ResultType.WARN, null, null, null, null);
	}

	public static <T> Result<T> warn(String message, T data) {
		return new Result<>(data, message, null, ResultType.WARN, null, null, null, null);
	}

	public static <T> Result<T> warn(String message, T data, int successCount, int failCount) {
		return new Result<>(data, message, null, ResultType.WARN, null, null, null, null);
	}

	public static <T> Result<T> warn(int successCount, int failCount) {
		return new Result<>(null, null, null, ResultType.WARN, successCount, failCount, null, null);
	}

	public static <T> Result<T> warn(String message, String[] args, T data) {
		return new Result<>(data, message, args, ResultType.WARN, null, null, null, null);
	}

	public Result<T> ifFailed(Consumer<Result<T>> consumer) {
		if (this.failed()) {
			consumer.accept(this);
		}
		return this;
	}

	public Result<T> ifFailed0(Consumer<T> consumer) {
		return this.ifFailed(result -> consumer.accept(this.data));
	}

	public void failedThenThrow() {
		this.ifFailed(result -> {
			if (result.getFail_type() != null && result.getFail_type().equals(ResultType.FAIL_TYPE_SYSTEM)) {
				throw new RuntimeException(result.getMessage());
			}
			throw new BusinessException(result.getMessage(), result.getCode());
		});
	}

	public void failedThenThrow0(Function<T, String> consumer) {
		this.ifFailed0(data -> {
			throw new BusinessException(consumer.apply(data));
		});
	}

	public Result<T> ifSucceeded(Consumer<Result<T>> consumer) {
		if (this.succeeded()) {
			consumer.accept(this);
		}
		return this;
	}

	public Result<T> filter(Predicate<T> predicate) {
		if (!predicate.test(this.data)) {
			this.setResult(ResultType.FAIL);
		}
		return this;
	}

	public Result<T> ifSucceeded0(Consumer<T> consumer) {
		return this.ifSucceeded(result -> consumer.accept(this.data));
	}

	public boolean succeeded() {
		return this.result == ResultType.SUCCESS;
	}

	public boolean failed() {
		return this.result == ResultType.FAIL;
	}

	public boolean warned() {
		return this.result == ResultType.WARN;
	}

	public void setMessage(String message) {
		this.setMessage(message, null);
	}

	public void setMessage(String message, String[] args) {
		if (CommonUtil.isBlank(message)) {
			return;
		}
		this.message = MessageUtil.getMessage(message, args);
	}

}
