package com.x.sso.ticket;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.sdk.mcs.interfaces.ICacheClient;
import com.x.sso.unicache.UniCache;
import com.x.sso.unicache.UniCacheConstants;
import com.x.sso.util.SerializeUtil;

public final class CommonService {
	private static final Logger LOG = LoggerFactory.getLogger(CommonService.class);
	private static final String CHARSET = "UTF-8";
	public static final String TICKET_REPOSITORY = "com.x.sso.ticket";
	private static CommonService instance;
	private CommonService(){}
	
	public static synchronized CommonService getInstance(){
		if(instance == null){
			instance = new CommonService();
		}
		return instance;
	}
	
	public ICacheClient getCache(){
		return UniCache.getCache(UniCacheConstants.CacheNamespaces.SSOCache);
		//return RedisClient.getInstance().getJedis();
	}

	public Map<byte[], byte[]> getValues(String key) {
		try {
			return getCache().hgetAll(TICKET_REPOSITORY.getBytes());
		} catch (Exception e) {
			LOG.error("获取 obj 失败",e);
		}
		return null;
	}

	public Object getValue(String key) {
		Object val = null;
		try {
			byte[] data = getCache().hget(TICKET_REPOSITORY.getBytes(),key.getBytes(CHARSET));
			val = SerializeUtil.unserialize(data);
		} catch (Exception e) {
			LOG.error("获取 obj 失败",e);
		}
		return val;
	}

	public void removeObj(String key) {
		try {
			getCache().hdel(TICKET_REPOSITORY.getBytes(),key.getBytes(CHARSET));
		} catch (UnsupportedEncodingException e) {
			LOG.error("删除 obj 失败",e);
		}
	}

	public void saveObj(String key,
			Object obj, int maxInactiveInterval) {
		try {
			getCache().hset(TICKET_REPOSITORY.getBytes(),key.getBytes(CHARSET),SerializeUtil.serialize(obj));
			//getCache().set(key.getBytes(CHARSET), SerializeUtil.serialize(obj));
			getCache().expire(key.getBytes(CHARSET), maxInactiveInterval);
		} catch (IOException e) {
			LOG.error("obj 保存至redis异常",e);
		}
	}

	public void saveObj(String key, String ticketId) {
		try {
			getCache().hset(TICKET_REPOSITORY.getBytes(),key.getBytes(CHARSET), SerializeUtil.serialize(ticketId));
		} catch (IOException e) {
			LOG.error("obj 保存至redis异常：",e);
		}
	}
}
