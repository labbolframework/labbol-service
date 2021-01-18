package com.labbol.service.exception;

import org.yelong.core.annotation.Nullable;

/**
 * 用户令牌异常
 * 
 * @author PengFei
 */
public class AuthTokenErrorException extends CommonException {

	private static final long serialVersionUID = 6553504553233041958L;

	@Nullable
	private final String token;

	public AuthTokenErrorException() {
		this(null);
	}

	public AuthTokenErrorException(String token) {
		super(CommonExceptionEnum.AUTH_TOKEN_ERROR);
		this.token = token;
	}

}
