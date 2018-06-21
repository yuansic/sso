package com.x.data.service.busi.impl;

import com.x.base.exception.SystemException;
import com.x.data.dao.mapper.bo.SysUser;
import com.x.data.service.atom.interfaces.ISysUserAtomSV;
import com.x.data.service.busi.interfaces.IUserBusiSV;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserBusiSVImpl implements IUserBusiSV {

    @Autowired
    ISysUserAtomSV iSysUserAtomSV;

    @Override
    public SysUser queryByUserId(String userId) throws SystemException {
        return iSysUserAtomSV.queryByUserId(userId);
    }

    

}
