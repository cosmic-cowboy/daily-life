package com.slgerkamp.daily.life.generic.domain.file;

import java.io.FileNotFoundException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.slgerkamp.daily.life.infra.fileio.FileId;
import com.slgerkamp.daily.life.infra.fileio.storage.FileStorage;

/**
 * <p>画像ファイルを登録するためのサービスクラスです。
 *
 */
@Component
public class ImageAccessService {

	private final FileStorage fileStorage;

	@Autowired
	public ImageAccessService(FileStorage fileStorage) {
		this.fileStorage = fileStorage;
	}

	public ResponseEntity<?> read(FileId fileId) throws FileNotFoundException {
		InputStream in = fileStorage.read(Long.toString(fileId.longValue()));

		// 返却オブジェクトを作成
		HttpHeaders headers = new HttpHeaders();
		headers.setPragma("");
		return new ResponseEntity<Resource>(
				new InputStreamResource(in),
				headers,
				HttpStatus.OK);
	}

}
