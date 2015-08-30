package com.slgerkamp.daily.life.core.diary;

import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.slgerkamp.daily.life.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * <p>日記一覧のリストアイテムを管理するクラスです。
 */
public class DiaryCell {

    @InjectView(R.id.post_date) TextView post_date;
    @InjectView(R.id.content) TextView content;

    public DiaryCell(View view){
        ButterKnife.inject(this, view);
    }

    public void setItem(DiaryItem item) {
        post_date.setText(DateFormat.format("M/d", item.postDate).toString());
        content.setText(item.content);
        ellipsizeMultilineText(content, 2);
    }

    /**
     *
     */
    public static void ellipsizeMultilineText(final TextView textView, final int maxLines) {
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                doEllipsizeMultilineText(textView, maxLines);
            }
        });
    }

    private static void doEllipsizeMultilineText(TextView view, int maxLines) {
        if (maxLines >= view.getLineCount()) return;

        // 行の長さをピクセル数で計算?
        float avail = 0.0f;
        for (int i = 0; i < maxLines; i++) {
            avail += view.getLayout().getLineMax(i);
        }

        // テキストを更新
        CharSequence ellipsizedText = TextUtils.ellipsize(
                view.getText(),
                view.getPaint(),
                avail,
                TextUtils.TruncateAt.END
        );
        view.setText(ellipsizedText);
    }
}
