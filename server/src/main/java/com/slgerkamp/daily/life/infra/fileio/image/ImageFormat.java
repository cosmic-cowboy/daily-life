package com.slgerkamp.daily.life.infra.fileio.image;

import java.util.EnumSet;
import java.util.Optional;

/**
 * <p>アプリケーションで扱う画像フォーマットを表します。
 *
 */
public enum ImageFormat {
	JPEG("image/jpeg"),
	PNG("image/png"),
	GIF("image/gif");

	public final String mimeType;

	private ImageFormat(String mimeType) {
		this.mimeType = mimeType;
	}

	/**
	 * <p>ファイルのmineTypeを元に画像フォーマットを返します。
	 * @param mimeType
	 * @return
	 */
	public static Optional<ImageFormat> fromMime(final String mimeType) {
		return EnumSet.allOf(ImageFormat.class)
				.stream()
				.filter(format -> format.mimeType.equals(mimeType))
				.findFirst();
	}
}
