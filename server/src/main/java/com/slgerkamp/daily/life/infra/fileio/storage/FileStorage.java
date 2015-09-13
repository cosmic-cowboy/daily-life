package com.slgerkamp.daily.life.infra.fileio.storage;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * <p>ファイルを管理するための抽象クラスです。
 *
 */
public interface FileStorage {

	/**
	 * <p>ファイルを書き込みます。
	 * @param input
	 * @param path
	 */
	void write(InputStream input, String path) throws StorageException;

	/**
	 * <p>ファイルを読み込みます。
	 * @param path
	 * @return
	 */
	InputStream read(String path) throws FileNotFoundException;

	/**
	 * <p>ファイルを削除します。
	 * @param path
	 */
	void delete(String path) throws StorageException;

	/**
	 * <p>ストレージで発生した例外を表します。
	 */
	public class StorageException extends Exception {

		private static final long serialVersionUID = 1L;

		public StorageException(String operation, String path, Throwable cause) {
			super("ストレージで例外が発生しました：" + operation + " " + path, cause);
		}
	}

}
