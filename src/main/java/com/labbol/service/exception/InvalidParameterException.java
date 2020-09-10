/**
 * 
 */
package com.labbol.service.exception;

/**
 * 非法的请求参数异常
 * 
 * @author PengFei
 * @date 2019年7月17日上午11:00:49
 */
public class InvalidParameterException extends CommonException {

	private static final long serialVersionUID = -2909875477711046085L;

	public InvalidParameterException() {
		super(CommonExceptionEnum.INVALID_PARAMETER);
	}

	/**
	 * @param message 异常消息
	 */
	public InvalidParameterException(String message) {
		super(CommonExceptionEnum.INVALID_PARAMETER, message);
	}

	/**
	 * @param message 异常消息
	 */
	public InvalidParameterException(Throwable t) {
		super(CommonExceptionEnum.INVALID_PARAMETER, t);
	}

}
