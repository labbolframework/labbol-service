package com.labbol.service.exception;

public class SignErrorException extends CommonException {

	private static final long serialVersionUID = -3805843804045233830L;

	public SignErrorException() {
		super(CommonExceptionEnum.SERVICE_SIGN_ERROR);
	}
}
