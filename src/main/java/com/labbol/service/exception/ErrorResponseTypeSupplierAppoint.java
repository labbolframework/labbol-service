/**
 * 
 */
package com.labbol.service.exception;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.yelong.core.jdbc.DataBaseOperationType;

import com.labbol.service.exception.ErrorResponseTypeSupplier.ErrorResponseType;

@Retention(RUNTIME)
@Target(METHOD)
/**
 * 指定 handlerMethod 的异常响应供应商。
 * 
 * @author PengFei
 * @date 2020年12月4日下午4:11:47
 * @see ServiceExceptionResolver
 */
public @interface ErrorResponseTypeSupplierAppoint {

	/**
	 * 指定 handlerMethod的异常响应供应商。且该类必须是一个枚举类
	 * 
	 * @author PengFei
	 * @date 2020年12月4日下午4:15:59
	 * @return 错误响应类型供应商
	 */
	Class<? extends Enum<?>> enumClass();

	/**
	 * 指定类型ID
	 * 
	 * @author PengFei
	 * @date 2020年12月4日下午4:26:36
	 * @return 类型ID
	 * @see ErrorResponseType#getId()
	 */
	String typeId();

	/**
	 * 指定处理器方法的数据操作类型
	 * 
	 * @author PengFei
	 * @date 2020年12月4日下午4:38:14
	 * @return 处理器方法的数据操作类型
	 */
	DataBaseOperationType operationType();
}
