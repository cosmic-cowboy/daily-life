package com.slgerkamp.daily.life.core.application.controller.user.api.v1.form;

import java.util.Optional;

import lombok.Data;

import com.slgerkamp.daily.life.core.domain.entry.EntryId;
import com.slgerkamp.daily.life.core.domain.entry.EntryRepository;

/**
 * <p>Entry 作成時のformクラスです。
 *
 */
@Data
public class EntryForm {

	public String content;
	public Long postDate;
	public Long fileId;

	public EntryId create(EntryRepository repository) {
		return repository.create(content, postDate, Optional.ofNullable(fileId));
	}

}
