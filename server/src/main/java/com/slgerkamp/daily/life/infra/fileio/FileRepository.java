package com.slgerkamp.daily.life.infra.fileio;

import java.io.InputStream;
import java.sql.Timestamp;
import java.time.Instant;

import com.google.common.base.Throwables;
import com.slgerkamp.daily.life.db.query.QFile;
import com.slgerkamp.daily.life.infra.fileio.storage.FileStorage;
import com.slgerkamp.daily.life.infra.fileio.storage.FileStorage.StorageException;
import com.slgerkamp.daily.life.infra.message.db.DbService;
import com.slgerkamp.daily.life.infra.utils.CommonUtils;

/**
 * <p>ファイルの管理を司るクラスです。
 *
 */
public class FileRepository {

	private final DbService dbService;
	private final FileStorage storage;
	private final QFile file = QFile.file;

	public FileRepository(DbService dbService, FileStorage storage) {
		this.dbService = dbService;
		this.storage = storage;
	}

	public FileId register(InputStream in) {

		FileId fileId = new FileId(CommonUtils.getUniqueId());

		// DB に 登録
		dbService.insert(file)
			.set(file.fileId, fileId.longValue())
			.set(file.createDate, Timestamp.from(Instant.now()))
			.execute();

		// FileStorage に 登録
		try {
			storage.write(in, Long.toString(fileId.longValue()));
			return fileId;
		} catch (StorageException e) {
			throw Throwables.propagate(e);
		}
	}
}
