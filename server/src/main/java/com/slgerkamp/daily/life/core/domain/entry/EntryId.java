package com.slgerkamp.daily.life.core.domain.entry;

import com.slgerkamp.daily.life.infra.modeling.LongValueId;

/**
 * <p>日記エントリのIDを表すクラスです。
 *
 */
public class EntryId extends LongValueId {

	public EntryId(long value) {
		super(value);
	}

}
