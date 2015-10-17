package com.slgerkamp.daily.life.core.domain.entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.core.types.Expression;
import com.slgerkamp.daily.life.db.query.QEntry;
import com.slgerkamp.daily.life.infra.db.DbService;
import com.slgerkamp.daily.life.infra.db.query.DailyLifeQuery;
import com.slgerkamp.daily.life.infra.db.query.ExpressionUtils;
import com.slgerkamp.daily.life.infra.db.query.Selection;
import com.slgerkamp.daily.life.infra.fileio.FileRelation;


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
	public Expression<EntryId> entryId() {
		return ExpressionUtils.select(entry.entryId).then(EntryId::new);
	}
	public Expression<String> content() {
		return entry.content;
	}
	public Expression<Long> postDate() {
		return entry.postDate;
	}

	// ----------------------------------------------------------------
	//     フィルタ
	// ----------------------------------------------------------------
	public EntryQuery entryId(EntryId entryId) {
		query.where(entry.entryId.eq(entryId.longValue()));
		return this;
	}
	public EntryQuery postDate(Long postDate) {
		query.where(entry.postDate.eq(postDate));
		return this;
	}

	// ----------------------------------------------------------------
	//     フィルタ　Fileテーブル
	// ----------------------------------------------------------------
	/**
	 * ファイル情報を取得するために join します。
	 */
	public EntryQuery joinFile(FileRelation relation) {
		relation.leftJoin(query, entry.fileId);
		return this;
	}

	// ----------------------------------------------------------------
	//     ソート
	// ----------------------------------------------------------------
	public EntryQuery orderByPostDateDesc() {
		query.orderBy(entry.postDate.desc());
		return this;
	}

}

