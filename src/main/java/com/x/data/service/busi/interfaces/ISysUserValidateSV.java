package com.x.data.service.busi.interfaces;


import com.x.base.exception.BusinessException;

public interface ISysUserValidateSV {

	void checkUserId(String accountId) throws BusinessException;

	void checkUserName(String userName) throws BusinessException;

}
