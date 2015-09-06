package com.slgerkamp.daily.life.infra.fileio.temp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.base.Throwables;
import com.slgerkamp.daily.life.infra.fileio.storage.FileStorage;
import com.slgerkamp.daily.life.infra.fileio.storage.FileStorage.StorageException;
import com.slgerkamp.daily.life.infra.utils.CommonUtils;

public class TempFileStorage {

	private final FileStorage storage;

	@Autowired
	public TempFileStorage(FileStorage storage) {
		this.storage = storage;
	}
	
	/**
	 * <p>一時ファイルを追加します。
	 */
	public TempFileId register(File file) {
		try (InputStream in = new FileInputStream(file)) {
			return register(in);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}
	
	/**
	 * <p>一時ファイルを追加します。
	 */
	private TempFileId register(InputStream in) {
		try {
			TempFileId id = new TempFileId(CommonUtils.getUniqueId());
			storage.write(in, Long.toString(id.longValue()));
			return id;
		} catch (StorageException e) {
			throw Throwables.propagate(e);
		}
	}

	/**
	 * <p>一時ファイルを読み込みます。
	 */
	public InputStream read(TempFileId id) {
		try {
			return storage.read(Long.toString(id.longValue()));
		} catch (FileNotFoundException e) {
			throw Throwables.propagate(e);
		}
	}
	
	/**
	 * <p>一時ファイルを削除します。
	 */
	public void delete(TempFileId id) {
		try {
			storage.delete(Long.toString(id.longValue()));
		} catch (StorageException e) {
			throw Throwables.propagate(e);
		}
	}

}
