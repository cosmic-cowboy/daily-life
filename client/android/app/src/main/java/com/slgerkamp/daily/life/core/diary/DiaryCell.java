package com.slgerkamp.daily.life.core.diary;

import android.text.format.DateFormat;
import android.view.View;
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
    }

}