package com.x.data.service.busi.interfaces;

import com.x.base.exception.SystemException;
import com.x.data.dao.mapper.bo.SysUser;

public interface IUserBusiSV {
	
	SysUser queryByUserId(String userId) throws SystemException;
	
}
