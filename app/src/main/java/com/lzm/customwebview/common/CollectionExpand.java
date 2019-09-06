package com.lzm.customwebview.common;

import java.util.Collection;
import java.util.Map;

/**
 * Collection拓展类
 * @author yangtao
 */
@SuppressWarnings("ALL")
public class CollectionExpand {

	/**
	 * collection是否为Null或Empty
	 */
	public static boolean isNullOrEmpty(Collection collection) { return collection == null || collection.isEmpty(); }

	/**
	 * map是否为Null或Empty
	 */
	public static boolean isNullOrEmpty(Map map) { return map == null || map.isEmpty(); }
}
