package org.yelong.support.json.gson;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.yelong.core.model.annotation.Id;
import org.yelong.core.model.annotation.Table;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * 注解排除策略
 * 
 * @date 2020年12月7日下午5:10:27
 */
public class AnnotationExclusionStrategy implements ExclusionStrategy {

	/** 忽略的注解 */
	private final List<Class<? extends Annotation>> ignoreAnnotations = new ArrayList<>();

	/** 只允许的注解 */
	private final List<Class<? extends Annotation>> onlyOperationAnnotations = new ArrayList<>();

	// ==================================================add==================================================

	public AnnotationExclusionStrategy addIgnoreAnnotation(Class<? extends Annotation> annotation) {
		ignoreAnnotations.add(annotation);
		return this;
	}

	public AnnotationExclusionStrategy addIgnoreAnnotation(Class<? extends Annotation>[] annotations) {
		for (Class<? extends Annotation> annotation : annotations) {
			ignoreAnnotations.add(annotation);
		}
		return this;
	}

	public AnnotationExclusionStrategy addOnlyOperationAnnotation(Class<? extends Annotation> annotation) {
		onlyOperationAnnotations.add(annotation);
		return this;
	}

	public AnnotationExclusionStrategy addOnlyOperationAnnotation(Class<? extends Annotation>[] annotations) {
		for (Class<? extends Annotation> annotation : annotations) {
			onlyOperationAnnotations.add(annotation);
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		AnnotationExclusionStrategy annotationExclusionStrategy = new AnnotationExclusionStrategy();
		annotationExclusionStrategy.addIgnoreAnnotation(ArrayUtils.toArray(Id.class, Table.class));
	}

	// ==================================================ExclusionStrategy==================================================

	@Override
	public boolean shouldSkipClass(Class<?> clazz) {
		if (!onlyOperationAnnotations.isEmpty()) {
			// 如果标注了只允许的直接则不跳过，否则跳过
			for (Class<? extends Annotation> onlyOperationAnnotation : onlyOperationAnnotations) {
				if (clazz.isAnnotationPresent(onlyOperationAnnotation)) {
					return false;
				}
			}
			return true;
		}

		for (Class<? extends Annotation> ignoreAnnotation : ignoreAnnotations) {
			if (clazz.isAnnotationPresent(ignoreAnnotation)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes f) {
		if (!onlyOperationAnnotations.isEmpty()) {
			// 如果标注了只允许的直接则不跳过，否则跳过
			for (Class<? extends Annotation> onlyOperationAnnotation : onlyOperationAnnotations) {
				if (null != f.getAnnotation(onlyOperationAnnotation)) {
					return false;
				}
			}
			return true;
		}

		for (Class<? extends Annotation> ignoreAnnotation : ignoreAnnotations) {
			if (null != f.getAnnotation(ignoreAnnotation)) {
				return true;
			}
		}
		return false;
	}

}
