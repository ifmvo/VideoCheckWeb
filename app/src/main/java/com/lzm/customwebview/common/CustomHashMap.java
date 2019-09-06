package com.lzm.customwebview.common;

import java.util.HashMap;

public class CustomHashMap<T> extends HashMap<String,T> {
	public CustomHashMap putObject(String key, T t){
		super.put(key, t);
		return this;
	}
}
