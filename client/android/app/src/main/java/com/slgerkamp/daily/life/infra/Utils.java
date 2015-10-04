package com.slgerkamp.daily.life.infra;

import android.content.Context;
import android.text.format.DateFormat;

import java.util.Date;

/**
 * ユーティリティクラス
 */
public class Utils {

    private Utils(){}

    // ロケールに合わせた日付表示
    public static String localDate(Context context, Date date){
        return DateFormat.getLongDateFormat(context).format(date);
    }

}
