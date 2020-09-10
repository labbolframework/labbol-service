/**
 * 
 */
package com.labbol.service.exception;

/**
 * 通用异常信息代码与说明
 * 
 * @author PengFei
 */
public enum CommonExceptionEnum {

	SERVICE_CONNECT_ERROR("common.service-connect-error", "服务网络连接异常"),
	DB_CONNECT_ERROR("common.db-connect-error", "数据库连接异常"),
	SERVICE_EXECUTE_TIMEOUT("common.service-execute-timeout", "服务执行超时"),
	SERVICE_INERNAL_ERROR("common.service-internal-error", "服务内部错误"),
	SERVICE_PERMISSON_DENIED("common.service-permisson-denied", "没有调用此服务的权限"),
	INVALID_PARAMETER("common.invalid-parameter", "非法的请求参数"), AUTH_TOKEN_ERROR("common.auth-token-error", "用户令牌异常"),
	SERVICE_AUTH_TOKEN_ERROR("common.service-auth-token-error", "服务令牌异常"),
	SERVICE_SIGN_ERROR("common.service-sign-error", "服务签名异常"),
	MISS_PARAMETER_ERROR("common.miss-parameter-error", "请求参数缺失"), INVALID_SIGN("common.invalid-sign", "非法的签名"),
	INVALID_APPKEY("common.invalid-appkey", "非法的appKey"),
	TIME_NOT_MATCH("common.time-not-match", "客户端与服务端时间不匹配（相差超过5分钟）");

	private ErrorResponse errorResponse;

	CommonExceptionEnum(String code, String msg) {
		errorResponse = new ErrorResponse(code, msg);
	}

	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}

	public String getCode() {
		return errorResponse.getCode();
	}

	public String getMsg() {
		return errorResponse.getMsg();
	}

}
