package com.labbol.service.exception;

/**
 * 错误响应信息提供商
 * 
 * @author PengFei
 * @date 2020年12月3日下午4:49:00
 */
public interface ErrorResponseSupplier {

	/**
	 * @author PengFei
	 * @date 2020年12月3日下午4:49:21
	 * @return 错误响应的信息
	 */
	ErrorResponse getErrorResponse();

	/**
	 * @author PengFei
	 * @date 2020年12月3日下午4:49:55
	 * @return 错误代码
	 */
	default String getCode() {
		return getErrorResponse().getCode();
	}

	/**
	 * @author PengFei
	 * @date 2020年12月3日下午4:50:02
	 * @return 错误信息
	 */
	default String getMsg() {
		return getErrorResponse().getMsg();
	}

}
