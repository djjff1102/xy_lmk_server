package com.wenge.common.result;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author HAO灏 2020/8/19 11:58
 */
@Getter
@Setter
@Schema
public class PageResult<T> extends Result<List<T>> {


	private static final long serialVersionUID = 2302085159463792280L;

	@Schema(description = "总条数")
	private long total;

	public PageResult(List<T> data, long total, String message, String[] args, int result) {
		super.setResult(result);
		super.setData(data);
		super.setMessage(message, args);
		this.total = total;
	}

	public static <T> PageResult<T> success(List<T> data, long total) {
		return new PageResult<>(data, total, null, null, ResultType.SUCCESS);
	}

	public static <T> PageResult<T> success(Set<T> data) {
		return success(new ArrayList<>(data), -1);
	}

	public static <T> PageResult<T> success(List<T> data) {
		return success(data, -1);
	}

	public static <T> PageResult<T> success0() {
		return success(new ArrayList<>(), 0);
	}

	public static <T> PageResult<T> fail0(String message) {
		return new PageResult<>(null, -1, message, null, ResultType.FAIL);
	}

	public static <T> PageResult<T> fail(String message, List<T> data) {
		return new PageResult<>(data, -1, message, null, ResultType.FAIL);
	}

	public static <T> PageResult<T> fail(List<T> data) {
		return new PageResult<>(data, -1, null, null, ResultType.FAIL);
	}

	public static <T> PageResult<T> warn0(String message) {
		return new PageResult<>(null, -1, message, null, ResultType.WARN);
	}

	public static <T> PageResult<T> warn(String message, List<T> data) {
		return new PageResult<>(data, -1, message, null, ResultType.WARN);
	}

	public static <T> PageResult<T> warn(List<T> data) {
		return new PageResult<>(data, -1, null, null, ResultType.WARN);
	}
}
