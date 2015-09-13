package com.slgerkamp.daily.life.infra.fileio.temp;

import com.slgerkamp.daily.life.infra.modeling.LongValueId;

/**
 * <p>一時ファイルを識別するためのIDクラスです。
 *
 */
public class TempFileId extends LongValueId {

	public TempFileId(long value) {
		super(value);
	}
}
