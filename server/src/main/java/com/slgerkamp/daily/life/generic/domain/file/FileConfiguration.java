package com.slgerkamp.daily.life.generic.domain.file;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.slgerkamp.daily.life.infra.db.DbService;
import com.slgerkamp.daily.life.infra.fileio.FileRepository;
import com.slgerkamp.daily.life.infra.fileio.storage.FileStorage;
import com.slgerkamp.daily.life.infra.fileio.storage.LocalFileStorage;
import com.slgerkamp.daily.life.infra.fileio.temp.TempFileStorage;

/**
 * <p>ファイルに関連する定義を初期設定するクラスです。
 *
 */
@Configuration
public class FileConfiguration {

	@Value("${dailylife.file.storage}")
	private String storagePath;

	@Value("${dailylife.file.tmpstorage}")
	private String tmpStoragePath;

	@Autowired
	@Bean
	FileRepository getFileRepository(DbService dbService) {
		return new FileRepository(dbService, createStorage(storagePath));
	}

	@Bean
	TempFileStorage getTempFileStorage() {
		return new TempFileStorage(createStorage(tmpStoragePath));
	}

	private FileStorage createStorage(String path) {
		FileStorage storage = new LocalFileStorage(new File(path));
		return storage;
	}

}
