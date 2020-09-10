package com.labbol.service.exception;

/**
 * @ClassName: VeryLongParameterException
 * @Description: 请求参数长度超限
 * @author Dwayne
 * @date 2020年6月22日
 *
 */
public class VeryLongParameterException extends CommonException {

	private static final long serialVersionUID = -7259531140715523361L;

	public VeryLongParameterException() {
		super(CommonExceptionEnum.INVALID_PARAMETER);
	}

	/**
	 * @param message 异常消息
	 */
	public VeryLongParameterException(String message) {
		super(CommonExceptionEnum.INVALID_PARAMETER , message);
	}

	/**
	 * @param message 异常消息
	 */
	public VeryLongParameterException(Throwable t) {
		super(CommonExceptionEnum.INVALID_PARAMETER , t);
	}

	
}
