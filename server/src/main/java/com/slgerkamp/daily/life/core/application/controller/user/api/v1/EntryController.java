package com.slgerkamp.daily.life.core.application.controller.user.api.v1;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.ImmutableMap;
import com.slgerkamp.daily.life.core.domain.EntryService;
import com.slgerkamp.daily.life.core.domain.entity.Entry;
import com.slgerkamp.daily.life.core.domain.entity.EntryQuery;
import com.slgerkamp.daily.life.generic.application.PathHelper;
import com.slgerkamp.daily.life.infra.message.EmailService;
import com.slgerkamp.daily.life.infra.message.Message;
import com.slgerkamp.daily.life.infra.message.ThymeleafMessageContent;
import com.slgerkamp.daily.life.infra.message.db.query.JsonProjection;


/**
 * <p>日記のentryへのリクエストを司るクラスです。
 *
 */
@RestController
@Transactional
@RequestMapping(PathHelper.USER_API_V1 + "/entry")
public class EntryController {

	private static final Logger LOGGER = Logger.getLogger(EntryController.class);

	@Autowired
	EntryService entryService;

	@Autowired
	EmailService emailService;

	@Autowired
	EntryQuery.Factory entryFactory;

	/**
	 * 日記を閲覧する。
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
	 * 日記を投稿する。
	 * @param entry
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public Entry post(@RequestBody Entry entry) {
		Entry createEntry = entryService.create(entry);
		SimpleMailMessage simpleMessage =  Message.build(message -> {
				message.sender("dummy@dummy.com");
				message.addresses(Collections.singletonList("dummy@dummy.com"));
				message.subject("dummy");
				message.content(new ThymeleafMessageContent());
			});
		emailService.send(Collections.singletonList(simpleMessage));
		return createEntry;
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
