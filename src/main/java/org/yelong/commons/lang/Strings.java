/**
 * 
 */
package org.yelong.commons.lang;

import java.util.Objects;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串非空验证工具类
 * 
 * @see StringUtils
 * @since 1.1
 */
public final class Strings {

	private Strings() {
	}

	// ==================================================requireNonEmpty==================================================

	/**
	 * 验证字符串是否是空字符
	 * 
	 * @param c 需要验证的字符串
	 * @return c
	 * @throws CharSequenceNotAllowEmptyException 字符串为空异常
	 */
	public static <T extends CharSequence> T requireNonEmpty(T c) throws CharSequenceNotAllowEmptyException {
		if (org.apache.commons.lang3.StringUtils.isEmpty(c)) {
			throw new CharSequenceNotAllowEmptyException();
		}
		return c;
	}

	/**
	 * 验证字符串是否是空字符
	 * 
	 * @param c       需要验证的字符串
	 * @param message 异常消息
	 * @return c
	 * @throws CharSequenceNotAllowEmptyException 字符串为空异常
	 */
	public static <T extends CharSequence> T requireNonEmpty(T c, String message)
			throws CharSequenceNotAllowEmptyException {
		if (org.apache.commons.lang3.StringUtils.isEmpty(c)) {
			throw new CharSequenceNotAllowEmptyException(message);
		}
		return c;
	}

	/**
	 * 验证字符串是否是空字符
	 * 
	 * @param c                 需要验证的字符串
	 * @param exceptionSupplier 异常提供者
	 * @return c
	 * @throws Exception 字符串为空异常。这个异常时调用的自定义异常
	 * @since 2.2.1
	 */
	public static <T extends CharSequence> T requireNonEmpty(T c, Supplier<Exception> exceptionSupplier)
			throws Exception {
		if (StringUtils.isBlank(c)) {
			throw exceptionSupplier.get();
		}
		return c;
	}

	// ==================================================requireNonBlank==================================================

	/**
	 * 验证字符串是否是空白字符
	 * 
	 * @param c 需要验证的字符串
	 * @return c
	 * @throws CharSequenceNotAllowBlankException 字符串为空白字符异常
	 */
	public static <T extends CharSequence> T requireNonBlank(T c) {
		if (org.apache.commons.lang3.StringUtils.isBlank(c)) {
			throw new CharSequenceNotAllowBlankException();
		}
		return c;
	}

	/**
	 * 验证字符串是否是空白字符
	 * 
	 * @param c       需要验证的字符串
	 * @param message 异常消息
	 * @return c
	 * @throws CharSequenceNotAllowBlankException 字符串为空白字符异常
	 */
	public static <T extends CharSequence> T requireNonBlank(T c, String message) {
		if (org.apache.commons.lang3.StringUtils.isBlank(c)) {
			throw new CharSequenceNotAllowBlankException(message);
		}
		return c;
	}

	/**
	 * 验证字符串是否是空白字符
	 * 
	 * @param c                 需要验证的字符串
	 * @param exceptionSupplier 异常供应商
	 * @return c
	 * @throws Exception 字符串为空白字符的自定义异常
	 * @since 2.2.1
	 */
	public static <T extends CharSequence> T requireNonBlank(T c, Supplier<Exception> exceptionSupplier)
			throws Exception {
		if (StringUtils.isBlank(c)) {
			throw exceptionSupplier.get();
		}
		return c;
	}

	// ==================================================nullOrBlank==================================================

	/**
	 * 判断值是否为null或者为空白字符，只有当值为字符串类型时才会进行空白字符的验证
	 * 
	 * @param value 值
	 * @return <code>true</code> 值为null或者值为字符串类型时为空白字符
	 */
	public static boolean isNullOrBlank(Object value) {
		return value instanceof CharSequence ? StringUtils.isBlank((CharSequence) value) : null == value;
	}

	/**
	 * 判断值是否为null或者为空白字符，只有当值为字符串类型时才会进行空白字符的验证
	 * 
	 * @param value 值
	 * @return value
	 * @throws CharSequenceNotAllowBlankException 字符串空白字符串异常
	 */
	public static <T> T requireNonNullOrBlank(T value) {
		Objects.requireNonNull(value);
		if (isNullOrBlank(value)) {
			throw new CharSequenceNotAllowBlankException();
		}
		return value;
	}

	/**
	 * 判断值是否为null或者为空白字符，只有当值为字符串类型时才会进行空白字符的验证
	 * 
	 * @param value   值
	 * @param message 异常消息
	 * @return this
	 * @throws CharSequenceNotAllowBlankException 字符串空白字符串异常
	 */
	public static <T> T requireNonNullOrBlank(T value, String message) {
		Objects.requireNonNull(value);
		if (isNullOrBlank(value)) {
			throw new CharSequenceNotAllowBlankException(message);
		}
		return value;
	}

	/**
	 * 判断值是否为null或者为空白字符，只有当值为字符串类型时才会进行空白字符的验证
	 * 
	 * @param value   值
	 * @param message 异常消息
	 * @return this
	 * @throws Exception 字符串空白字符串的自定义异常
	 * @since 2.2.1
	 */
	public static <T> T requireNonNullOrBlank(T value, Supplier<Exception> exceptionSupplier) throws Exception {
		Objects.requireNonNull(value);
		if (isNullOrBlank(value)) {
			throw exceptionSupplier.get();
		}
		return value;
	}

	// ==================================================exception==================================================

	/**
	 * 字符不允许为空字符异常
	 */
	public static class CharSequenceNotAllowEmptyException extends RuntimeException {

		private static final long serialVersionUID = 7254731206561925448L;

		public CharSequenceNotAllowEmptyException() {
		}

		public CharSequenceNotAllowEmptyException(String message) {
			super(message);
		}

	}

	/**
	 * 字符不允许为空白字符异常
	 */
	public static class CharSequenceNotAllowBlankException extends RuntimeException {

		private static final long serialVersionUID = 7254731206561925448L;

		public CharSequenceNotAllowBlankException() {
		}

		public CharSequenceNotAllowBlankException(String message) {
			super(message);
		}

	}

}
