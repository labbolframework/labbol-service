/**
 * 
 */
package com.labbol.service.convert;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

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

/**
 * @author PengFei
 */
public abstract class AbstractRequestBodyConvertObject<T> implements RequestBodyConvertObject<T> {

	private static final GsonBuilder modelDeserializerGsonBuilder = new GsonBuilder();

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
	public T convert(HttpServletRequest request) throws InvalidParameterException {
		try {
			String json = HttpServletRequestReuseWrapper.readerBodyStr(request);
			System.out.println("请求参数：" + json);
			return jsonToObject(json);
		} catch (Exception e) {
			throw new InvalidParameterException(e);
		}

	}

	/**
	 * 获取可以对model实现值为null的字段update的问题。
	 * 
	 * @author 彭飞
	 * @date 2019年10月21日下午4:04:25
	 * @version 1.0
	 * @return
	 */
	@Deprecated
	protected GsonBuilder getModelGsonBuilder() {
		return modelDeserializerGsonBuilder;
	}

	public static GsonBuilder getModelDeserializerGsonBuilder() {
		return modelDeserializerGsonBuilder;
	}

	public static Gson getModelDeserializerGson() {
		return modelDeserializerGsonBuilder.create();
	}

	/**
	 * 获取可以对model实现值为null的字段update的问题。 暂未开发
	 * 
	 * @author 彭飞
	 * @date 2019年10月21日下午4:04:25
	 * @version 1.0
	 * @return
	 */
	protected Gson getModelGson() {
		return getModelGsonBuilder().create();
	}

	/**
	 * 将json字符串转换为指定的类型对象
	 * 
	 * @author 彭飞
	 * @date 2019年7月17日下午6:56:52
	 * @param json request的请求消息体
	 * @return
	 * @throws InvalidParameterException 如果json转换不为指定的类型则抛出此异常
	 */
	public abstract T jsonToObject(String json) throws Exception;

}
