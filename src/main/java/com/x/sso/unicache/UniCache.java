package com.x.sso.unicache;


import com.x.sdk.mcs.interfaces.ICacheClient;

/**
 * 统一缓存处理封装类
 * 
 * @author wangyongxin
 * 
 */
public final class UniCache {
	
	private UniCache(){}

	/**
	 * 根据配置的命名获取对应缓存实例
	 * @param ssocache 
	 * 
	 * @return
	 */
	public static ICacheClient getCache(String ssocachenamespace) {
		return UniCacheFactory.getCacheClient(ssocachenamespace);
	}

	/**
	 * 测试统一session
	 * 
	 * @param sessionId
	 * @return
	 */
	/*public static String getSessionCache(String sessionId) {
		return UniCacheFactory.getCacheClient(UniCacheConstants.CacheNamespaces.sessionCache).hget(UniCacheConstants._UniCache_PaaS_Session+sessionId, "");
	}*/

}