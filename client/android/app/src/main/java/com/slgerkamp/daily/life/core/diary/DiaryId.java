package com.slgerkamp.daily.life.core.diary;

import java.io.Serializable;

import rx.Observable;
import rx.functions.Func1;

/**
 * <p>日記を一意で識別するためのIDクラスです。
 */
public class DiaryId implements Serializable{

    public final long value;

    public DiaryId(long value) {
        this.value = value;
    }

    public static Observable<DiaryId> from(Long value) {
        return Observable.just(new DiaryId(value));
    }
}
