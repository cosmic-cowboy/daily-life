package com.slgerkamp.daily.life.infra.db.query;

import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.Expression;

/**
 * データの集合からデータを取得するためのインタフェースを返します。
 */
public class Selection {

	private final DailyLifeQuery<?> query;

	/* package */ Selection(DailyLifeQuery<?> query) {
		this.query = query;
	}

	/** 該当するデータをすべて取得します。 */
	public <T> List<T> list(Expression<T> fields) {
		return query.select(fields).fetch();
	}

	/**
	 * <p>データを一件取得します。
	 * <p>要素がない場合はNullPointerExceptionを返します。
	 *
	 */
	public <T> T one(Expression<T> fields) {
		T t = query.select(fields).fetchOne();
		if (t == null) {
			throw new NullPointerException();
		}
		return t;
	}

	/**
	 * <p>複数あるデータの中から最初の一件を取得します。
	 *
	 * <p>条件を満たす結果が二件以上あり、その中から一件取得する場合を除き {@link #one(Expression)} を呼び出す事が推奨されます。
	 */
	public <T> Optional<T> first(Expression<T> fields) {
		return Optional.ofNullable(query.select(fields).fetchFirst());
	}
}
