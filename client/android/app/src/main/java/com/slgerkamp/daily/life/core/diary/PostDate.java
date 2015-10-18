package com.slgerkamp.daily.life.core.diary;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import rx.Observable;

/**
 * <p>投稿日を識別するためのIDクラスです。
 */
public class PostDate implements Serializable {

    public static final String DATE_FORMAT = "yyyyMMdd";
    public final Date value;

    // YYYYMMDDの数値用コンストラクタ
    public PostDate(long value) {
        this.value = setDate(value);
    }

    // Date用コンストラクタ
    private PostDate(Date value) {
        this.value = value;
    }

    public static Observable<PostDate> from(Long value) {
        return Observable.just(new PostDate(value));
    }

    /**
     * Date型からPostDateインスタンスを作成します。
     * @param date
     * @return
     */
    public static PostDate of(Date date) {
        return new PostDate(date);
    }

    /**
     * year, month, day からPostDateインスタンスを作成します。
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static PostDate of(int year, int month, int day) {
        final Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return new PostDate(cal.getTime());
    }

    /**
     * <p>投稿日をyyyyMMddの文字列に変換する</p>
     * @return yyyyMMddの文字列に変換された投稿日
     */
    public String getString() {
        return new SimpleDateFormat(DATE_FORMAT).format(value);
    }

    /**
     * <p>投稿日をCalendarクラスに変換する。</p>
     * @return Calendarクラスに変換された投稿日
     */
    public Calendar toCalendar(){
        Calendar cal = Calendar.getInstance();
        cal.setTime(value);
        return cal;
    }

    private Date setDate(Long postDate) {
        try {
            // 毎回生成しなければならない。残念。
            // http://www.symmetric.co.jp/blog/archives/20
            return new SimpleDateFormat(DATE_FORMAT).parse(Long.toString(postDate));
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

}
