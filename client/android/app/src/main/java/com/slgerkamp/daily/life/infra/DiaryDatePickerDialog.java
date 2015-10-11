package com.slgerkamp.daily.life.infra;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.common.collect.ImmutableList;
import com.slgerkamp.daily.life.core.diary.DiaryFragment;
import com.slgerkamp.daily.life.core.diary.PostDate;
import com.slgerkamp.daily.life.generic.Backend;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * <p>
 *     DiaryLife用カレンダー
 *     目的
 *     ・Viewのカスタマイズ
 *     ・カレンダーの表示を微調整
 * </p>
 */
public class DiaryDatePickerDialog extends DatePickerDialog {

    /**
     * リリース年以前の当時は検討していないので、2015年以前は表示しない。
     * 仕様変更により、日記のインポートや過去分の投稿も可能にする場合はこの値を変更する。
     */
    private static final int DAIRY_LIFE_MIN_YEAR = 2015;

    /**
     * <p>
     *     インスタンス化を行う。
     *     ・日付設定
     *     ・カレンダーの表示領域設定
     *     ・タップの有効領域設定
     * </p>
     * @param callBack
     * @param year
     * @param monthOfYear
     * @param dayOfMonth
     * @return
     */
    public static DiaryDatePickerDialog newInstance(DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        DiaryDatePickerDialog ret = new DiaryDatePickerDialog();
        ret.initialize(callBack, year, monthOfYear, dayOfMonth);

        // カレンダーのタップの有効領域設定
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        ret.setMaxDate(calendar);
        // カレンダーの表示領域
        ret.setYearRange(DAIRY_LIFE_MIN_YEAR, year + 1);

        return ret;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // 投稿日をカレンダーに反映
        setPostDate();

        // OK、CANCELボタンの非表示
        LinearLayout linearLayout = (LinearLayout) view.findViewById(com.wdullaer.materialdatetimepicker.R.id.done_background);
        linearLayout.getLayoutParams().height = 0;
        linearLayout.setVisibility(View.INVISIBLE);

        return view;
    }

    /**
     * <p>カレンダーの日付がタップされたタイミングで呼び出されます。</p>
     * @param year
     * @param month
     * @param day
     */
    @Override
    public void onDayOfMonthSelected(int year, int month, int day) {
        super.onDayOfMonthSelected(year, month, day);
        tryVibrate();
        if(getDialog() != null) getDialog().cancel();
    }

    /**
     * <p>日記の投稿日を取得し、カレンダーに反映する</p>
     */
    private void setPostDate() {
        new Backend(getActivity()).get("entry/postdate")
                .toObservable()
                .flatMap(new Func1<JSONData, Observable<List<JSONData>>>() {
                    @Override
                    public Observable<List<JSONData>> call(JSONData json) {
                        return json.getList("entryList");
                    }
                })
                .map(new Func1<List<JSONData>, List<PostDate>>() {
                    @Override
                    public List<PostDate> call(List<JSONData> jsonData) {

                        ImmutableList.Builder<PostDate> builder = new ImmutableList.Builder<>();
                        for (JSONData d : jsonData) {
                            PostDate postDate = d.getLong("postDate").flatMap(new Func1<Long, Observable<PostDate>>() {
                                @Override
                                public Observable<PostDate> call(Long l) {
                                    return PostDate.from(l);
                                }
                            }).toBlocking().lastOrDefault(null);
                            if (postDate != null) {
                                builder.add(postDate);
                            } else {
                                Log.e(DiaryFragment.class.getSimpleName(), "invalid data: " + d);
                            }
                        }
                        return builder.build();
                    }
                })
                .subscribe(new Action1<List<PostDate>>() {
                    @Override
                    public void call(List<PostDate> postDates) {
                        List<Calendar> postDateCalendar = Observable.from(postDates)
                                .map(new Func1<PostDate, Calendar>() {
                                    @Override
                                    public Calendar call(PostDate postDate) {
                                        return postDate.toCalendar();
                                    }
                                })
                                .toList().toBlocking().single();
                        setHighlightedDays(postDateCalendar.toArray(new Calendar[0]));
                    }
                });
    }


}
