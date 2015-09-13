package com.slgerkamp.daily.life.infra.fileio;

import java.io.InputStream;

import com.google.common.base.Throwables;
import com.slgerkamp.daily.life.infra.fileio.storage.FileStorage;
import com.slgerkamp.daily.life.infra.fileio.storage.FileStorage.StorageException;
import com.slgerkamp.daily.life.infra.utils.CommonUtils;

/**
 * <p>ファイルの管理を司るクラスです。
 *
 */
public class FileRepository {

	private final FileStorage storage;

	public FileRepository(FileStorage storage) {
		this.storage = storage;
	}

	public FileId register(InputStream in) {

		try {
			FileId fileId = new FileId(CommonUtils.getUniqueId());
			storage.write(in, Long.toString(fileId.longValue()));
			return fileId;
		} catch (StorageException e) {
			throw Throwables.propagate(e);
		}
	}
}
