/**
 * 
 */
package com.labbol.service.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.yelong.commons.beans.BeanUtilsE;
import org.yelong.commons.lang.annotation.AnnotationUtilsE;
import org.yelong.core.model.annotation.Table;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.labbol.core.controller.BaseCoreController;
import com.labbol.core.queryinfo.QueryInfo;
import com.labbol.core.utils.ColumnSupportUtils;
import com.labbol.service.exception.InvalidParameterException;
import com.labbol.service.exception.VeryLongParameterException;

/**
 * @author PengFei
 */
public class BaseServiceController extends BaseCoreController {

	/**
	 * 查询结果转换为json字符
	 */
	protected <T> String pageInfoToJson(PageInfo<T> pageInfo) {
		Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
		jsonMap.put("total", pageInfo.getTotal());
		jsonMap.put("root", pageInfo.getList());
		return createDefaultGsonBuilder().create().toJson(jsonMap);
	}

	/**
	 * 通过指定的gson进行将pageInfo转换为json字符串
	 */
	protected <T> String pageInfoToJson(Gson gson, PageInfo<T> pageInfo) {
		Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
		jsonMap.put("total", pageInfo.getTotal());
		jsonMap.put("root", pageInfo.getList());
		return gson.toJson(jsonMap);
	}

	/**
	 * 验证请求必填参数<br/>
	 * 验证obj对象中fields是否为空来确认是否必填参数已经填上。如果没有至少一个为空将会抛出InvalidParameterException异常
	 * 
	 * @param obj
	 * @param fields
	 * @throws InvalidParameterException 如果obj对象中至少一个fields为空
	 */
	protected void validateRequestRequiredParameters(Object obj, String[] fieldNames) throws InvalidParameterException {
		try {
			for (String fieldName : fieldNames) {
				Object value = BeanUtilsE.getProperty(obj, fieldName);
				if (null == value || (value instanceof CharSequence && StringUtils.isBlank((CharSequence) value))) {
					throw new NullPointerException("字段“" + fieldName + "”值为：" + value + "，不符合的参数值！");
				}
			}
		} catch (Exception e) {
			throw new InvalidParameterException(e);
		}
	}

	/**
	 * @Title: checkAttributeValueLength @Description: 验证请求参数长度是否超限 @param @param
	 * obj 参数 @return void 返回类型 @throws
	 */
	protected void checkAttributeValueLength(Object obj) {
		try {
			if (null != obj) {
				Class<? extends Object> cls = obj.getClass();
				Table table =  AnnotationUtilsE.getAnnotation(cls, Table.class, true);
				System.out.println("校验对象中参数的数据长度是否符合要求,校验对象:" + cls.getName());
				if (null != table) {
					ColumnSupportUtils.checkValueLength(obj, cls);
				}
			}
		} catch (Exception e) {
			throw new VeryLongParameterException(e);
		}
	}

	/** 默认不能为空的queryinfo字段 */
	protected static final String[] DEFAULT_QUERY_INFO_NOT_ENTRY_FIELD = { "pageNum", "pageSize" };

	/**
	 * 验证QueryInfo对象。默认页面和页面大小不能为空
	 * 
	 * @throws InvalidParameterException 如果不满足验证条件
	 */
	protected void validateQueryInfo(QueryInfo queryInfo) throws InvalidParameterException {
		validateRequestRequiredParameters(queryInfo, DEFAULT_QUERY_INFO_NOT_ENTRY_FIELD);
	}

}
