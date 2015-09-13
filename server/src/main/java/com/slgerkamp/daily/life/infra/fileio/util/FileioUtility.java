package com.slgerkamp.daily.life.infra.fileio.util;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * <p>ファイル関連のユーティリティクラスです。
 *
 */
public final class FileioUtility {

	private FileioUtility() { };

	/**
	 * <p>再帰的にディレクトリ、ファイルを削除します。
	 * @return
	 */
	public static FileVisitor<Path> recursiveDeleteFile() {
		return new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				// ファイルを削除し、処理を続ける
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}
			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc)
					throws IOException {
				// ディレクトリを削除し、処理を続ける
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		};
	}
}
