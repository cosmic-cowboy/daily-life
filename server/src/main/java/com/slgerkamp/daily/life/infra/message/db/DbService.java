package com.slgerkamp.daily.life.infra.message.db;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.slgerkamp.daily.life.infra.message.db.query.DailyLifeQuery;

/**
 * <p>DBを使用する際に利用します。
 *
 */
@Component
public class DbService {

	@Autowired
	private DataSource dataSource;


	/**
	 * <p>データベースのレコードを取得するクエリを取得します。
	 *
	 * @param table
	 * @return
	 */
	public DailyLifeQuery<Void> query(RelationalPath<?> table) {
		return new DailyLifeQuery<Void>(getConnection(), createDialect()).from(table);
	}

	/**
	 * <p>データベースのレコードを更新する。
	 * @param table
	 * @return
	 */
	public SQLUpdateClause update(RelationalPath<?> table) {
		return new SQLUpdateClause(getConnection(), createDialect(), table);
	}

	/**
	 * <p>データベースにレコードを削除する。
	 * @param table
	 * @return
	 */
	public SQLDeleteClause delete(RelationalPath<?> table) {
		return new SQLDeleteClause(getConnection(), createDialect(), table);
	}

	/**
	 * <p>データベースにレコードを登録する。
	 * @param table
	 * @return
	 */
	public SQLInsertClause insert(RelationalPath<?> table) {
		return new SQLInsertClause(getConnection(), createDialect(), table);
	}



	/**
	 * <p>コネクションの取得処理です。
	 * <p>コネクションはかならずここから取得してください。
	 *
	 * <p>
	 * SpringでqueryDSLを利用する場合に、SpringConnectionProviderの活用を推奨しています。
	 * https://github.com/querydsl/querydsl/issues/1254
	 *
	 * ◆ SpringConnectionProvider ◆
	 * https://github.com/querydsl/querydsl/blob/master/querydsl-sql-spring
	 * /src/main/java/com/querydsl/sql/spring/SpringConnectionProvider.java
	 *
	 * 現状では、Transactionのハンドリングだけできれば良いので、判定処理だけ追加しています。
	 * 今後他の機能が必要になった場合は、下記から新しいライブラリを追加してください。
	 * http://mvnrepository.com/artifact/com.mysema.querydsl/querydsl-sql-spring
	 * </p>
	 *
	 * @return
	 */
	private Connection getConnection() {

		Connection connection = DataSourceUtils.getConnection(dataSource);
		if (!DataSourceUtils.isConnectionTransactional(connection, dataSource)) {
			throw new IllegalStateException("Connection is not transactional");
		}
		return connection;
	}

	/**
	 * <p>DB特有の拡張や構文を追加したテンプレートを提供します。
	 * @return SQLTemplates
	 */
	private static SQLTemplates createDialect() {
		return new H2Templates();
	}
}
