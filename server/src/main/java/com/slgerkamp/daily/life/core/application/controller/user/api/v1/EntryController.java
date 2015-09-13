package com.slgerkamp.daily.life.core.application.controller.user.api.v1;

import java.util.List;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.slgerkamp.daily.life.core.domain.entry.EntryQuery;
import com.slgerkamp.daily.life.core.domain.entry.EntryRepository;
import com.slgerkamp.daily.life.core.domain.entry.EntryId;
import com.slgerkamp.daily.life.generic.application.PathHelper;
import com.slgerkamp.daily.life.generic.domain.file.IllegalFileException;
import com.slgerkamp.daily.life.generic.domain.file.ImageRegistrationService;
import com.slgerkamp.daily.life.infra.db.query.JsonProjection;
import com.slgerkamp.daily.life.infra.fileio.FileId;
import com.slgerkamp.daily.life.infra.fileio.temp.TempFileId;


/**
 * <p>日記のentryへのリクエストを司るクラスです。
 *
 */
@RestController
@Transactional
@RequestMapping(PathHelper.USER_API_V1 + "/entry")
public class EntryController {

	@Autowired
	EntryQuery.Factory entryFactory;

	@Autowired
	EntryRepository.Factory entryRepositoryFactory;

	@Autowired
	ImageRegistrationService imageRegistrationService;

	/**
	 * <p>日記を閲覧する。
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, Object> list() {

		EntryQuery query = entryFactory.create();
		List<Map<String, Object>> list =
				query.select().list(
						new JsonProjection()
							.put("postDate", query.postDate())
							.put("content", query.content())
				);
		return new ImmutableMap.Builder<String, Object>()
				.put("entryList", list)
				.build();
	}

	/**
	 * <p>日記を投稿する。
	 * @param entry
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Map<String, Object> post(@RequestBody String content) {
		EntryId entryId = entryRepositoryFactory.create().create(content);
		return new ImmutableMap.Builder<String, Object>()
				.put("entryId", entryId.longValue())
				.build();
	}

	/**
	 * <p>日記を削除する。
	 * @param entryId
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@RequestParam Long entryId) {

		entryRepositoryFactory.create().delete(new EntryId(entryId));
	}

	/**
	 * <p>画像を投稿する。
	 * @param entry
	 * @throws IllegalFileException
	 */
	@RequestMapping(value = "/image", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Map<String, Object> uploadImage(InputStream input) throws IllegalFileException {
		TempFileId tempFileId = imageRegistrationService.submit(input);
		FileId fileId = imageRegistrationService.commit(tempFileId);
		return new ImmutableMap.Builder<String, Object>()
				.put("fileId", fileId.longValue())
				.build();
	}

	/**
	 * <p>画像を削除する。
	 * @param entry
	 */
	@RequestMapping(value = "/image", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteImage(@RequestParam Long fileId) {

	}


}
