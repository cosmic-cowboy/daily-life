package com.slgerkamp.daily.life.infra;

import android.content.Context;
import android.text.format.DateFormat;

import com.slgerkamp.daily.life.core.diary.PostDate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * ユーティリティクラス
 */
public class Utils {

    private Utils(){}

    // ロケールに合わせた日付表示
    public static String localDate(Context context, Date date){
        String dayOfTheWeek = (String)DateFormat.format("EEEE", date);
        return DateFormat.getLongDateFormat(context).format(date) + " " + dayOfTheWeek;
    }

    public static boolean hasDate(List<Calendar> calendars, PostDate postDate){
        final Calendar cal = postDate.toCalendar();
        Calendar nullableCalendar =
                Observable.from(calendars).filter(new Func1<Calendar, Boolean>() {
                    @Override
                    public Boolean call(Calendar calendar) {
                        return
                                calendar.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
                                        && calendar.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                                        && calendar.get(Calendar.DATE) == cal.get(Calendar.DATE);
                    }
                }).firstOrDefault(null).toBlocking().first();
        return nullableCalendar != null;
    }
}
