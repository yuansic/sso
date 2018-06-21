package com.x.uac.web.controller.captcha;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.x.uac.web.constants.Constants;
import com.x.uac.web.util.VerifyUtil;

@RequestMapping("/captcha")
@Controller
public class CaptchaController {
	private static final Logger LOG = LoggerFactory.getLogger(CaptchaController.class);


	/**
	 * 获取验证码
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getImageVerifyCode")
	public void getImageVerifyCode(HttpServletRequest request, HttpServletResponse response) {
		String cacheKey = Constants.Register.CACHE_KEY_VERIFY_PICTURE + request.getSession().getId();
		BufferedImage image = VerifyUtil.getImageVerifyCode(Constants.Register.CACHE_NAMESPACE, cacheKey, 100, 38);
		try {
			ImageIO.write(image, "PNG", response.getOutputStream());
		} catch (IOException e) {
			LOG.error("生成图片验证码错误：" + e);
			e.printStackTrace();
		}
	}
	
}
