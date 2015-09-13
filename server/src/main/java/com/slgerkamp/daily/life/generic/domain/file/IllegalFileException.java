package com.slgerkamp.daily.life.generic.domain.file;

/**
 * <p>不正なファイルであることを表す例外です。
 *
 */
public class IllegalFileException extends Exception {

	private static final long serialVersionUID = 1L;

	public IllegalFileException(String message) {
		super(message);
	}
}
