package com.slgerkamp.daily.life.core.domain.entity;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.slgerkamp.daily.life.db.query.QEntry;
import com.slgerkamp.daily.life.infra.message.EmailService;
import com.slgerkamp.daily.life.infra.message.Message;
import com.slgerkamp.daily.life.infra.message.ThymeleafMessageContent;
import com.slgerkamp.daily.life.infra.message.db.DbService;

/**
 * <p>日記のエントリ情報を登録・更新・削除するためのクエリクラスです。
 * <p>これは日記エントリの更新用オブジェクトとなります。
 * <p>詳細は下記を参照ください。
 * <p>http://d.hatena.ne.jp/j5ik2o/20110211/1297442876
 *
 */
public final class EntryRepository {

	private final DbService dbService;
	private final EmailService emailService;
	private EntryRepository(DbService dbService, EmailService emailService) {
		this.dbService = dbService;
		this.emailService = emailService;
	}

	/**
	 * <p>日記のエントリ情報を登録・更新・削除するためのクエリクラスのファクトリクラスです。
	 *
	 */
	@Component
	public static class Factory {
		@Autowired
		private DbService dbService;
		@Autowired
		private EmailService emailService;

		public EntryRepository create() {
			return new EntryRepository(dbService, emailService);
		}

	}

	/**
	 * <p>日記エントリを作成します。
	 */
	public long create(final String content) {

		// 登録情報の整理
		final Timestamp now = Timestamp.from(Instant.now());
		final long messageId = now.getTime();
		// 登録
		QEntry e = QEntry.entry;
		SQLInsertClause insert = dbService.insert(e)
				.set(e.userId, 1111L)
				.set(e.messageId, messageId)
				.set(e.messageType, "日記")
				.set(e.content, content)
				.set(e.createDate, now)
				.set(e.updateDate, now)
				.set(e.postDate, now);

		insert.execute();

		// メール送信（TODO イベント駆動にして、メール送信専用のhandlerクラスを作成する）
		SimpleMailMessage simpleMessage =  Message.build(message -> {
				message.sender("dummy@dummy.com");
				message.addresses(Collections.singletonList("dummy@dummy.com"));
				message.subject("dummy");
				message.content(new ThymeleafMessageContent());
			});
		emailService.send(Collections.singletonList(simpleMessage));

		return messageId;

	}

	/**
	 * <p>日記エントリを削除します。
	 */
	public void delete(final long messegeId) {

		QEntry e = QEntry.entry;
		SQLDeleteClause delete = dbService.delete(e)
				.where(e.messageId.eq(messegeId));
		delete.execute();

	}

}
