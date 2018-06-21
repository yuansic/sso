package com.x.data.util;

import com.x.data.constants.AccountConstants.SEQ;
import com.x.sdk.component.sequence.util.SeqUtil;

/**
 * 生成账号Id Date: 2016年3月17日 <br>
 * Copyright (c) 2016 company.com <br>
 * 
 * @author wangyongxin
 */
public final class AccountSeqUtil {

    private AccountSeqUtil() {
    }

    /**
     * 生成账号ID
     *
     * @return
     * @author wangyongxin
     * @ApiCode
     */
    public static long createAccountId() {
        return SeqUtil.getNewId(SEQ.ACCOUT_ID_SEQ);

    }

    /**
     * 生成昵称
     * 
     * @return
     * @author wangyongxin
     * @ApiCode
     */
    public static String createNickName() {
        String allName = SeqUtil.getNewId(SEQ.NICK_NAME, 6);
        char c = (char) (int) (Math.random() * 26 + 97);
        return c + allName;
    }
}
