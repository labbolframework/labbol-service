/**
 * 
 */
package com.labbol.service.convert;

import javax.servlet.http.HttpServletRequest;

import com.labbol.core.queryinfo.QueryInfo;
import com.labbol.service.exception.InvalidParameterException;

/**
 * @author PengFei
 */
public class RequestBodyConvertObjects {

	private static final RequestBodyConvertObject<QueryInfo> requestyBodyConvertQueryInfo;

	static {
		requestyBodyConvertQueryInfo = new RequestBodyConvertQueryInfo();
	}

	public static final QueryInfo toQueryInfo(HttpServletRequest request) throws InvalidParameterException {
		return requestyBodyConvertQueryInfo.convert(request);
	}

}
