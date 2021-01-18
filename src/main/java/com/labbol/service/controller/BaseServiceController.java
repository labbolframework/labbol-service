/**
 * 
 */
package com.labbol.service.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.yelong.commons.beans.BeanUtilsE;
import org.yelong.commons.lang.annotation.AnnotationUtilsE;
import org.yelong.commons.util.Dates;
import org.yelong.core.model.annotation.Table;
import org.yelong.support.json.gson.AnnotationExclusionStrategy;
import org.yelong.support.servlet.HttpServletUtils;

import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.labbol.core.check.token.RequestUserInfo;
import com.labbol.core.check.token.RequestUserInfoHolder;
import com.labbol.core.controller.BaseCoreController;
import com.labbol.core.gson.GsonSupplier;
import com.labbol.core.model.BaseModelable;
import com.labbol.core.queryinfo.QueryInfo;
import com.labbol.core.queryinfo.QueryInfos;
import com.labbol.core.service.LabbolModelService;
import com.labbol.core.utils.ColumnSupportUtils;
import com.labbol.service.exception.CommonExceptionEnum;
import com.labbol.service.exception.ErrorResponse;
import com.labbol.service.exception.ErrorResponseSupplier;
import com.labbol.service.exception.InvalidParameterException;
import com.labbol.service.exception.ServiceException;
import com.labbol.service.exception.VeryLongParameterException;

import dream.first.base.gson.GsonExclude;

/**
 * @author PengFei
 */
