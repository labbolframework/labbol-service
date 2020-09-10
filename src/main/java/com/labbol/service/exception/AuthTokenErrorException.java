package com.labbol.service.exception;

/**
 * 用户令牌异常
 * 
 * @author PengFei
 */
public class AuthTokenErrorException extends CommonException {

	private static final long serialVersionUID = 6553504553233041958L;

	public AuthTokenErrorException() {
		super(CommonExceptionEnum.AUTH_TOKEN_ERROR);
	}

}
