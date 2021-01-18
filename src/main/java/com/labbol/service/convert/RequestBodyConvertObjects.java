/**
 * 
 */
package com.labbol.service.convert;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.labbol.core.queryinfo.QueryInfo;
import com.labbol.service.exception.InvalidParameterException;

/**
 * @author PengFei
 */
public final class RequestBodyConvertObjects {

	private static final RequestBodyConvertObject<QueryInfo> requestyBodyConvertQueryInfo = new RequestBodyConvertQueryInfo();

	private static final RequestBodyConvertMap requestBodyConvertMap = new RequestBodyConvertMap();

	private RequestBodyConvertObjects() {
	}

	/**
	 * 请求消息体转换为查询信息对象
	 * 
	 * @author PengFei
	 * @date 2020年12月4日下午3:20:33
	 * @param request request
	 * @return 查询信息对象
	 */
	public static QueryInfo toQueryInfo(HttpServletRequest request) throws InvalidParameterException {
		return requestyBodyConvertQueryInfo.convert(request);
	}

	/**
	 * 请求消息体转换为Map
	 * 
	 * @author PengFei
	 * @date 2020年12月4日下午3:20:13
	 * @param request request
	 * @return Map
	 */
	public static Map<String, Object> toMap(HttpServletRequest request) throws InvalidParameterException {
		return requestBodyConvertMap.convert(request);
	}

}
