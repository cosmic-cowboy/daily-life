package com.slgerkamp.daily.life.infra.fileio.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import com.slgerkamp.daily.life.infra.fileio.util.FileioUtility;

/**
 * <p>ファイルシステム上、一般にアプリケーションと同一サーバー内にファイルを格納するためのクラスです。
 *
 */
public class LocalFileStorage implements FileStorage{

	private final File baseDir;
	
	public LocalFileStorage(File baseDir) {
		this.baseDir = baseDir;
	}

	@Override
	public void write(InputStream input, String path) throws StorageException {
		Path file = getFile(path);
		
		try {
			// ファイルパスが存在しないときはファイルパスまでのディレクトリを作成する
			try {
				file.toFile().getParentFile().mkdirs();
				// ファイルが存在する場合は削除する
				if(file.toFile().exists()){
					Files.delete(file);
				}
			} finally {
				input.close();
			}
			
		} catch (IOException e) {
			throw new StorageException("登録に失敗しました。", file.toAbsolutePath().toString(), e);
		}
	}

	@Override
	public InputStream read(String path) throws FileNotFoundException {
		return new FileInputStream(getFile(path).toFile());
	}

	@Override
	public void delete(String path) throws StorageException {
		Path file = getFile(path);
		try {
			Files.walkFileTree(file, FileioUtility.recursiveDeleteFile());
		} catch (IOException e) {
			throw new StorageException("削除に失敗しました。", file.toAbsolutePath().toString(), e);
		}	
	}

	// ----------------------------------------------------------------
	//    内部処理
	// ----------------------------------------------------------------
	private Path getFile(String path) {
		return baseDir.toPath().resolve(path);
	}


}
