package com.x.uac.web.util;

import com.x.sdk.mcs.MCSClientFactory;
import com.x.sdk.mcs.interfaces.ICacheClient;
import com.x.sdk.util.StringUtil;
import net.sf.json.JSONObject;


public final class CacheUtil {
	private CacheUtil(){}
	
	/**
	 * 设置缓存数据
	 * @param key
	 * @param second 缓存有效时间
	 * @param value
	 * @param namespace 命名空间
	 */
	public static void setValue(String key, int second, Object value, String namespace){
		JSONObject userObject = JSONObject.fromObject(value);
		ICacheClient cacheClient = MCSClientFactory.getCacheClient(namespace);
		cacheClient.setex(key, second, userObject.toString());
	}
	
	/**
	 * 取值
	 * @param key
	 * @param namespace 命名空间
	 * @param beanClass 取出的对象类型
	 * @return
	 */
	public static Object getValue(String key,String namespace,Class<?> beanClass){
		if(StringUtil.isBlank(key)||StringUtil.isBlank(namespace)){
			return null;
		}
		ICacheClient cacheClient = MCSClientFactory.getCacheClient(namespace);
		String userClientStr = cacheClient.get(key);
		if(StringUtil.isBlank(userClientStr)){
			return null;
		}
		JSONObject userObject = JSONObject.fromObject(userClientStr);
		return JSONObject.toBean(userObject, beanClass);
	}
	
	/**
	 * 
	 * @param key
	 * @param namespace 命名空间
	 */
	public static void deletCache(String key,String namespace){
		ICacheClient cacheClient = MCSClientFactory.getCacheClient(namespace);
		cacheClient.del(key);
	}
	
}
