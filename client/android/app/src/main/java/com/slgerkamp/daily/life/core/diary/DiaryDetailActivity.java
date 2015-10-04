package com.slgerkamp.daily.life.core.diary;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.slgerkamp.daily.life.R;
import com.slgerkamp.daily.life.generic.Backend;
import com.slgerkamp.daily.life.infra.JSONData;
import com.slgerkamp.daily.life.infra.Utils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class DiaryDetailActivity extends AppCompatActivity {

    @InjectView(R.id.message_image) ImageView imageView;
    @InjectView(R.id.message_content) TextView textView;
    DiaryItem item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_diary_detail);
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getEntry((DiaryId) getIntent().getSerializableExtra("diaryId"));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            // ホームボタン
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpImage(JSONData json){
        json.getLong("fileId").subscribe(
                new Action1<Long>() {
                    @Override
                    public void call(Long id) {
                        setUpImageId(id);
                    }
                }
        );
    }

    private void setUpImageId(Long id) {
        new Backend(DiaryDetailActivity.this).imageLoader()
                .load(id).into(imageView);
    }

    private void getEntry(DiaryId diaryId) {
        new Backend(this).get("entry")
                .param("entryId", Long.toString(diaryId.value))
                .toObservable()
                .flatMap(new Func1<JSONData, Observable<List<JSONData>>>() {
                    @Override
                    public Observable<List<JSONData>> call(JSONData json) {
                        return json.getList("entryList");
                    }
                })
                .map(new Func1<List<JSONData>, List<DiaryItem>>() {
                    @Override
                    public List<DiaryItem> call(List<JSONData> jsonData) {

                        ImmutableList.Builder<DiaryItem> builder = new ImmutableList.Builder<>();
                        for (JSONData d : jsonData) {
                            DiaryItem item = DiaryItem.fromJSON(d).toBlocking().lastOrDefault(null);
                            if (item != null) {
                                builder.add(item);
                            } else {
                                Log.e(DiaryFragment.class.getSimpleName(), "invalid data: " + d);
                            }
                        }
                        return builder.build();
                    }
                })
                .subscribe(new Action1<List<DiaryItem>>() {
                    @Override
                    public void call(List<DiaryItem> diaryItems) {
                        item = diaryItems.get(0);
                        getSupportActionBar().setTitle(Utils.localDate(getParent(), item.postDate.value));
                        textView.setText(item.content);
                        if (item.optFileId.isPresent()) {
                            setUpImageId(item.optFileId.get());
                        }
                    }
                });
    }

}
