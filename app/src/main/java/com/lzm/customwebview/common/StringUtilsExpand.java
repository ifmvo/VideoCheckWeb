package com.lzm.customwebview.common;

import java.util.List;

public class StringUtilsExpand {

	public static String valueOf(Object obj) {
		return (obj == null) ? null : obj.toString();
	}

	public static String maxSubstring(String text, String target) {
		String max = (text.length() > target.length()) ? text : target;
		String min = max.equals(text) ? target : text;
		for (int i = 0; i < min.length(); i++) {
			for (int m = 0, n = min.length() - i; n != min.length() + 1; m++, n++) {
				String sub = min.substring(m, n);
				if (max.contains(sub)) {
					return sub;
				}
			}
		}
		return "";
	}

	/**
	 * 拓展List<String>的jion方法
	 */
	public static String join(String join, List list) {
		int size = list.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			if (i == (size - 1)) {
				sb.append(list.get(i));
			} else {
				sb.append(list.get(i)).append(join);
			}
		}

		return sb.toString();
	}

	/**
	 * 拓展List<String>的jion方法
	 */
	public static String join(String join, Object[] objects) {
		int size = objects.length;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			if (i == (size - 1)) {
				sb.append(objects[i]);
			} else {
				sb.append(objects[i]).append(join);
			}
		}

		return sb.toString();
	}

}
