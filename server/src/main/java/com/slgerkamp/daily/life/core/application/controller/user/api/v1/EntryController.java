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
import com.slgerkamp.daily.life.core.domain.entity.EntryQuery;
import com.slgerkamp.daily.life.core.domain.entity.EntryRepository;
import com.slgerkamp.daily.life.generic.application.PathHelper;
import com.slgerkamp.daily.life.infra.message.db.query.JsonProjection;


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
		long messageId = entryRepositoryFactory.create().create(content);
		return new ImmutableMap.Builder<String, Object>()
				.put("messageId", messageId)
				.build();
	}

	/**
	 * <p>日記を削除する。
	 * @param messegeId
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@RequestParam Long messegeId) {
		entryRepositoryFactory.create().delete(messegeId);
	}

	/**
	 * <p>画像を投稿する。
	 * @param entry
	 */
	@RequestMapping(value = "/image", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public long uploadImage(InputStream input) {
		return 0L;
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
