package com.x.data.service.busi.impl;

import com.x.base.exception.BusinessException;
import com.x.data.constants.AccountExceptCode;
import com.x.data.service.busi.interfaces.ISysUserValidateSV;
import com.x.sdk.util.StringUtil;

import org.springframework.stereotype.Component;


@Component
public class SysUserValidateSVImpl implements ISysUserValidateSV {
	

	
	@Override
	public void checkUserId(String accountId) throws BusinessException {
		if (StringUtil.isBlank(accountId)) {
			throw new BusinessException(AccountExceptCode.ErrorCode.PARAM_NULL_ERROR, "账号ID（userId）不能为空");
		}
	}

	@Override
	public void checkUserName(String userName) throws BusinessException {
		if (StringUtil.isBlank(userName)) {
			throw new BusinessException(AccountExceptCode.ErrorCode.PARAM_NULL_ERROR, "用户名不能为空");
		}
	}


   

}
