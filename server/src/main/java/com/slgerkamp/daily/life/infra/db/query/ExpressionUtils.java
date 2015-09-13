package com.slgerkamp.daily.life.infra.db.query;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.function.Function;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.MappingProjection;

/**
 * <p>QueryDSL で様々な Expression を作成するためのユーティリティクラスです。
 *
 */
public final class ExpressionUtils {

	private ExpressionUtils() { };

	/**
	 * <p>Expressionを加工可能な状態（FunctionBuild）を変換します。
	 * @param e
	 * @return
	 */
	public static <T> FunctionBuild.Builder<T> select(Expression<T> e) {
		return new FunctionBuild.Builder<T>(e);
	}


	/**
	 * <p>Expressionを加工可能な状態（FunctionBuild）。
	 *
	 */
	public static class FunctionBuild {

		/**
		 * <p>ビルダーです。
		 *
		 * @param <T>
		 */
		public static final class Builder<T> {
			Expression<T> e;

			public Builder(Expression<T> e) {
				this.e = e;
			}

			public <R> Expression<R> then(Function<? super T, R> function) {
				return new Product<T, R>(ExpressionUtils.<R>returnClass(function), e, function);
			}
		}

		/**
		 * <p>Expressionを加工可能を受け持つクラスです。
		 *
		 * @param <P1>
		 * @param <R>
		 */
		public static final class Product<P1, R> extends MappingProjection<R> {

			private static final long serialVersionUID = -495739462613558908L;
			private final Expression<P1> e1;
			private final Function<? super P1, R> f;

			public Product(Class<? super R> cls, Expression<P1> e1, Function<? super P1, R> f) {
				super(cls, e1);
				this.e1 = e1;
				this.f = f;
			}


			@Override
			protected R map(Tuple row) {
				return f.apply(row.get(e1));
			}
		}
	}

	/**
	 * <p>Functionの戻り値の型を返却します。
	 * @param function
	 * @return
	 */
	private static <R> Class<R> returnClass(Object function) {
		Method target = null;

		// Function の apply メソッドを取得する
		for (Method m : function.getClass().getDeclaredMethods()) {
			if (m.getName().equals("apply")) {
				target = m;
				break;
			}
		}
		// apply メソッドがない場合は例外をスロー
		if (target == null) throw new IllegalStateException();

		// メソッドの仮の戻り値の型を表すTypeオブジェクトを取得する
		Type returnType;
		Type genericReturnType = target.getGenericReturnType();
		if (genericReturnType instanceof Class<?>) {
			returnType = genericReturnType;
		} else if (genericReturnType instanceof ParameterizedType) {
			returnType = ((ParameterizedType) genericReturnType).getRawType();
		} else {
			throw new IllegalStateException();
		}

		@SuppressWarnings("unchecked")
		Class<R> res = (Class<R>) returnType;

		return res;
	}
}
