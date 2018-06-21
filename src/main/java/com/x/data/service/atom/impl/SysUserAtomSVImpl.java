package com.x.data.service.atom.impl;

import com.x.base.exception.SystemException;
import com.x.data.dao.mapper.bo.SysUser;
import com.x.data.dao.mapper.factory.MapperFactory;
import com.x.data.dao.mapper.interfaces.SysUserMapper;
import com.x.data.service.atom.interfaces.ISysUserAtomSV;

import org.springframework.stereotype.Component;

@Component
public class SysUserAtomSVImpl implements ISysUserAtomSV {

	@Override
	public SysUser queryByUserId(String userId) throws SystemException {
		SysUserMapper sysUserMapper = MapperFactory.getSysUserMapper();
		return sysUserMapper.selectByPrimaryKey(userId);
	}

}
