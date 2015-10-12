package com.slgerkamp.daily.life.infra;

/**
 * <p>DairyDatePicker用のイベントハンドラです。</p>
 */
public interface OnDiaryDatePickerClickListener {

    /**
     * <p>ハイライトされている日付をタップしたときの動作を記述してください。</p>
     */
    void onHighlightedDayOfMonthSelected(int year, int month, int day);

    /**
     * <p>ハイライトされていない日付をタップしたときの動作を記述してください。</p>
     */
    void onNotHighlightedDayOfMonthSelected(int year, int month, int day);

}
