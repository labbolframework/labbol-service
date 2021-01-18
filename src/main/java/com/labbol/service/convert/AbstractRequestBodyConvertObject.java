/**
 * 
 */
package com.labbol.service.convert;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yelong.support.json.gson.model.ModelByteJsonDeserializer;
import org.yelong.support.json.gson.model.ModelCharacterJsonDeserializer;
import org.yelong.support.json.gson.model.ModelDateJsonDeserializer;
import org.yelong.support.json.gson.model.ModelDoubleJsonDeserializer;
import org.yelong.support.json.gson.model.ModelFloatJsonDeserializer;
import org.yelong.support.json.gson.model.ModelIntegerJsonDeserializer;
import org.yelong.support.json.gson.model.ModelLongJsonDeserializer;
import org.yelong.support.json.gson.model.ModelShortJsonDeserializer;
import org.yelong.support.json.gson.model.ModelStringJsonDeserializer;
import org.yelong.support.servlet.wrapper.HttpServletRequestReuseWrapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.labbol.service.exception.InvalidParameterException;
import com.labbol.service.exception.ServiceException;
import com.labbol.service.exception.ServiceInernalErrorException;

/**
 * @author PengFei
 */
public abstract class AbstractRequestBodyConvertObject<T> implements RequestBodyConvertObject<T> {

	private static final GsonBuilder modelDeserializerGsonBuilder = new GsonBuilder();

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRequestBodyConvertObject.class);

	static {
		modelDeserializerGsonBuilder.registerTypeAdapter(Byte.class, new ModelByteJsonDeserializer());
		modelDeserializerGsonBuilder.registerTypeAdapter(Short.class, new ModelShortJsonDeserializer());
		modelDeserializerGsonBuilder.registerTypeAdapter(Integer.class, new ModelIntegerJsonDeserializer());
		modelDeserializerGsonBuilder.registerTypeAdapter(Long.class, new ModelLongJsonDeserializer());
		modelDeserializerGsonBuilder.registerTypeAdapter(Float.class, new ModelFloatJsonDeserializer());
		modelDeserializerGsonBuilder.registerTypeAdapter(Double.class, new ModelDoubleJsonDeserializer());
		modelDeserializerGsonBuilder.registerTypeAdapter(Character.class, new ModelCharacterJsonDeserializer());
		modelDeserializerGsonBuilder.registerTypeAdapter(String.class, new ModelStringJsonDeserializer());
		modelDeserializerGsonBuilder.registerTypeAdapter(Date.class, new ModelDateJsonDeserializer());
	}

	@Override
	public T convert(HttpServletRequest request) throws ServiceException {
		String json;
		try {
			json = HttpServletRequestReuseWrapper.readerBodyStr(request);
		} catch (IOException e) {
			throw new ServiceInernalErrorException("读取请求消息体异常", e);
		}
		LOGGER.info("(" + request.getRequestURI() + ")请求参数：" + json);
		if (StringUtils.isBlank(json)) {
			throw new InvalidParameterException("请求消息体为空");
		}
		try {
			return jsonToObject(json);
		} catch (ServiceException e) {
			throw e;
		} catch (Exception e) {
			throw new InvalidParameterException(e);
		}

	}

	/**
	 * 获取可以对model实现值为null的字段update的问题。
	 * 
	 * @see #getModelDeserializerGsonBuilder()
	 */
	protected GsonBuilder getModelGsonBuilder() {
		return modelDeserializerGsonBuilder;
	}

	/**
	 * @see #getModelGsonBuilder()
	 */
	public static GsonBuilder getModelDeserializerGsonBuilder() {
		return modelDeserializerGsonBuilder;
	}

	public static Gson getModelDeserializerGson() {
		return modelDeserializerGsonBuilder.create();
	}

	/**
	 * 获取可以对model实现值为null的字段update的问题。 暂未开发
	 */
	protected Gson getModelGson() {
		return getModelGsonBuilder().create();
	}

	/**
	 * 将json字符串转换为指定的类型对象
	 */
	public abstract T jsonToObject(String json) throws Exception;

}