public abstract class BaseServiceController extends BaseCoreController {

	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseServiceController.class);

	@Resource
	protected LabbolModelService modelService;

	// ==================================================temp==================================================

	static {
		GsonBuilder gsonBuilder = GsonSupplier.getDefaultGsonBuilder();
		AnnotationExclusionStrategy annotationExclusionStrategy = new AnnotationExclusionStrategy();
		annotationExclusionStrategy.addIgnoreAnnotation(GsonExclude.class);
		gsonBuilder.addSerializationExclusionStrategy(annotationExclusionStrategy);
		GsonSupplier.setDefaultGsonBuilder(gsonBuilder);
		GsonSupplier.setDefaultGson(gsonBuilder.create());
	}

	// ==================================================validate==================================================

	/**
	 * 验证请求必填参数<br/>
	 * 验证obj对象中fieldNames是否为空来确认是否必填参数已经填上。如果没有至少一个为空将会抛出InvalidParameterException异常
	 * 
	 * @param obj        对象
	 * @param fieldNames 字段名称数组
	 * @throws InvalidParameterException 如果obj对象中至少一个fieldNames为空
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
	 *         obj 参数 @return void 返回类型 @throws
	 */
	protected void checkAttributeValueLength(Object obj) {
		try {
			if (null != obj) {
				Class<? extends Object> cls = obj.getClass();
				Table table = AnnotationUtilsE.getAnnotation(cls, Table.class, true);
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
	 */
	protected void validateQueryInfo(QueryInfo queryInfo) throws InvalidParameterException {
		validateQueryInfo(queryInfo, () -> new InvalidParameterException());
	}

	/**
	 * 验证QueryInfo对象。默认页面和页面大小不能为空
	 */
	protected void validateQueryInfo(QueryInfo queryInfo, Supplier<ServiceException> serviceExceptionSupplier) {
		if (null == queryInfo) {
			throw serviceExceptionSupplier.get();
		}
		validateRequestRequiredParameters(queryInfo, DEFAULT_QUERY_INFO_NOT_ENTRY_FIELD);
	}

	/**
	 * 验证QueryInfo对象。默认页面和页面大小不能为空
	 */
	protected void validateQueryInfo(QueryInfo queryInfo, ErrorResponseSupplier errorResponseSupplier)
			throws ServiceException {
		validateQueryInfo(queryInfo, errorResponseSupplier.getErrorResponse());
	}

	/**
	 * 验证QueryInfo对象。默认页面和页面大小不能为空
	 */
	protected void validateQueryInfo(QueryInfo queryInfo, ErrorResponse errorResponse) throws ServiceException {
		validateQueryInfo(queryInfo, () -> new ServiceException(errorResponse));
	}

	/**
	 * 验证QueryInfo必须要填写的查询字段<br/>
	 * 
	 * @date 2019年7月31日下午4:41:50
	 * @param queryInfo          查询信息
	 * @param requiredFieldNames 必须要填写的查询条件字段
	 */
	protected void validataQueryInfoQualifyFieldName(QueryInfo queryInfo, String[] requiredFieldNames)
			throws InvalidParameterException {
		validataQueryInfoQualifyFieldName(queryInfo, requiredFieldNames, CommonExceptionEnum.INVALID_PARAMETER);
	}

	/**
	 * 验证QueryInfo必须要填写的查询字段<br/>
	 * 
	 * @date 2019年7月31日下午4:41:50
	 * @param queryInfo          查询信息
	 * @param requiredFieldNames 必须要填写的查询条件字段
	 */
	protected void validataQueryInfoQualifyFieldName(QueryInfo queryInfo, String[] requiredFieldNames,
			ErrorResponseSupplier errorResponseSupplier) throws InvalidParameterException {
		if (ArrayUtils.isEmpty(requiredFieldNames)) {
			return;
		}
		List<String> filterFieldNames = QueryInfos.getFilterFieldNames(queryInfo);
		// 必填字段
		List<String> requiredFieldNameList = Arrays.asList(requiredFieldNames);
		// 验证必填字段
		if (!filterFieldNames.containsAll(requiredFieldNameList)) {
			throw new ServiceException(errorResponseSupplier,
					"必填查询条件字段缺失！必填字段：" + requiredFieldNameList + "当前传入的查询字段：" + filterFieldNames);
		}
	}

	/**
	 * @see #validataQueryInfoQualifyFieldName(QueryInfo, String[], String[],
	 *      ErrorResponseSupplier)
	 */
	protected void validataQueryInfoQualifyFieldName(QueryInfo queryInfo, String[] requiredFieldNames,
			String[] onlyAllowFieldNames) throws InvalidParameterException {
		validataQueryInfoQualifyFieldName(queryInfo, requiredFieldNames, onlyAllowFieldNames,
				CommonExceptionEnum.INVALID_PARAMETER);
	}

	/**
	 * 验证QueryInfo限制的查询字段<br/>
	 * queryInfo中的查询条件字段必须在onlyAllowFieldName字段范围内，且必须包含requiredFieldName字段
	 * 如果不符合规范则抛出InvalidParameterException
	 * 
	 * @param queryInfo          查询信息
	 * @param requiredFieldName  必须出现的字段名称 允许为空
	 * @param onlyAllowFieldName 只允许在这个字段名称范围内
	 */
	protected void validataQueryInfoQualifyFieldName(QueryInfo queryInfo, String[] requiredFieldNames,
			String[] onlyAllowFieldNames, ErrorResponseSupplier errorResponseSupplier) throws ServiceException {
		if (ArrayUtils.isEmpty(onlyAllowFieldNames)) {
			throw new ServiceException(errorResponseSupplier, "允许的字段限制不能为空！");
		}
		List<String> filterFieldNames = QueryInfos.getFilterFieldNames(queryInfo);
		if (CollectionUtils.isEmpty(filterFieldNames)) {
			throw new ServiceException(errorResponseSupplier, "queryInfo没有查询字段条件");
		}
		// 必填字段
		List<String> requiredFieldNameList = Arrays.asList(requiredFieldNames);
		// 只允许字段
		List<String> onlyAllowFieldNameList = Arrays.asList(onlyAllowFieldNames);
		// 验证必填字段
		if (!filterFieldNames.containsAll(requiredFieldNameList)) {
			throw new ServiceException(errorResponseSupplier,
					"必填查询条件字段缺失！必填字段：" + requiredFieldNameList + "当前传入的查询字段：" + filterFieldNames);
		}
		// 验证只允许字段
		if (!onlyAllowFieldNameList.containsAll(filterFieldNames)) {
			throw new ServiceException(errorResponseSupplier,
					"存在不允许的查询字段！只允许字段：" + onlyAllowFieldNameList + "当前传入的查询字段：" + filterFieldNames);
		}
	}

	/**
	 * 验证所有的值是否都是 <code>null</code>或者空白字符串
	 * 
	 * @author PengFei
	 * @date 2020年12月1日上午11:48:15
	 * @param values 需要验证的值
	 * @return <code>true</code> 所有的值都是 <code>null</code> 或者空白字符串
	 */
	protected boolean isAllNullOrBlank(Object... values) {
		for (Object value : values) {
			if (null == value) {
				continue;
			}
			if (value instanceof CharSequence) {
				if (StringUtils.isBlank((CharSequence) value)) {
					continue;
				}
			}
			return false;
		}
		return true;
	}

	// ==================================================toJson==================================================

	/**
	 * 查询结果转换为json字符
	 */
	protected <T> String pageInfoToJson(PageInfo<T> pageInfo) {
		Map<String, Object> jsonMap = new LinkedHashMap<String, Object>();
		jsonMap.put("total", pageInfo.getTotal());
		jsonMap.put("root", pageInfo.getList());
		return getGson().toJson(jsonMap);
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
	 * 将model对象转换为json { "modelName":{fieldName:fieldValue,...} }<br/>
	 * modelName默认为model名称小写
	 */
	protected String modelToJson(Object model) {
		return modelToJson(model, model.getClass().getSimpleName().toLowerCase());
	}

	/**
	 * 将model对象转换为json { "modelName":{fieldName:fieldValue,...} }<br/>
	 */
	protected String modelToJson(Object model, String modelName) {
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		jsonMap.put(modelName, model);
		return toJson(jsonMap);
	}

	/**
	 * 转换为创建结果 { "modelName":{createTime:..,id:..,...} }<br/>
	 * modelName默认为model名称小写
	 */
	protected String toJsonCreateResult(BaseModelable model) {
		return toJsonCreateResult(model, model.getClass().getSimpleName().toLowerCase());
	}

	/**
	 * 转换为创建结果 { "modelName":{createTime:..,id:..,...} }<br/>
	 */
	protected String toJsonCreateResult(BaseModelable model, String modelName) {
		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> info = new HashMap<String, Object>();
		info.put("id", model.getId());
		info.put("createTime", model.getCreateTime());
		result.put(modelName, info);

		return toJson(result);
	}

	/**
	 * 转换为修改结果 { "modelName":{updateTime:..,id:..,...} }<br/>
	 * modelName默认为model名称小写
	 */
	protected String toJsonUpdateResult(BaseModelable model) {
		return toJsonUpdateResult(model, model.getClass().getSimpleName().toLowerCase());
	}

	/**
	 * 转换为修改结果 { "modelName":{updateTime:..,id:..,...} }<br/>
	 */
	protected String toJsonUpdateResult(BaseModelable model, String modelName) {
		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> info = new HashMap<String, Object>();
		info.put("id", model.getId());
		info.put("updateTime", model.getUpdateTime());
		result.put(modelName, info);

		return toJson(result);
	}

	/**
	 * 转换为修改结果 { "modelName":{deleteTime:..,id:..,...} }<br/>
	 * modelName默认为model名称小写
	 */
	protected String toJsonDeleteResult(String id, String modelName) {
		return toJsonDeleteResult(id, Dates.nowDate(), modelName);
	}

	/**
	 * 转换为修改结果 { "modelName":{deleteTime:..,id:..,...} }<br/>
	 */
	protected String toJsonDeleteResult(String id, Date deleteDate, String modelName) {
		Map<String, Object> result = new HashMap<String, Object>();

		Map<String, Object> info = new HashMap<String, Object>();
		info.put("id", id);
		info.put("deleteTime", deleteDate);
		result.put(modelName, info);

		return toJson(result);
	}

	// ==================================================获取请求人信息==================================================

	/**
	 * 获取请求中的token<br/>
	 * 该token在请求头中传入，其键值为：{@link #TOKEN_PARAM_NAME}<br/>
	 * 如果没有传入token这将返回一个空字符
	 * 
	 * @return 返回请求中的token({@link #TOKEN_PARAM_NAME})信息。如果没有传入头信息键值为{@link #TOKEN_PARAM_NAME}则返回一个空字符
	 */
	@Nullable
	protected String getToken() {
		return getRequest().getHeader(TOKEN_PARAM_NAME);
	}

	/**
	 * 获取当前登录人信息
	 * 
	 * @see #getToken()
	 */
	@Nullable
	protected String getRequestToken() {
		return getRequestUserInfo().getToken();
	}

	/**
	 * 获取发送该请求的用户名<br/>
	 * 通过token获取用户名称
	 * 
	 * @return 发送该请求的用户名称。如果token不存在则返回空字符
	 */
	@Nullable
	protected String getRequestUserName() {
		return getRequestUserInfo().getUserName();
	}

	/**
	 * 获取发送该请求的用户的orgId<br/>
	 * 通过token获取用户名称
	 * 
	 * @return 发送该请求的用户名称。如果token不存在则返回空字符
	 */
	@Nullable
	protected String getRequestUserOrgId() {
		return getRequestUserInfo().getOrgId();
	}

	/**
	 * 获取发送该请求的用户的组织名称（orgName）<br/>
	 * 通过token获取用户名称
	 * 
	 * @return 发送该请求的用户名称。如果token不存在则返回空字符
	 */
	@Nullable
	protected String getRequestUserOrgName() {
		return getRequestUserInfo().getOrgName();
	}

	/**
	 * 获取发送该请求的用户的组织No（orgNo）<br/>
	 * 通过token获取用户名称
	 * 
	 * @return 发送该请求的用户名称。如果token不存在则返回空字符
	 */
	@Nullable
	protected String getRequestUserOrgNo() {
		return getRequestUserInfo().getOrgNo();
	}

	/**
	 * 获取当前登录人信息
	 */
	@Nullable
	protected RequestUserInfo getRequestUserInfo() {
		return RequestUserInfoHolder.currentRequestUserInfo();
	}

	// ==================================================utils==================================================

	/**
	 * 获取请求参数
	 */
	protected String getRequestPayload(HttpServletRequest request) {
		return HttpServletUtils.readerBodyStr(request);
	}

}
