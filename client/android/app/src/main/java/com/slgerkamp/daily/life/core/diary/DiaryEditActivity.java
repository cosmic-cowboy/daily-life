package com.slgerkamp.daily.life.core.diary;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.common.base.Optional;
import com.slgerkamp.daily.life.MainActivity;
import com.slgerkamp.daily.life.R;
import com.slgerkamp.daily.life.generic.Backend;
import com.slgerkamp.daily.life.generic.ImageSelectionWizard;
import com.slgerkamp.daily.life.infra.JSONData;
import com.slgerkamp.daily.life.infra.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.functions.Action1;
import rx.functions.Func1;

public class DiaryEditActivity extends AppCompatActivity {

    @InjectView(R.id.message_image) ImageView imageView;
    @InjectView(R.id.message_input) EditText editText;
    Optional<Long> optFileId;
    PostDate postDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        optFileId = Optional.absent();
        postDate = PostDate.of(new Date());
        setContentView(R.layout.activity_diary_edit);
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Utils.localDate(this, postDate.value));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
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
            //
            case R.id.open_image_dialog:
                ImageSelectionWizard wizard = ImageSelectionWizard.create(100);
                wizard.result.subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) {
                        updateImage(bitmap);
                    }
                });
                wizard.show(getFragmentManager(), MainActivity.class.getSimpleName());

                return true;
            // 保存ボタン
            case R.id.post_newDiary:
                postEntry(editText.getText().toString(), postDate.getString());
                setResult(RESULT_OK);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void postEntry(String content, String postDate) {

        Backend.Post post = new Backend(this).post("entry")
                .param("content", content)
                .param("postDate", postDate);
        if (optFileId.isPresent()){
            post.param("fileId", Long.toString(optFileId.get()));
        }
        post.toObservable()
                .map(new Func1<JSONData, DiaryItem>() {
                    @Override
                    public DiaryItem call(JSONData json) {
                        return DiaryItem.fromJSON(json).toBlocking().lastOrDefault(null);
                    }
                })
                .subscribe(new Action1<DiaryItem>() {
                    @Override
                    public void call(DiaryItem diaryItem) {
                        Log.e(DiaryEditActivity.class.getSimpleName(), "送信完了 : " + diaryItem);
                    }
                });
    }

    private void updateImage(Bitmap bitmap) {
        //
        // 1. アップロードする画像のバイナリを作成
        //
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, buffer);

        //
        // 2. アップロード
        //
        new Backend(this).upload("entry/image", new ByteArrayInputStream(buffer.toByteArray())).subscribe(
                new Action1<JSONData>() {
                    @Override
                    public void call(JSONData jsonData) {
                        setUpImage(jsonData);
                    }
                }
        );
    }

    private void setUpImage(JSONData json){
        json.getLong("fileId").subscribe(
                new Action1<Long>() {
                    @Override
                    public void call(Long id) {
                        new Backend(DiaryEditActivity.this).imageLoader()
                                .load(id).into(imageView);
                        optFileId = Optional.of(id);
                    }
                }
        );
    }


}
