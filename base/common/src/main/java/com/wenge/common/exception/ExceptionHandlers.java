package com.wenge.common.exception;

import com.wenge.common.config.log.Log;
import com.wenge.common.result.Result;
import com.wenge.common.result.ResultType;
import com.wenge.common.util.CommonUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author HAO灏
 * 统一异常处理
 */
@RestControllerAdvice
public class ExceptionHandlers {

	private final static Map<Class<?>, HttpStatus> HTTP_STATUS = CommonUtil.ofMapN(
			HttpMessageNotReadableException.class, HttpStatus.BAD_REQUEST,
			MissingServletRequestParameterException.class, HttpStatus.BAD_REQUEST,
			BindException.class, HttpStatus.BAD_REQUEST,
			ConstraintViolationException.class, HttpStatus.BAD_REQUEST,
			ForbiddenException.class, HttpStatus.FORBIDDEN,
			MethodArgumentNotValidException.class, HttpStatus.BAD_REQUEST,
			UnauthorizedException.class, HttpStatus.UNAUTHORIZED,
			HttpRequestMethodNotSupportedException.class, HttpStatus.METHOD_NOT_ALLOWED,
			LockedException.class, HttpStatus.OK,
			BusinessException.class, HttpStatus.OK);

	private ResponseEntity<Result<?>> validException(Exception exception, List<FieldError> fieldErrors) {
		String exceptionMessage = "";
		if (CommonUtil.isNotEmpty(fieldErrors)) {
			exceptionMessage = fieldErrors.stream()
					//拼接加空格是因为字段名是英文，提示信息如果也是英文显示会有问题
					.map(fieldError -> fieldError.getField() + " " + fieldError.getDefaultMessage())
					.collect(Collectors.joining(","));
		}
		return this.fail(exception, exceptionMessage, ResultType.FAIL_TYPE_BUSINESS);
	}

	@ExceptionHandler(BaseException.class)
	@ResponseBody
	public ResponseEntity<Result<?>> resolveBaseException(BaseException exception) {
		return this.fail(exception, CommonUtil.toString(exception.getMessage()), exception.getCode(), exception.getArgs(), ResultType.FAIL_TYPE_BUSINESS);
	}

	@ExceptionHandler({UnauthorizedException.class})
	@ResponseBody
	public ResponseEntity<Result<?>> resolveBaseException(UnauthorizedException exception) {
		return this.fail(exception, CommonUtil.toString(exception.getMessage()), exception.getCode(), exception.getArgs(), ResultType.FAIL_TYPE_BUSINESS);
	}

	@ExceptionHandler({ForbiddenException.class})
	@ResponseBody
	public ResponseEntity<Result<?>> resolveBaseException(ForbiddenException exception) {
		return this.fail(exception, CommonUtil.toString(exception.getMessage()), exception.getCode(), exception.getArgs(), ResultType.FAIL_TYPE_BUSINESS);
	}


	@ExceptionHandler
	@ResponseBody
	public ResponseEntity<Result<?>> resolveUnknownException(Exception exception) {
		if (exception instanceof UndeclaredThrowableException
				|| exception instanceof ExecutionException) {
			exception = (Exception) exception.getCause();
			if (exception instanceof BaseException) {
				return this.resolveBaseException((BaseException) exception);
			}
		}
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		Log.logger.error(exception.getMessage(), exception);
		return ResponseEntity.status(status).body(Result.fail(status.getReasonPhrase(), ResultType.FAIL_TYPE_SYSTEM));
	}

	@ExceptionHandler({MethodArgumentNotValidException.class, BindException.class, ConstraintViolationException.class})
	@ResponseBody
	public ResponseEntity<Result<?>> resolveValidException(Exception exception) {
		List<FieldError> fieldErrors = new ArrayList<>();
		if (exception instanceof MethodArgumentNotValidException) {
			fieldErrors = ((MethodArgumentNotValidException) exception).getBindingResult().getFieldErrors();
		} else if (exception instanceof BindException) {
			fieldErrors = ((BindException) exception).getBindingResult().getFieldErrors();
		} else if (exception instanceof ConstraintViolationException) {
			String exceptionMessage = ((ConstraintViolationException) exception).getConstraintViolations().stream().map(cv -> cv.getPropertyPath() + " " + cv.getMessage()).collect(Collectors.joining(","));
			return this.fail(exception, exceptionMessage, ResultType.FAIL_TYPE_SYSTEM);
		}
		return this.validException(exception, fieldErrors);
	}


	@ExceptionHandler({HttpMessageNotReadableException.class, HttpRequestMethodNotSupportedException.class,
			MissingServletRequestParameterException.class})
	@ResponseBody
	public ResponseEntity<Result<?>> resolveKnownException(Exception exception) {
		return this.fail(exception, "", ResultType.FAIL_TYPE_SYSTEM);
	}

	private ResponseEntity<Result<?>> fail(Exception exception, String message, Integer failType) {
		return this.fail(exception, message, null, null, failType);
	}

	private ResponseEntity<Result<?>> fail(Exception exception, String message, String code, String[] args, Integer failType) {
		Log.logger.warn(exception.getMessage(), exception);
		HttpStatus status = HTTP_STATUS.get(exception.getClass());
		if (status == null) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		if (CommonUtil.isBlank(message)) {
			message = status.getReasonPhrase();
		}
		return ResponseEntity.status(status).body(Result.fail(message, code, args, failType));
	}

}
