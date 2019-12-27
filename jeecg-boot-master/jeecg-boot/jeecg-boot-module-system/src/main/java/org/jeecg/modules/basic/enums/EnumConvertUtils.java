package org.jeecg.modules.basic.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.EnumUtils;

/**
 * 枚举通用接口
 */
public class EnumConvertUtils {

	/**
	 * 返回枚举数据的下拉格式
	 *
	 * @param clazz
	 * @return
	 */
	public static List<Map<String, Object>> getList(Class clazz) {

		List<AbstractEnum> data = EnumUtils.getEnumList(clazz);
		List<Map<String, Object>> list = new ArrayList<>();
		for (AbstractEnum one : data) {
			list.add(one.getMap());
		}
		return list;
	}

	/**
	 * 指定Sid对应的数据
	 *
	 * @param clazz
	 * @param ids
	 * @return
	 */
	public static List<Map<String, Object>> getListIn(Class clazz, Object... ids) {

		List<Map<String, Object>> list = new ArrayList<>();
		if (ids == null || ids.length == 0)
			return list;
		List<AbstractEnum> data = EnumUtils.getEnumList(clazz);
		for (Object id : ids) {
			for (AbstractEnum one : data) {
				if (null != id && one.getId().equals(id.toString())) {
					list.add(one.getMap());
					break;
				}
			}
		}
		return list;
	}

	/**
	 * 排除指定数据的list数据
	 *
	 * @param clazz
	 * @param ids
	 * @return
	 */
	public static List<Map<String, Object>> getListNotIn(Class clazz, Object... ids) {

		List<Map<String, Object>> list = new ArrayList<>();
		List<AbstractEnum> data = EnumUtils.getEnumList(clazz);
		if (ids == null || ids.length == 0)
			return list;
		for (AbstractEnum one : data) {
			boolean isIn = false;
			for (Object id : ids) {
				if (one.getId().equals(id.toString())) {
					isIn = true;
					break;
				}
			}
			if (!isIn)
				list.add(one.getMap());
		}
		return list;
	}

	/**
	 * 获取枚举的对象格式信息
	 *
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static Map<String, Object> getMap(Class clazz, Object id) {

		if (id == null)
			return new HashedMap();
		List<AbstractEnum> data = EnumUtils.getEnumList(clazz);

		for (AbstractEnum one : data) {
			if (one.getId().equals(id.toString()))
				return one.getMap();
		}
		return new HashedMap();
	}

	/**
	 * 获取枚举的国际化信息
	 *
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static String getName(Class clazz, Object id) {

		if (id == null)
			return null;
		List<AbstractEnum> data = EnumUtils.getEnumList(clazz);

		for (AbstractEnum one : data) {
			if (one.getId().equals(id.toString()))
				return one.getName();
		}
		return null;

	}

	/**
	 * 根据name返回Id
	 *
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static String getId(Class clazz, Object name) {

		if (name == null)
			return null;
		List<AbstractEnum> data = EnumUtils.getEnumList(clazz);

		for (AbstractEnum one : data) {
			if (one.getName().equals(name.toString()))
				return one.getId();
		}
		return null;

	}

	/**
	 * 获取单个对象
	 *
	 * @param clazz
	 * @param id
	 * @return
	 */
	public static <E> E getOne(Class<E> clazz, Object id) {

		if (id == null)
			return null;
		for (E e : Arrays.asList(clazz.getEnumConstants())) {
			if (e instanceof AbstractEnum) {
				AbstractEnum one = (AbstractEnum) e;
				if (one.getId().equals(id.toString()))
					return (E) one;
			}
		}
		return null;

	}
}
