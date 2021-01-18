package org.yelong.support.json.gson;

import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Gson工具类
 * 
 * @date 2020年12月4日下午3:06:58
 */
public final class GsonUtils {

	private static final Gson gson = new Gson();

	private GsonUtils() {
	}

	/**
	 * JSON字符串转换为 map
	 * 
	 * @date 2020年12月4日下午3:15:53
	 * @param <K>  map key type
	 * @param <V>  map value type
	 * @param json JSON字符串
	 * @return map对象
	 */
	@SuppressWarnings("unchecked")
	public static <K, V> Map<K, V> fromJsonToMap(String json) {
		return gson.fromJson(json, Map.class);
	}

	/**
	 * JSON字符串转换为 map
	 * 
	 * @date 2020年12月4日下午3:15:53
	 * @param <K>      map key type
	 * @param <V>      map value type
	 * @param json     JSON字符串
	 * @param mapClass map对象的类型
	 * @return map对象
	 */
	@SuppressWarnings({ "rawtypes" })
	public static <M extends Map> M fromJsonToMap(String json, Class<M> mapClass) {
		return gson.fromJson(json, mapClass);
	}

	/**
	 * JSON字符串转换为 List
	 * 
	 * @date 2020年12月4日下午3:15:53
	 * @param <T>  list elements type
	 * @param json JSON字符串
	 * @return list对象
	 */
	public static <T> List<T> fromJsonToList(String json) {
		return gson.fromJson(json, new TypeToken<List<Object>>() {
		}.getType());
	}

}
