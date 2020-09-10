package com.labbol.core.check.sign.exception;

import com.labbol.service.exception.CommonException;
import com.labbol.service.exception.CommonExceptionEnum;

public class SignErrorException extends CommonException {

	private static final long serialVersionUID = -3805843804045233830L;

	public SignErrorException() {
		super(CommonExceptionEnum.SERVICE_SIGN_ERROR);
	}
}
