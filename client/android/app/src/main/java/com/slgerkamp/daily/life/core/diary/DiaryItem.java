package com.slgerkamp.daily.life.core.diary;

/**
 * <p>日記の情報を受け渡しするためのクラスです。
 */
public class DiaryItem {

    public final DiaryId diaryId;
    public final String content;
    public final long postDate;

    public DiaryItem(DiaryId diaryId, String content, long postDate) {
        this.diaryId = diaryId;
        this.content = content;
        this.postDate = postDate;
    }
}
