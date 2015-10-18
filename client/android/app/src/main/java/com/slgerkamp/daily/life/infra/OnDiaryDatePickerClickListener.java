package com.slgerkamp.daily.life.infra;

import com.slgerkamp.daily.life.core.diary.PostDate;

/**
 * <p>DairyDatePicker用のイベントハンドラです。</p>
 */
public interface OnDiaryDatePickerClickListener {

    /**
     * <p>ハイライトされている日付をタップしたときの動作を記述してください。</p>
     */
    void onHighlightedDayOfMonthSelected(PostDate postDate);

    /**
     * <p>ハイライトされていない日付をタップしたときの動作を記述してください。</p>
     */
    void onNotHighlightedDayOfMonthSelected(PostDate postDate);

}
