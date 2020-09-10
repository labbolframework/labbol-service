/**
 * 
 */
package com.labbol.service.convert;

import com.google.gson.Gson;
import com.labbol.core.queryinfo.QueryInfo;
import com.labbol.service.exception.InvalidParameterException;

/**
 * 解析一下格式的json字符串为QueryInfo对象
 * 
 * <pre>
  {
 		"queryInfo": {
 			"pageNum": 1, 
 			"pageSize": 20,
 			"filters": [{
 				"fieldName":"name",
 				"operator":"LIKE",
 				"fieldValue":"张"
  			}]
 		}
 	}
 * 
 * </pre>
 * 
 * @author PengFei
 */
public class RequestBodyConvertQueryInfo extends AbstractRequestBodyConvertObject<QueryInfo> {

	@Override
	public QueryInfo jsonToObject(String json) throws InvalidParameterException {
		try {
			return new Gson().fromJson(json, QueryInfoWrapper.class).getQueryInfo();
		} catch (Exception e) {
			throw new InvalidParameterException(e);
		}

	}

	public class QueryInfoWrapper {

		private QueryInfo queryInfo;

		public QueryInfo getQueryInfo() {
			return queryInfo;
		}

		public void setQueryInfo(QueryInfo queryInfo) {
			this.queryInfo = queryInfo;
		}

	}

}
