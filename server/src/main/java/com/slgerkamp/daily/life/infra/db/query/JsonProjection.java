package com.slgerkamp.daily.life.infra.db.query;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.google.common.collect.ImmutableMap;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.MappingProjection;

/**
 * <p>DBから取得した値をJSON形式で提供しやすいようにMapに格納するためのExpressionです。
 *
 */
public class JsonProjection extends MappingProjection<Map<String, Object>> {

	private static final long serialVersionUID = -2087202619495858744L;

	private Map<String, Expression<?>> expressions;

	public JsonProjection(Map<String, Expression<?>> expressions) {
		super(Map.class, expressions.values().toArray(new Expression[expressions.size()]));
		this.expressions = expressions;
	}

	public JsonProjection() {
		this(Collections.<String, Expression<?>>emptyMap());
	}

	/**
	 * 値を取得して指定の属性に割り当てます。
	 */
	public JsonProjection put(String key, Expression<?> value) {
		ImmutableMap.Builder<String, Expression<?>> newExpressions =
				new ImmutableMap.Builder<String, Expression<?>>()
				.putAll(expressions)
				.put(key, value);

		return new JsonProjection(newExpressions.build());
	}

	@Override
	protected Map<String, Object> map(Tuple row) {
		Map<String, Object> res = new HashMap<>();
		for (Map.Entry<String, Expression<?>> e : expressions.entrySet()) {
			Object value = row.get(e.getValue());
			if (value instanceof Optional) {
				value = Optional.ofNullable(value);
			}
			res.put(e.getKey(), value);
		}
		return res;
	}

}
