package com.slgerkamp.daily.life.core.application.controller.user.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>ユーザ画面です。
 *
 */
@Controller
public class WebEntryController {

	/**
	 * ユーザ画面のトップページです。
	 * @return
	 */
	@RequestMapping("/")
	public String index() {
		return "user/index";
	}
}
