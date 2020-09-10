/**
 * 
 */
package com.labbol.service.convert;

import javax.servlet.http.HttpServletRequest;

import com.labbol.service.exception.InvalidParameterException;

/**
 * 
 * 请求消息体转化为对象
 * 
 * @author PengFei
 */
public interface RequestBodyConvertObject<T> {

	/**
	 * 将请求消息体转换为指定的对象
	 * 
	 * @param request
	 * @return 请求消息json串转换类型对象
	 * @throws InvalidParameterException 如果这不是一个符合规范的请求消息
	 */
	T convert(HttpServletRequest request) throws InvalidParameterException;

}
