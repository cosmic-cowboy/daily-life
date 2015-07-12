package com.slgerkamp.daily.life.infra.message;

/**
 * <p>Thymeleafのテンプレート機能を利用したコンテンツを作成する。
 *
 */
public class ThymeleafMessageContent implements MessageContent {

	@Override
	public String content() {
		return "ThymeleafMessageContent";
	}

}
