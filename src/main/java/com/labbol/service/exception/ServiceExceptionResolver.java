/**
 * 
 */
package com.labbol.service.exception;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
import org.yelong.core.jdbc.DataBaseOperationType;
import org.yelong.core.model.ModelException;
import org.yelong.core.model.validator.exception.FieldValueBlankException;
import org.yelong.core.model.validator.exception.FieldValueLengthException;
import org.yelong.core.model.validator.exception.FieldValueNullException;
import org.yelong.support.spring.mvc.HandlerResponseWay;

import com.labbol.service.exception.ErrorResponseTypeSupplier.ErrorResponseType;
import com.labbol.service.exception.ErrorResponseTypeSupplier.ErrorType;

/**
 * 异常处理
 * 
 * @author PengFei
 */
public class ServiceExceptionResolver extends AbstractHandlerExceptionResolver {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceExceptionResolver.class);

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ModelAndView modelAndView = null;
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			HandlerResponseWay handlerResponseWay = HandlerResponseWay.handlerResponseWayResolver(handlerMethod);
			if (handlerResponseWay == HandlerResponseWay.JSON) {
				response.setStatus(HttpStatus.OK.value()); // 设置状态码
				response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE); // 设置ContentType
				response.setCharacterEncoding("UTF-8"); // 避免乱码
				// response.setCharacterEncoding("GBK"); //避免乱码
				response.setHeader("Cache-Control", "no-cache, must-revalidate");
				String json = handlerExceptionResponseJson(request, response, handler, ex);
				try {
					response.getWriter().write(json);
				} catch (IOException e) {
					json = e.getMessage();
					e.printStackTrace();
				}
				// LOGGER.info("exception:"+handlerMethod.toString()+"---response:"+json);
				modelAndView = new ModelAndView();
			} else if (handlerResponseWay == HandlerResponseWay.MODEL_AND_VIEW) {
				modelAndView = handlerExceptionResponseModelAndView(request, response, handlerResponseWay, ex);
			}
		}
		return modelAndView;
	}

	protected String handlerExceptionResponseJson(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) {
		ServiceException serviceException = resolveException((HandlerMethod) handler, ex);
		// 出现异常后，日志详情默认是异常信息
		// LogRecordUtils.setLogDesc(ServiceException.getMessage());
		// 将异常信息记录到日志中
		LOGGER.error("", serviceException);
		return serviceException.getErrorResponseJson();
	}

	protected ModelAndView handlerExceptionResponseModelAndView(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		// 此方法一般不会被调用
		return new ModelAndView("500.jsp");
	}

	/**
	 * 解析异常为服务异常
	 * 
	 * @author PengFei
	 * @date 2020年12月4日下午4:17:24
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected ServiceException resolveException(HandlerMethod handlerMethod, Exception ex) {
		if (ex instanceof ServiceException) {
			return (ServiceException) ex;
		}
		Method method = handlerMethod.getMethod();
		ErrorResponseTypeSupplierAppoint errorResponseTypeSupplierAppoint = getAnnotation(method,
				ErrorResponseTypeSupplierAppoint.class);
		// 如果指定了异常响应类型供应商
		if (null != errorResponseTypeSupplierAppoint) {
			Class<? extends Enum<?>> enumClass = errorResponseTypeSupplierAppoint.enumClass();
			// 如果value不是一个枚举则抛出异常
			if (!ErrorResponseTypeSupplier.class.isAssignableFrom(enumClass)) {
				return new ServiceInernalErrorException(enumClass + "不是转换为 ErrorResponseTypeSupplier");
			}
			DataBaseOperationType dataBaseOperationType = errorResponseTypeSupplierAppoint.operationType();
			ErrorType errorType = null;
			if (ex instanceof FieldValueNullException || ex instanceof FieldValueBlankException) {// 字段值null或者空白异常
				errorType = ErrorType.DATA_NULL;
			} else if (ex instanceof FieldValueLengthException) {// 字段值超过规范的长度异常
				errorType = ErrorType.DATA_LENGTH;
			} else if (ex instanceof ModelException || ex instanceof SQLException) {
				if (dataBaseOperationType == DataBaseOperationType.INSERT) {
					errorType = ErrorType.DATA_SAVE;
				} else if (dataBaseOperationType == DataBaseOperationType.UPDATE) {
					errorType = ErrorType.DATA_MODIFY;
				} else {
					return new ServiceInernalErrorException("不支持的操作类型格式：" + dataBaseOperationType);
				}
			} else {
				return buildDefaultServiceException(ex);
			}
			ErrorResponseType errorResponseType = new ErrorResponseType(errorResponseTypeSupplierAppoint.typeId(),
					errorType, dataBaseOperationType);
			ErrorResponseTypeSupplier errorResponseTypeSupplier = getErrorResponseTypeSupplierByErrorResponserType(
					(Class) enumClass, errorResponseType);
			if (null != errorResponseTypeSupplier) {
				return new ServiceException(errorResponseTypeSupplier);
			}
		}
		// 默认为服务内部异常
		return buildDefaultServiceException(ex);
	}

	/**
	 * 构建默认的服务异常
	 * 
	 * @author PengFei
	 * @date 2020年12月4日下午4:44:07
	 * @param e 异常
	 * @return 服务异常
	 */
	protected ServiceException buildDefaultServiceException(Exception e) {
		return new ServiceInernalErrorException(e);
	}

	/**
	 * 根据异常响应类型获取异常响应类型供应商
	 * 
	 * @author PengFei
	 * @date 2020年12月4日下午4:44:35
	 * @param <E>               enumType
	 * @param enumClass         枚举类型
	 * @param errorResponseType 异常响应类型
	 * @return 异常响应类型供应商
	 */
	protected <E extends Enum<E>> ErrorResponseTypeSupplier getErrorResponseTypeSupplierByErrorResponserType(
			Class<E> enumClass, ErrorResponseType errorResponseType) {
		List<E> enumList = EnumUtils.getEnumList(enumClass);
		for (E e : enumList) {
			ErrorResponseTypeSupplier errorResponseTypeSupplier = (ErrorResponseTypeSupplier) e;
			ErrorResponseType enumErrorResponseType = errorResponseTypeSupplier.getErrorResponseType();
			if (null == enumErrorResponseType) {
				continue;
			}
			if (enumErrorResponseType.equals(errorResponseType)) {
				return errorResponseTypeSupplier;
			}
		}
		return null;
	}

	public static <A extends Annotation> A getAnnotation(Method method, Class<A> annotation) {
		if (method.isAnnotationPresent(annotation)) {
			return method.getAnnotation(annotation);
		}
		return null;
	}

}
