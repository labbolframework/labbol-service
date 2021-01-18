/**
 * 
 */
package com.labbol.service.convert;

import java.util.Map;

import org.yelong.support.json.gson.GsonUtils;

import com.labbol.service.exception.InvalidParameterException;

/**
 * 请求消息体转换为Map
 * 
 * @author PengFei
 * @date 2020年12月4日下午3:21:36
 */
public class RequestBodyConvertMap extends AbstractRequestBodyConvertObject<Map<String,Object>> {

	@Override
	public Map<String,Object> jsonToObject(String json) throws InvalidParameterException {
		return GsonUtils.fromJsonToMap(json);
	}

}
