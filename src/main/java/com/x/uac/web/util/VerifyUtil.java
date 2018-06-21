package com.x.uac.web.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.x.base.vo.ResponseHeader;
import com.x.sdk.ccs.util.ConfigTool;
import com.x.sdk.mail.EmailFactory;
import com.x.sdk.mail.EmailTemplateUtil;
import com.x.sdk.mcs.MCSClientFactory;
import com.x.sdk.mcs.interfaces.ICacheClient;
import com.x.sdk.util.RandomUtil;
import com.x.sdk.util.StringUtil;
import com.x.sdk.web.model.ResponseData;
import com.x.uac.web.constants.VerifyConstants;
import com.x.uac.web.model.email.SendEmailRequest;


public class VerifyUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(VerifyUtil.class);

	public static BufferedImage getImageVerifyCode(String namespace, String cacheKey, int width, int height) {
		// int width = 100, height = 38;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics g = image.getGraphics();

		// 设定背景色
		g.setColor(new Color(0xDCDCDC));
		g.fillRect(0, 0, width, height);

		// 画边框
		g.setColor(Color.lightGray);
		g.drawRect(0, 0, width - 1, height - 1);

		// 取随机产生的认证码
		String verifyCode = RandomUtil.randomString(VerifyConstants.PictureVerifyConstants.VERIFY_SIZE);
		// 将认证码存入缓存
		try{
		    ICacheClient cacheClient = MCSClientFactory.getCacheClient(cacheKey);
	        String overTimeStr = ConfigTool.getConfigItem(VerifyConstants.PictureVerifyConstants.VERIFY_OVERTIME_KEY);
	        cacheClient.setex(cacheKey, Integer.valueOf(overTimeStr), verifyCode);
	        LOGGER.debug("cacheKey=" + cacheKey + ",verifyCode=" + verifyCode);
	        // 将认证码显示到图象中
	        g.setColor(new Color(0x10a2fb));

	        g.setFont(new Font("Atlantic Inline", Font.PLAIN, 30));
	        String Str = verifyCode.substring(0, 1);
	        g.drawString(Str, 8, 25);

	        Str = verifyCode.substring(1, 2);
	        g.drawString(Str, 28, 30);
	        Str = verifyCode.substring(2, 3);
	        g.drawString(Str, 48, 27);

	        Str = verifyCode.substring(3, 4);
	        g.drawString(Str, 68, 32);
	        // 随机产生88个干扰点，使图象中的认证码不易被其它程序探测到
	        Random random = new Random();
	        for (int i = 0; i < 88; i++) {
	            int x = random.nextInt(width);
	            int y = random.nextInt(height);
	            g.drawOval(x, y, 0, 0);
	        }

	        // 图象生效
	        g.dispose();

		}catch(Exception e){
		    LOGGER.info(e.getMessage());
		}
		 return image;
	}

	/**
	 * 发送邮件
	 * 
	 * @param emailRequest
	 * @return
	 */
	public static boolean sendEmail(SendEmailRequest emailRequest) {
		boolean success = true;
		String htmlcontext = EmailTemplateUtil.buildHtmlTextFromTemplate(emailRequest.getTemplateRUL(), emailRequest.getData());
		try {
			EmailFactory.SendEmail(emailRequest.getTomails(), emailRequest.getCcmails(), emailRequest.getSubject(), htmlcontext);
		} catch (Exception e) {
			success = false;
			LOGGER.info(e.getMessage());
		}
		return success;
	}

	/**
	 * 发送手机信息
	 * 
	 * @param smDataInfoNotify
	 * @return
	 */
	/*public static boolean sendPhoneInfo(SMDataInfoNotify smDataInfoNotify) {
		SMSServices smsService = DubboConsumerFactory.getService("sMSServices");
		boolean isSuccess = true;
		try {
			smsService.dataInput(smDataInfoNotify);
		} catch (Exception e) {
			isSuccess = false;
			LOGGER.info(e.getMessage());
		}
		return isSuccess;
	}*/


	/**
	 * 检查图片验证码
	 * 
	 * @param verifyCode
	 * @param cacheVerifyCode
	 * @return
	 */
	public static ResponseData<String> checkPictureVerifyCode(String verifyCode, String cacheVerifyCode) {
		ResponseData<String> responseData = null;
		ResponseHeader responseHeader = null;
		if (cacheVerifyCode == null) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "图形验证码已失效", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_PICTURE_ERROR, "图形验证码已失效");
		} else if (cacheVerifyCode.compareToIgnoreCase(verifyCode) != 0) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "图形验证码错误", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_PICTURE_ERROR, "图形验证码错误");
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "图形验证码正确", null);
			responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "图形验证码正确");
		}
		responseData.setResponseHeader(responseHeader);
		return responseData;
	}

	/**
	 * 检查邮箱验证码
	 * 
	 * @param verifyCode
	 * @param cacheVerifyCode
	 * @return
	 */
	public static ResponseData<String> checkPhoneVerifyCode(String verifyCode, String cacheVerifyCode) {
		ResponseData<String> responseData = null;
		ResponseHeader responseHeader = null;
		if (cacheVerifyCode == null) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "验证码已失效", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_VERIFY_ERROR, "短信验证码已失效");
		} else if (!cacheVerifyCode.equals(verifyCode)) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "短信验证码错误", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_VERIFY_ERROR, "短信验证码错误");
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "手机校验码正确", null);
			responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "手机校验码正确");
		}
		responseData.setResponseHeader(responseHeader);
		return responseData;
	}

	/**
	 * 检查邮箱验证码
	 * 
	 * @param verifyCode
	 * @param cacheVerifyCode
	 * @return
	 */
	public static ResponseData<String> checkEmailVerifyCode(String verifyCode, String cacheVerifyCode) {
		ResponseData<String> responseData = null;
		ResponseHeader responseHeader = null;
		if (cacheVerifyCode == null) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "邮箱校验码已失效", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_VERIFY_ERROR, "邮箱校验码已失效");
		} else if (!cacheVerifyCode.equals(verifyCode)) {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "邮箱校验码已错误", null);
			responseHeader = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_VERIFY_ERROR, "邮箱校验码错误");
		} else {
			responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, "邮箱校验码正确", null);
			responseHeader = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.SUCCESS_CODE, "邮箱校验码正确");
		}
		responseData.setResponseHeader(responseHeader);
		return responseData;
	}



	/**
	 * 检查ip发送手机验证码次数是否超限
	 * 
	 * @param namespace
	 * @param key
	 * @return
	 */
	public static ResponseData<String> checkIPSendPhoneCount(String namespace, String key) {
		ResponseData<String> responseData = null;
		ResponseHeader header = null;
		ICacheClient cacheClient = MCSClientFactory.getCacheClient(namespace);
		String countStr = cacheClient.get(key);
		try{
		    String overTime = ConfigTool.getConfigItem(VerifyConstants.PhoneVerifyConstants.IP_SEND_OVERTIME_KEY);
	        if (!StringUtil.isBlank(countStr)) {
	            String maxNoStr = ConfigTool.getConfigItem(VerifyConstants.PhoneVerifyConstants.SEND_VERIFY_IP_MAX_NO_KEY);
	            int maxNo = Integer.valueOf(maxNoStr);
	            int count = Integer.valueOf(countStr);
	            count++;
	            if (count > maxNo) {
	                String message = "频繁发送手机验证码，已被禁止" + Integer.valueOf(overTime) / 60 + "分钟";
	                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, message);
	                header = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_VERIFY_ERROR, message);
	                responseData.setResponseHeader(header);
	                return responseData;
	            }else{
	                cacheClient.setex(key, Integer.valueOf(overTime), Integer.toString(count));
	            }
	        }else{
	            cacheClient.setex(key, Integer.valueOf(overTime), "1");
	        }
	        responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, null);
	        header = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.REGISTER_VERIFY_ERROR, null);
	        responseData.setResponseHeader(header);
	        return responseData;  
		}catch(Exception e){
		   LOGGER.info(e.getMessage()); 
		}
		 return responseData;  
	}
	
	/**
	 * 检查ip发送邮箱验证码次数是否超限
	 * 
	 * @param namespace
	 * @param key
	 * @return
	 */
	public static ResponseData<String> checkIPSendEmailCount(String namespace, String key) {
		ResponseData<String> responseData = null;
		ResponseHeader header = null;
		ICacheClient cacheClient = MCSClientFactory.getCacheClient(namespace);
		String countStr = cacheClient.get(key);
		//限制时间
		try{
		    String overTime = ConfigTool.getConfigItem(VerifyConstants.EmailVerifyConstants.IP_SEND_OVERTIME_KEY);
	        if (!StringUtil.isBlank(countStr)) {
	            String maxNoStr = ConfigTool.getConfigItem(VerifyConstants.EmailVerifyConstants.SEND_VERIFY_IP_MAX_NO_KEY);
	            int maxNo = Integer.valueOf(maxNoStr);
	            int count = Integer.valueOf(countStr);
	            count++;
	            if (count > maxNo) {
	                String message = "频繁发送邮箱验证码，已被禁止" + Integer.valueOf(overTime) / 60 + "分钟";
	                responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, message);
	                header = new ResponseHeader(false, VerifyConstants.ResultCodeConstants.REGISTER_VERIFY_ERROR, message);
	                responseData.setResponseHeader(header);
	                return responseData;
	            }else{
	                cacheClient.setex(key, Integer.valueOf(overTime), Integer.toString(count));
	            }
	        }else{
	            cacheClient.setex(key, Integer.valueOf(overTime), "1");
	        }
	        responseData = new ResponseData<String>(ResponseData.AJAX_STATUS_SUCCESS, null);
	        header = new ResponseHeader(true, VerifyConstants.ResultCodeConstants.REGISTER_VERIFY_ERROR, null);
	        responseData.setResponseHeader(header);
	        return responseData;
		}catch(Exception e){
		    LOGGER.info(e.getMessage());
		}
		 return responseData;
	}
}
