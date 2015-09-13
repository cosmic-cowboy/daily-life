package com.slgerkamp.daily.life.core.domain.entity;

import com.slgerkamp.daily.life.infra.modeling.LongValueId;

/**
 * <p>日記エントリのIDを表すクラスです。
 *
 */
public class MessageId extends LongValueId {

	public MessageId(long value) {
		super(value);
	}

}
