package com.slgerkamp.daily.life.core.diary;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.slgerkamp.daily.life.R;
import com.slgerkamp.daily.life.generic.Backend;
import com.slgerkamp.daily.life.infra.Utils;

import butterknife.ButterKnife;
import butterknife.Bind;

/**
 * <p>日記一覧のリストアイテムを管理するクラスです。
 */
public class DiaryCell {

    @Bind(R.id.post_date) TextView post_date;
    @Bind(R.id.content) TextView content;
    @Bind(R.id.picture) ImageView picture;

    public DiaryCell(View view){
        ButterKnife.bind(this, view);
    }

    public void setItem(DiaryItem item, Context context, Backend.ImageLoader loader) {

        post_date.setText(Utils.localDate(context, item.postDate.value));
        content.setText(item.content);
        ellipsizeMultilineText(content, 2);
        if (item.optFileId.isPresent()) {
            int displaySize = context.getResources().getDimensionPixelSize(R.dimen.list_image_size);
            picture.getLayoutParams().height = displaySize;
            picture.getLayoutParams().width = displaySize;
            int paddingSize = context.getResources().getDimensionPixelSize(R.dimen.list_image_padding_size);
            picture.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
            loader.load(item.optFileId.get()).into(picture);
        } else {
            int defaultSize = context.getResources().getDimensionPixelSize(R.dimen.list_image_default_size);
            picture.getLayoutParams().height = defaultSize;
            picture.getLayoutParams().width = defaultSize;
            picture.setPadding(defaultSize, defaultSize, defaultSize, defaultSize);
            picture.setImageBitmap(null);
        }
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
