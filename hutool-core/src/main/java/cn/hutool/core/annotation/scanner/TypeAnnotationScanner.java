package cn.hutool.core.annotation.scanner;

import cn.hutool.core.collection.CollUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Proxy;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

/**
 * 扫描{@link Class}上的注解
 *
 * @author huangchengxing
 */
public class TypeAnnotationScanner extends AbstractTypeAnnotationScanner<TypeAnnotationScanner> implements AnnotationScanner {

	/**
	 * 构造一个类注解扫描器
	 *
	 * @param includeSupperClass 是否允许扫描父类
	 * @param includeInterfaces  是否允许扫描父接口
	 * @param filter             过滤器
	 * @param excludeTypes       不包含的类型
	 */
	public TypeAnnotationScanner(boolean includeSupperClass, boolean includeInterfaces, Predicate<Class<?>> filter, Set<Class<?>> excludeTypes) {
		super(includeSupperClass, includeInterfaces, filter, excludeTypes);
	}

	/**
	 * 构建一个类注解扫描器，默认允许扫描指定元素的父类以及父接口
	 */
	public TypeAnnotationScanner() {
		this(true, true, t -> true, CollUtil.newLinkedHashSet());
	}

	/**
	 * 判断是否支持扫描该注解元素，仅当注解元素是{@link Class}接时返回{@code true}
	 *
	 * @param annotatedElement 注解元素
	 * @return 是否支持扫描该注解元素
	 */
	@Override
	public boolean support(AnnotatedElement annotatedElement) {
		return annotatedElement instanceof Class;
	}

	/**
	 * 将注解元素转为{@link Class}
	 *
	 * @param annotatedElement 注解元素
	 * @return 类
	 */
	@Override
	protected Class<?> getClassFormAnnotatedElement(AnnotatedElement annotatedElement) {
		return (Class<?>)annotatedElement;
	}

	/**
	 * 获取{@link Class#getAnnotations()}
	 *
	 * @param source      最初的注解元素
	 * @param index       类的层级索引
	 * @param targetClass 类
	 * @return 类上直接声明的注解
	 */
	@Override
	protected Annotation[] getAnnotationsFromTargetClass(AnnotatedElement source, int index, Class<?> targetClass) {
		return targetClass.getAnnotations();
	}

	/**
	 * 是否允许扫描父类
	 *
	 * @param includeSupperClass 是否
	 * @return 当前实例
	 */
	@Override
	public TypeAnnotationScanner setIncludeSupperClass(boolean includeSupperClass) {
		return super.setIncludeSupperClass(includeSupperClass);
	}

	/**
	 * 是否允许扫描父接口
	 *
	 * @param includeInterfaces 是否
	 * @return 当前实例
	 */
	@Override
	public TypeAnnotationScanner setIncludeInterfaces(boolean includeInterfaces) {
		return super.setIncludeInterfaces(includeInterfaces);
	}

	/**
	 * 若类型为jdk代理类，则尝试转换为原始被代理类
	 * @deprecated replace with {@link AbstractTypeAnnotationScanner.JdkProxyClassConverter}
	 */
	@Deprecated
	public static class JdkProxyClassConverter implements UnaryOperator<Class<?>> {
		@Override
		public Class<?> apply(Class<?> sourceClass) {
			return Proxy.isProxyClass(sourceClass) ? apply(sourceClass.getSuperclass()) : sourceClass;
		}
	}

}
