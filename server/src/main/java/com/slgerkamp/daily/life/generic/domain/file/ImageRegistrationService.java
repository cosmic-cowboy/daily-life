package com.slgerkamp.daily.life.generic.domain.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Optional;

import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Throwables;
import com.slgerkamp.daily.life.infra.fileio.image.ImageFormat;
import com.slgerkamp.daily.life.infra.fileio.storage.FileStorage;
import com.slgerkamp.daily.life.infra.fileio.temp.TempDirectorySession;
import com.slgerkamp.daily.life.infra.fileio.temp.TempFileId;
import com.slgerkamp.daily.life.infra.fileio.temp.TempFileStorage;

/**
 * <p>画像ファイルを登録するためのサービスクラスです。
 *
 */
@Component
public class ImageRegistrationService {

	private TempFileStorage tempFileStorage;
	
	@Autowired
	public ImageRegistrationService(TempFileStorage tempFileStorage) {
		this.tempFileStorage = tempFileStorage;
	}
	/**
	 * <p>登録準備を行います。
	 * @param input
	 * @param size
	 * @throws IllegalFileException
	 */
	public TempFileId submit(InputStream input) throws IllegalFileException{
		try (TempDirectorySession session = TempDirectorySession.start()){
			// ファイルを一時ディレクトリにコピーする
			Path source = session.copy(input);
			// ファイルの画像フォーマットを取得する + 判定する
			Optional<ImageFormat> format = format(source.toFile());
			if (!format.isPresent()) {
				throw new IllegalFileException("この画像ファイルには対応しておりません");
			}
			// TODO 画像の最適化
			// ファイルのIDを返却
			TempFileId tempFileId = tempFileStorage.register(source.toFile());
			return tempFileId;
		}
		
	}
	/**
	 * <p>画像ファイルを登録します。
	 * @param input
	 */
	public File	commit(TempFileId tempFileId)){
		
		// DBへの登録
		// ストレージへの登録
		
	}

	
	// ----------------------------------------------------------------
	//    内部処理
	// ----------------------------------------------------------------
	/**
	 * <p>ファイルからメタデータを取得し、画像フォーマットを割り当てます。
	 * @param file
	 * @return
	 */
	private Optional<ImageFormat> format(File file){
		try (InputStream input = new BufferedInputStream(new FileInputStream(file))) {
			TikaConfig tika = new TikaConfig();
			Metadata metadata = new Metadata();
			MediaType type = tika.getDetector().detect(input, metadata);
		
			return ImageFormat.fromMime(type.toString());
		
		} catch (TikaException | IOException e) {
			throw Throwables.propagate(e);
		}
	}
}
