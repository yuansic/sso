package com.x.sso.unicache;

import com.x.sdk.mcs.MCSClientFactory;
import com.x.sdk.mcs.interfaces.ICacheClient;
import org.springframework.stereotype.Component;



@Component
public class UniCacheFactory {

    public static ICacheClient getCacheClient(String ssocachenamespace) {
    	return MCSClientFactory.getCacheClient(ssocachenamespace);
    }
    
}
