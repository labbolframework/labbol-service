package com.labbol.service.exception;

import org.yelong.core.annotation.Nullable;
import org.yelong.core.jdbc.DataBaseOperationType;

/**
 * 异常响应类型供应商。一般由存放异常信息的枚举类实现。该接口
 * 
 * @author PengFei
 * @date 2020年12月4日下午4:07:16
 * @see ErrorResponseTypeSupplierAppoint
 */
public interface ErrorResponseTypeSupplier extends ErrorResponseSupplier {

	/**
	 * @author PengFei
	 * @date 2020年12月4日下午4:07:25
	 * @return 异常响应类型信息
	 */
	@Nullable
	ErrorResponseType getErrorResponseType();

	public static enum ErrorType {
		/** 数据保存异常 */
		DATA_SAVE,
		/** 数据修改异常 */
		DATA_MODIFY,
		/** 数据长度异常 */
		DATA_LENGTH,
		/** 数据不允许为空异常(如必填字段不允许为空异常) */
		DATA_NULL,
		/** 数据删除异常 */
		DATA_DELETE,
		/** 数据查询异常 */
		DATA_QUERY,
	}

	/**
	 * 构建错误响应类型信息。如果参数中存在一个空值则返回 <code>null</code>
	 * 
	 * @author PengFei
	 * @date 2020年12月4日下午7:24:47
	 */
	default ErrorResponseType buildErrorResponseType(String typeId, ErrorType errorType,
			DataBaseOperationType operationType) {
		if (null != typeId && null != errorType && null != operationType) {
			return new ErrorResponseType(typeId, errorType, operationType);
		} else {
			return null;
		}
	}

	public static class ErrorResponseType {

		/** 异常类型的标识，一般用于区分一个异常的枚举类中存在多种相同的类型的异常 */
		private final String id;

		private final ErrorType errorType;

		private final DataBaseOperationType operationType;

		public ErrorResponseType(String id, ErrorType errorType, DataBaseOperationType operationType) {
			this.id = id;
			this.errorType = errorType;
			this.operationType = operationType;
		}

		public String getId() {
			return id;
		}

		public ErrorType getErrorType() {
			return errorType;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof ErrorResponseType) {
				ErrorResponseType errorResponseType = (ErrorResponseType) obj;
				return id.equals(errorResponseType.id) && errorType.equals(errorResponseType.errorType)
						&& operationType.equals(errorResponseType.operationType);
			}
			return false;
		}

	}

}
