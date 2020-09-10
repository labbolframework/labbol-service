/**
 * 
 */
package com.labbol.service.exception;

/**
 * 服务内部异常
 * 
 * @author PengFei
 */
public class ServiceInernalErrorException extends CommonException {

	private static final long serialVersionUID = 9020233382858702551L;

	public ServiceInernalErrorException() {
		super(CommonExceptionEnum.SERVICE_INERNAL_ERROR);
	}

	public ServiceInernalErrorException(String message) {
		super(CommonExceptionEnum.SERVICE_INERNAL_ERROR, message);
	}

	public ServiceInernalErrorException(Throwable t) {
		super(CommonExceptionEnum.SERVICE_INERNAL_ERROR, t);
	}

}
