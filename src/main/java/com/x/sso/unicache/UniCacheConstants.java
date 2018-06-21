package com.x.sso.unicache;

/**
 * 缓存统一定义常量类
 * 
 * @author wangyongxin
 * 
 */
public final class UniCacheConstants {
	
	private UniCacheConstants(){}

	/* PaaSsession缓存前缀定义 */
	public static final String _UniCache_PaaS_Session = "R_JSID_";

	/* sso缓存前缀定义 */
	public static final String _UniCache_Redis = "R_SSOID_";

	public static final class CacheNamespaces {
		
		private CacheNamespaces(){}
		
		/* 单点登录缓存命名空间 */
		public static final String SSOCache = "com.x.sso.unicache";
		
	}
}
