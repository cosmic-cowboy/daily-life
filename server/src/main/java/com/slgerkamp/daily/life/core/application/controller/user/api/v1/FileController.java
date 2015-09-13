package com.slgerkamp.daily.life.core.application.controller.user.api.v1;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.slgerkamp.daily.life.generic.application.PathHelper;
import com.slgerkamp.daily.life.generic.domain.file.ImageAccessService;
import com.slgerkamp.daily.life.infra.fileio.FileId;


/**
 * <p>ファイルの取得処理を司るクラスです。
 *
 */
@RestController
@Transactional
@RequestMapping(PathHelper.USER_API_V1 + "/file")
public class FileController {

	@Autowired
	ImageAccessService imageAccessService;

	/**
	 * <p>画像を取得する。
	 * @return
	 * @throws FileNotFoundException
	 */
	@RequestMapping("image")
	public ResponseEntity<?> image(@RequestParam("fileId") Long fileId) throws FileNotFoundException {

		return imageAccessService.read(new FileId(fileId));
	}
}
