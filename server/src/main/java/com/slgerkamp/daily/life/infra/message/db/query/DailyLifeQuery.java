package com.slgerkamp.daily.life.infra.message.db.query;

import java.sql.Connection;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;


/**
 * <p>DailyLifeで利用する標準 DailyLifeQueryです。
 *
 * @param <T> 返す要素の型
 */
public class DailyLifeQuery<T> extends AbstractSQLQuery<T, DailyLifeQuery<T>> {

	private static final long serialVersionUID = 5911201164924402881L;


	public DailyLifeQuery(Connection connection, SQLTemplates templates) {
		super(connection, new Configuration(templates));
	}

	/**
     * <p>サブクエリ用のクエリを作成します。
     */
	public DailyLifeQuery() {
		super(null, new Configuration(SQLTemplates.DEFAULT), new DefaultQueryMetadata());
	}

	/**
	 * <p>データを取得するための Selection を取得します。
	 */
	public Selection select() {
		return new Selection(this);
	}

	@Override
	public DailyLifeQuery<T> clone(Connection connection) {

		DailyLifeQuery<T> q = new DailyLifeQuery<T>(connection, getConfiguration().getTemplates());
		q.clone(this);
		return q;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <U> DailyLifeQuery<U> select(Expression<U> expr) {
		queryMixin.setProjection(expr);
		return (DailyLifeQuery<U>) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public DailyLifeQuery<Tuple> select(Expression<?>... exprs) {
		queryMixin.setProjection(exprs);
		return (DailyLifeQuery<Tuple>) this;
	}
}
