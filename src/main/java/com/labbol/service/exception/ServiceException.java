/**
 * 
 */
package com.labbol.service.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

/**
 * 接口服务异常根类
 * 
 * @author PengFei
 * @since 1.0
 */
public class ServiceException extends RuntimeException implements ErrorResponseSupplier {

	private static final long serialVersionUID = 2863583512099677644L;

	/** 异常信息根节点 */
	public static final String ERROR_RESPONSE_ROOT_NODE = "error_response";

	/** 异常代码节点 */
	public static final String ERROR_CODE_NODE = "code";

	/** 异常信息节点 */
	public static final String ERROR_MSG_NODE = "msg";

	protected static final Logger LOGGER = LoggerFactory.getLogger(ServiceException.class);

	private ErrorResponse errorResponse;

	public ServiceException(ServiceException errorResponseException) {
		this(errorResponseException.getErrorResponse());
	}

	public ServiceException(String code, String msg) {
		this(new ErrorResponse(code, msg));
	}

	public ServiceException(ErrorResponse errorResponse) {
		super(errorResponse.getMsg());
		this.errorResponse = errorResponse;
	}

	/**
	 * @param errorResponse 信息
	 * @param message       异常信息
	 */
	public ServiceException(ErrorResponse errorResponse, String message) {
		super(message);
		this.errorResponse = errorResponse;
	}

	public ServiceException(ErrorResponse errorResponse, String message, Throwable t) {
		super(message, t);
		this.errorResponse = errorResponse;
	}

	/**
	 * @param code    异常代码
	 * @param msg     异常代码的消息
	 * @param message 异常信息
	 */
	public ServiceException(String code, String msg, String message) {
		this(new ErrorResponse(code, msg), message);
	}

	/**
	 * @param errorResponseSupplier 错误响应信息提供者
	 */
	public ServiceException(ErrorResponseSupplier errorResponseSupplier) {
		this(errorResponseSupplier.getErrorResponse());
	}

	/**
	 * @param errorResponseSupplier 错误响应信息提供者
	 */
	public ServiceException(ErrorResponseSupplier errorResponseSupplier, String message) {
		this(errorResponseSupplier.getErrorResponse(), message);
	}

	/**
	 * 发生了意外的异常
	 */
	public ServiceException(ErrorResponse errorResponse, Throwable throwable) {
		super(throwable);
		this.errorResponse = errorResponse;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}

	/**
	 * 获取异常消息json字符<br/>
	 * <p>
	 * 异常响应：<br/>
	 * 接口异常响应采用JSON格式：<br/>
	 * <span style = 'color : blue'>{<br/>
	 * "error_response": {<br/>
	 * "code":"customer.invalid-parameter", <br/>
	 * "msg":"非法参数"<br/>
	 * }<br/>
	 * }<br/>
	 * </span>
	 * </p>
	 * 
	 * @author PengFei
	 * @date 2019年7月17日下午1:06:34
	 */
	public String getErrorResponseJson() {
//		LOGGER.debug(this.getMessage());
		Map<String, Object> response = new HashMap<String, Object>();
		response.put(ERROR_RESPONSE_ROOT_NODE, this.errorResponse);
		response.put("error_msg", this.getMessage());// 测试用
		return new Gson().toJson(response);
	}

	/**
	 * @return 异常代码
	 */
	@Override
	public String getCode() {
		return errorResponse.getCode();
	}

	/**
	 * @return 异常消息
	 */
	@Override
	public String getMsg() {
		return errorResponse.getMsg();
	}

}
