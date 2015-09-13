package com.slgerkamp.daily.life.infra.fileio.temp;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;

import com.google.common.base.Throwables;
import com.slgerkamp.daily.life.infra.fileio.util.FileioUtility;

/**
 * <p>一時的にファイルを保存するためのディレクトリです。
 * 一時ファイルが作成、加工される範囲を限定し、
 * 処理後に削除されるようにしています。
 *
 */
public final class TempDirectorySession implements AutoCloseable {

	// 一時ファイルを扱うための一時ディレクトリ
	private final Path directory;

	private TempDirectorySession(Path directory) {
		this.directory = directory;
	}

	/**
	 * <p>一時的にファイルを保存するためのディレクトリです。
	 * @return
	 */
	public static TempDirectorySession start() {
		try {
			Path tmpDir = Files.createTempDirectory(TempDirectorySession.class.getName());
			return new TempDirectorySession(tmpDir);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * 一時ディレクトリにファイルをコピーします。
	 * @param input
	 * @return
	 */
	public Path copy(InputStream input) {
		try {
			Path path = createTempPath();
			Files.copy(input, path);
			return path;
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	@Override
	public void close() {
		// 生成された一時ディレクトリの後処理
		try {
			Files.walkFileTree(directory, FileioUtility.recursiveDeleteFile());
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	// ----------------------------------------------------------------
	//    内部処理
	// ----------------------------------------------------------------
	/**
	 * 一時ディレクトリ内に一時パスを作成します。
	 * @return
	 */
	private Path createTempPath() {
		return directory.resolve(Integer.toString(Instant.now().getNano()));
	}

}
