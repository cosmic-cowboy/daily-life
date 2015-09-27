package com.slgerkamp.daily.life.core.diary;

import com.google.common.base.Optional;
import com.slgerkamp.daily.life.infra.JSONData;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func4;

/**
 * <p>日記の情報を受け渡しするためのクラスです。
 */
public class DiaryItem {

    public final DiaryId diaryId;
    public final String content;
    public final long postDate;
    public final Optional<Long> optFileId;

    public DiaryItem(DiaryId diaryId, String content, long postDate, Optional<Long> optFileId) {
        this.diaryId = diaryId;
        this.content = content;
        this.postDate = postDate;
        this.optFileId = optFileId;
    }

    public static Observable<DiaryItem> fromJSON(JSONData d) {
        return Observable.zip(
                d.getLong("entryId").flatMap(new Func1<Long, Observable<DiaryId>>() {
                    @Override
                    public Observable<DiaryId> call(Long l) {
                        return DiaryId.from(l);
                    }
                }),
                d.getString("content"),
                d.getLong("postDate"),
                d.optLong("fileId"),
                new Func4<DiaryId, String, Long, Optional<Long>, DiaryItem>() {
                    @Override
                    public DiaryItem call(DiaryId a1, String a2, Long a3, Optional<Long> a4) {
                        return new DiaryItem(a1, a2, a3, a4);
                    }
                }
        );
    }
}
