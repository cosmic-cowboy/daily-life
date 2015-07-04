package com.slgerkamp.daily.life.core.application.controller.user.api.v1;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.slgerkamp.daily.life.core.domain.EntryService;
import com.slgerkamp.daily.life.core.domain.entity.Entry;
import com.slgerkamp.daily.life.generic.application.PathHelper;

/**
 * <p>日記のentryへのリクエストを司るクラスです。
 *
 */
@RestController
@Transactional
@RequestMapping(PathHelper.USER_API_V1 + "/entry")
public class EntryController {

	@Autowired
	EntryService entryService;

	/**
	 * 日記を閲覧する。
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Entry> list() {
		return entryService.findAll();
	}

	/**
	 * 日記を投稿する。
	 * @param entry
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Entry post(@RequestBody Entry entry) {
		return entryService.create(entry);
	}

	/**
	 * 日記を削除する。
	 * @param entry
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@RequestParam Long messegeId) {
		entryService.delete(messegeId);
	}

}
