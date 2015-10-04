package com.slgerkamp.daily.life.core.domain.entry;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.slgerkamp.daily.life.db.query.QEntry;
import com.slgerkamp.daily.life.infra.db.DbService;
import com.slgerkamp.daily.life.infra.utils.CommonUtils;

/**
 * <p>日記のエントリ情報を登録・更新・削除するためのクエリクラスです。
 * <p>これは日記エントリの更新用オブジェクトとなります。
 * <p>詳細は下記を参照ください。
 * <p>http://d.hatena.ne.jp/j5ik2o/20110211/1297442876
 *
 */
public final class EntryRepository {

	private final DbService dbService;
	private EntryRepository(DbService dbService) {
		this.dbService = dbService;
	}

	/**
	 * <p>日記のエントリ情報を登録・更新・削除するためのクエリクラスのファクトリクラスです。
	 *
	 */
	@Component
	public static class Factory {
		@Autowired
		private DbService dbService;

		public EntryRepository create() {
			return new EntryRepository(dbService);
		}

	}

	/**
	 * <p>日記エントリを作成します。
	 */
	public EntryId create(final String content, final LocalDate localDate, Optional<Long> optFileId) {

		// 登録情報の整理
		final Timestamp now = Timestamp.from(Instant.now());
		final EntryId entryId = new EntryId(CommonUtils.getUniqueId());
		// 登録
		QEntry e = QEntry.entry;
		SQLInsertClause insert = dbService.insert(e)
				.set(e.entryId, entryId.longValue())
				.set(e.content, content)
				.set(e.postDate, localDate.toString())
				.set(e.createDate, now)
				.set(e.updateDate, now);
		optFileId.ifPresent(fileId -> insert.set(e.fileId, fileId));
		insert.execute();

		return entryId;

	}

	/**
	 * <p>日記エントリを削除します。
	 */
	public void delete(final EntryId entryId) {

		QEntry e = QEntry.entry;
		SQLDeleteClause delete = dbService.delete(e)
				.where(e.entryId.eq(entryId.longValue()));
		delete.execute();

	}

}
