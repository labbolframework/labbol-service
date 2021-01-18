/**
 * 
 */
package com.labbol.service.exception;

/**
 * 通用异常
 * 
 * @author PengFei
 */
public class CommonException extends ServiceException {

	private static final long serialVersionUID = -4567453170966801859L;

	public CommonException(String code, String msg) {
		this(code, msg, msg);
	}

	public CommonException(String code, String msg, String message) {
		super(code, msg, message);
	}

	/**
	 * 
	 * @param commonException 异常类型
	 * @param message         异常消息
	 */
	public CommonException(CommonExceptionEnum commonException, String message) {
		super(commonException.getErrorResponse(), message);
	}

	public CommonException(CommonExceptionEnum commonException, String message, Throwable t) {
		super(commonException.getErrorResponse(), message, t);
	}

	/**
	 * @param commonException 异常类型
	 */
	public CommonException(CommonExceptionEnum commonException) {
		super(commonException.getErrorResponse());
	}

	/**
	 * @param commonException 异常类型
	 */
	public CommonException(CommonExceptionEnum commonException, Throwable t) {
		super(commonException.getErrorResponse(), t);
	}

}
