package com.slgerkamp.daily.life.core.diary;

import android.content.Context;
import android.text.format.DateFormat;

import com.google.common.base.Optional;
import com.slgerkamp.daily.life.infra.JSONData;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func4;

/**
 * <p>日記の情報を受け渡しするためのクラスです。
 */
public class DiaryItem  implements Serializable {

    public final DiaryId diaryId;
    public final String content;
    public final PostDate postDate;
    public final Optional<Long> optFileId;

    public DiaryItem(DiaryId diaryId, String content, PostDate postDate, Optional<Long> optFileId) {
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
                d.getLong("postDate").flatMap(new Func1<Long, Observable<PostDate>>() {
                    @Override
                    public Observable<PostDate> call(Long l) {
                        return PostDate.from(l);
                    }
                }),
                d.optLong("fileId"),
                new Func4<DiaryId, String, PostDate, Optional<Long>, DiaryItem>() {
                    @Override
                    public DiaryItem call(DiaryId a1, String a2, PostDate a3, Optional<Long> a4) {
                        return new DiaryItem(a1, a2, a3, a4);
                    }
                }
        );
    }

}
