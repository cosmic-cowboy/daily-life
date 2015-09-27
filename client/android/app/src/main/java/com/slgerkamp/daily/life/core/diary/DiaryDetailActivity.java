package com.slgerkamp.daily.life.core.diary;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.base.Optional;
import com.slgerkamp.daily.life.MainActivity;
import com.slgerkamp.daily.life.R;
import com.slgerkamp.daily.life.generic.Backend;
import com.slgerkamp.daily.life.generic.ImageSelectionWizard;
import com.slgerkamp.daily.life.infra.JSONData;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;
import rx.functions.Func1;

public class DiaryDetailActivity extends AppCompatActivity {

    @InjectView(R.id.message_image) ImageView imageView;
    @InjectView(R.id.message_content) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DiaryItem item = (DiaryItem)getIntent().getSerializableExtra("diaryItem");

        setContentView(R.layout.activity_diary_detail);
        ButterKnife.inject(this);

        textView.setText(item.content);
        if (item.optFileId.isPresent()) {
            setUpImageId(item.optFileId.get());
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Long.toString(new Date().getTime()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

}
