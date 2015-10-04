package com.slgerkamp.daily.life.core.application.controller.user.api.v1.form;

import java.time.LocalDate;
import java.util.Optional;

import com.slgerkamp.daily.life.core.domain.entry.EntryId;
import com.slgerkamp.daily.life.core.domain.entry.EntryRepository;

import lombok.Data;

/**
 * <p>Entry 作成時のformクラスです。
 *
 */
@Data
public class EntryForm {

	public String content;
	public String postDate;
	public Long fileId;

	public EntryId create(EntryRepository repository) {
		return repository.create(content, LocalDate.parse(postDate), Optional.ofNullable(fileId));
	}

}
