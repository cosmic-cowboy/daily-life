package com.slgerkamp.daily.life.core.diary;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.common.base.Optional;
import com.slgerkamp.daily.life.MainActivity;
import com.slgerkamp.daily.life.R;
import com.slgerkamp.daily.life.generic.Backend;
import com.slgerkamp.daily.life.generic.ImageFullscreenDialog;
import com.slgerkamp.daily.life.generic.ImageSelectionWizard;
import com.slgerkamp.daily.life.infra.JSONData;
import com.slgerkamp.daily.life.infra.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.Bind;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class DiaryEditActivity extends AppCompatActivity {

    private static final String PARAM_POST_DATE = "postDate";
    public static final int CALL_DIARY_EDIT_ACTIVITY_REQUEST_CODE = 123;

    @Bind(R.id.message_image) ImageView imageView;
    @Bind(R.id.message_input) EditText editText;
    Optional<Long> optFileId;
    PostDate postDate;

    /**
     * <p>日記編集画面を開きます。</p>
     */
    public static void openFromFragmentUsingPostDate(Activity activity, Fragment fragment, PostDate postDate) {
        Intent intent = new Intent(activity, DiaryEditActivity.class);
        intent.putExtra(PARAM_POST_DATE, postDate);
        fragment.startActivityForResult(intent, CALL_DIARY_EDIT_ACTIVITY_REQUEST_CODE);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        optFileId = Optional.absent();
        setContentView(R.layout.activity_diary_edit);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        postDate = (PostDate)getIntent().getSerializableExtra(PARAM_POST_DATE);

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
                Observable post = postEntry(editText.getText().toString(), postDate);

                post.subscribe(
                        new Action1<JSONData>() {
                            @Override
                            public void call(JSONData json) {
                                Log.v(DiaryEditActivity.class.getSimpleName(), "送信完了 : " + json);
                                setResult(RESULT_OK);
                                finish();
                            }
                        },
                        new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                new AlertDialog.Builder(DiaryEditActivity.this)
                                        .setMessage(getString(R.string.notify_not_save))
                                        .setPositiveButton(getString(R.string.ok), null)
                                        .show();
                            }
                        }

                );

        }

        return super.onOptionsItemSelected(item);
    }


    private Observable postEntry(String content, PostDate postDate) {

        Backend.Post post = new Backend(this).post("entry")
                .param("content", content)
                .param("postDate", postDate.getString());
        if (optFileId.isPresent()){
            post.param("fileId", Long.toString(optFileId.get()));
        }
        return post.toObservable();
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

        int displaySize = getResources().getDimensionPixelSize(R.dimen.list_image_size);
        imageView.getLayoutParams().height = displaySize;
        int paddingSize = getResources().getDimensionPixelSize(R.dimen.list_image_padding_size);
        imageView.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);

        json.getLong("fileId").subscribe(
                new Action1<Long>() {
                    @Override
                    public void call(Long id) {
                        new Backend(DiaryEditActivity.this).imageLoader()
                                .load(id).into(imageView);
                        optFileId = Optional.of(id);

                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageFullscreenDialog
                                        .newInstance(optFileId.get())
                                        .show(DiaryEditActivity.this.getFragmentManager(), "imageFullScreen");
                            }
                        });

                    }
                }
        );
    }


}
