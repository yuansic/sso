package com.x.data.service.atom.interfaces;

import com.x.base.exception.SystemException;
import com.x.data.dao.mapper.bo.SysUser;

public interface ISysUserAtomSV {
	
	SysUser queryByUserId(String userId) throws SystemException;
	
}
