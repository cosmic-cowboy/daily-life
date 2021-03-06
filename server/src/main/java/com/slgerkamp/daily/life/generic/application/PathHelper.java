package com.slgerkamp.daily.life.generic.application;

/**
 * <p> アプリケーション全般のパスを管理するクラスです。
 *
 */
public final class PathHelper {

	private PathHelper() { };

	// ユーザが閲覧する画面のURI
	public static final String USER_WEB_INDEX = "/";

	// ユーザが利用するAPI
	public static final String USER_API = "/user/api";
	public static final String USER_API_V1 = USER_API + "/v1";


}
