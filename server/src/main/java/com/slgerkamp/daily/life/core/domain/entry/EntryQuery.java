package com.slgerkamp.daily.life.core.domain.entry;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.core.types.Expression;
import com.slgerkamp.daily.life.db.query.QEntry;
import com.slgerkamp.daily.life.infra.db.DbService;
import com.slgerkamp.daily.life.infra.db.query.DailyLifeQuery;
import com.slgerkamp.daily.life.infra.db.query.ExpressionUtils;
import com.slgerkamp.daily.life.infra.db.query.Selection;


/**
 * <p>日記のエントリ情報を取得するためのクエリクラスです。
 * <p>これは日記エントリの参照用オブジェクトとなります。
 * <p>詳細は下記を参照ください。
 * <p>http://d.hatena.ne.jp/j5ik2o/20110211/1297442876
 *
 */
public final class EntryQuery {

	final QEntry entry = new QEntry("entry");

	private final DailyLifeQuery<?> query;

	private EntryQuery(DbService dbService) {
		this.query = dbService.query(entry);
	}

	/**
	 * <p>EntryQueryのファクトリクラスです。
	 *
	 */
	@Component
	public static class Factory {
		@Autowired
		private DbService dbService;

		public EntryQuery create() {
			return new EntryQuery(dbService);
		}
	}

	// ----------------------------------------------------------------
	//     共通処理
	// ----------------------------------------------------------------
	public Selection select() {
		return query.select();
	}

	// ----------------------------------------------------------------
	//     各種パス
	// ----------------------------------------------------------------
	public Expression<String> content() {
		return entry.content;
	}
	public Expression<Long> postDate() {
		return ExpressionUtils.select(entry.postDate).then(Timestamp::getTime);
	}
}
