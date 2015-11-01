package com.slgerkamp.daily.life.infra.utils;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * <p>アプリケーション全体で利用するユーティリティクラスです。
 *
 */
public final class CommonUtils {

	private CommonUtils() { };

	/**
	 * <p>ユニークIDを生成します。
	 * @return
	 */
	public static long getUniqueId() {
		// TODO 本番提供前に重複を考慮する
		return LocalDateTime.now()
				.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}
}
