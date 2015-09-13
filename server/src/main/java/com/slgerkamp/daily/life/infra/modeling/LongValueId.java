package com.slgerkamp.daily.life.infra.modeling;

/**
 * <p>long型でIDで表すときに利用する基幹クラスです。
 *
 */
public abstract class LongValueId {

	private final long value;

	/**
	 * <p>コンストラクタです。
	 * @param value
	 */
	protected LongValueId(long value) {
		this.value = value;
	}

	public final long longValue() {
		return value;
	}

	// ----------------------------------------------------------------
	//    おまじない
	// ----------------------------------------------------------------
	@Override
	public int hashCode() {
		int result = 1;
		result = 31 * result + (int) (value ^ (value >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		LongValueId other = (LongValueId) obj;
		if (value != other.value) return false;
		return true;
	}
}
