package com.slgerkamp.daily.life.infra.fileio;

import com.querydsl.core.types.Expression;
import com.slgerkamp.daily.life.db.query.QFile;
import com.slgerkamp.daily.life.infra.db.query.DailyLifeQuery;

/**
 * <p>ファイル情報を司るクラスです。
 *
 */
public class FileRelation {

	private final QFile file = QFile.file;
	public final Expression<Long> fileId = file.fileId;

	/**
	 * コンストラクタです。
	 */
	public FileRelation() { }

	/**
	 * 他のテーブルとファイルのテーブルを join します。
	 */
	public void leftJoin(DailyLifeQuery<?> query, Expression<Long> targetFileId) {
		query.leftJoin(file).on(file.fileId.eq(targetFileId));
	}

}
