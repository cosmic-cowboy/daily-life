package com.slgerkamp.daily.life.infra.utils;

import java.time.Instant;

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
		return Instant.now().getNano();
	}
}
