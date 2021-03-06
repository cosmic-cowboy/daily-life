package com.slgerkamp.daily.life.core.application.controller.user.api.v1;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

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
import com.slgerkamp.daily.life.core.application.controller.user.api.v1.form.EntryForm;
import com.slgerkamp.daily.life.core.domain.entry.EntryId;
import com.slgerkamp.daily.life.core.domain.entry.EntryQuery;
import com.slgerkamp.daily.life.core.domain.entry.EntryRepository;
import com.slgerkamp.daily.life.generic.application.PathHelper;
import com.slgerkamp.daily.life.generic.domain.file.IllegalFileException;
import com.slgerkamp.daily.life.generic.domain.file.ImageRegistrationService;
import com.slgerkamp.daily.life.infra.db.query.JsonProjection;
import com.slgerkamp.daily.life.infra.fileio.FileId;
import com.slgerkamp.daily.life.infra.fileio.FileRelation;
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
	 * <p>すべての日記を取得する。
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Map<String, Object> entryList(
			@RequestParam(value = "entryId", required = false) Long entryId,
			@RequestParam(value = "postDate", required = false) Long postDate) {
		Optional<EntryId> optEntryId = Optional.empty();
		Optional<Long> optDateTime = Optional.empty();
		if (entryId != null) {
			optEntryId = Optional.of(new EntryId(entryId));
		}
		if (postDate != null) {
			optDateTime = Optional.of(postDate);
		}
		return getEntryList(optEntryId, optDateTime);
	}

	/**
	 * <p>すべての日記の投稿日を取得する。
	 * @return
	 */
	@RequestMapping(value = "/postdate", method = RequestMethod.GET)
	public Map<String, Object> entryPostDateList() {
		return getEntryPostDateList();
	}

	// ----------------------------------------------------------------
	//     副作用のあるリクエスト
	// ----------------------------------------------------------------
	/**
	 * <p>日記を投稿する。
	 * @param entryForm
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Map<String, Object> post(@RequestBody EntryForm entryForm) {
		EntryRepository repository = entryRepositoryFactory.create();
		EntryId entryId = entryForm.create(repository);
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

	// ----------------------------------------------------------------
	//     ヘルパーメソッド
	// ----------------------------------------------------------------
	/**
	 * <p>日記のエントリを取得する。</p>
	 * @param optEntryId
	 * @param optPostDate
	 * @return
	 */
	private Map<String, Object> getEntryList(
			Optional<EntryId> optEntryId,
			Optional<Long> optPostDate) {
		BiFunction<FileRelation, EntryQuery, JsonProjection> function =
				((fileRelation, query) -> new JsonProjection()
					.put("entryId", query.entryId())
					.put("postDate", query.postDate())
					.put("content", query.content())
					.put("fileId", fileRelation.fileId)
				);
		return getEntryList(optEntryId, optPostDate, function);
	}

	/**
	 * <p>日記の投稿日を取得する。</p>
	 * @return
	 */
	private Map<String, Object> getEntryPostDateList() {
		BiFunction<FileRelation, EntryQuery, JsonProjection> function =
				((fileRelation, query) -> new JsonProjection()
					.put("postDate", query.postDate())
				);
		return getEntryList(Optional.empty(), Optional.empty(), function);

	}

	private Map<String, Object> getEntryList(
			Optional<EntryId> optEntryId,
			Optional<Long> optPostDate,
			BiFunction<FileRelation, EntryQuery, JsonProjection> function) {

		final FileRelation fileRelation = new FileRelation();
		final EntryQuery query = entryFactory.create();
		query.joinFile(fileRelation);
		query.orderByPostDateDesc();

		optEntryId.ifPresent(entryId -> query.entryId(entryId));
		optPostDate.ifPresent(postDate -> query.postDate(postDate));

		List<Map<String, Object>> list =
				query.select().list(function.apply(fileRelation, query));
		return new ImmutableMap.Builder<String, Object>()
				.put("entryList", list)
				.build();
	}

}
