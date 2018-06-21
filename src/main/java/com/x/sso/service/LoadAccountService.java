package com.x.sso.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.x.base.exception.SystemException;
import com.x.data.api.user.interfaces.ILoginSV;
import com.x.data.api.user.param.UserLoginResponse;
import com.x.sso.principal.BssCredentials;

/**
 * 加载账号信息服务（Dubbo服务）
 *
 */
@Service
public class LoadAccountService {

	@Autowired
	private ILoginSV iLoginSV;

	/**
	 * 加载账号信息
	 * 
	 * @param bssCredentials
	 * @throws SystemException
	 */
	public UserLoginResponse loadAccount(BssCredentials bssCredentials) throws SystemException {
		UserLoginResponse user = null;
		if (bssCredentials != null) {
			user = iLoginSV.queryUserByUserName(bssCredentials.getUsername());
		} // end if

		return user;
	}// end

}
