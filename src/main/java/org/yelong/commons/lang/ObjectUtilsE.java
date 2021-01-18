package org.yelong.commons.lang;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * 对象工具类
 * 
 * @since 2.2.1
 * @see Objects
 */
public final class ObjectUtilsE {

	private ObjectUtilsE() {
	}

	/**
	 * 验证对象是否为 <code>null</code>
	 * 
	 * @param <T>               object type
	 * @param obj               对象
	 * @param exceptionSupplier 异常供应商
	 * @return obj
	 * @throws Exception 验证对象抛出的自定义异常
	 */
	public static <T> T requireNonNull(T obj, Supplier<? extends Exception> exceptionSupplier) throws Exception {
		if (obj == null)
			throw exceptionSupplier.get();
		return obj;
	}

}
