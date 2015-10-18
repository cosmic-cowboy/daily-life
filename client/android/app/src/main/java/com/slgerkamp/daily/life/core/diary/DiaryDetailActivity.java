package com.slgerkamp.daily.life.core.diary;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.common.collect.ImmutableList;
import com.slgerkamp.daily.life.R;
import com.slgerkamp.daily.life.generic.Backend;
import com.slgerkamp.daily.life.generic.ImageFullscreenDialog;
import com.slgerkamp.daily.life.infra.JSONData;
import com.slgerkamp.daily.life.infra.Utils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * <p>日記の詳細ページ</p>
 */
public class DiaryDetailActivity extends AppCompatActivity {

    private static final String PARAM_DIARY_ID  = "diaryId";
    private static final String PARAM_POST_DATE = "postDate";
    public static final int CALL_DIARY_DETAIL_ACTIVITY_REQUEST_CODE = 124;

    @InjectView(R.id.message_image) ImageView imageView;
    @InjectView(R.id.message_content) TextView textView;
    private DiaryId diaryId;

    /**
     * <p>日記編集画面を開きます。</p>
     */
    public static void openFromFragment(Activity activity, Fragment fragment, DiaryId diaryId) {
        Intent intent = new Intent(activity, DiaryDetailActivity.class);
        intent.putExtra(PARAM_DIARY_ID, diaryId);
        fragment.startActivityForResult(intent, CALL_DIARY_DETAIL_ACTIVITY_REQUEST_CODE);
    }

    /**
     * <p>日記編集画面を開きます。</p>
     */
    public static void openFromFragment(Activity activity, Fragment fragment, PostDate postDate) {
        Intent intent = new Intent(activity, DiaryDetailActivity.class);
        intent.putExtra(PARAM_POST_DATE, postDate);
        fragment.startActivityForResult(intent, CALL_DIARY_DETAIL_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_diary_detail);
        ButterKnife.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getSerializableExtra(PARAM_DIARY_ID) != null){
            diaryId = (DiaryId) getIntent().getSerializableExtra(PARAM_DIARY_ID);
            getEntry(diaryId);
        };
        if (getIntent().getSerializableExtra(PARAM_POST_DATE) != null){
            PostDate postDate = (PostDate)getIntent().getSerializableExtra(PARAM_POST_DATE);
            getEntry(postDate);
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            // ホームボタン
            case android.R.id.home:
                setResult(RESULT_CANCELED);
                finish();
                return true;

            // 削除ボタン
            case R.id.delete_diary:
                deleteDiary();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * <p>日記を削除する</>
     * <p>
     *     日記を削除するか確認
     *      - OKの場合、削除処理を実行
     *         - 正常終了の場合、一覧画面に遷移
     *         - 異常終了の場合、そのまま
     *
     * </>
     */
    private void deleteDiary() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.confirm_delete))
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new Backend(DiaryDetailActivity.this).delete("entry")
                                .param("entryId", Long.toString(diaryId.value))
                                .toObservable()
                                .subscribe(
                                        new Action1<JSONData>() {
                                            @Override
                                            public void call(JSONData jsonData) {
                                                finish();
                                            }
                                        },
                                        new Action1<Throwable>() {
                                            @Override
                                            public void call(Throwable throwable) {
                                                new AlertDialog.Builder(DiaryDetailActivity.this)
                                                        .setMessage(getString(R.string.notify_not_delete))
                                                        .setPositiveButton(getString(R.string.ok), null)
                                                        .show();
                                            }
                                        });


                    }
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
    }

    /**
     * <p>指定された日記の日付から日記情報を取得し、タイトル、日記内容をViewに配置する</p>
     * @param postDate
     */
    private void getEntry(PostDate postDate) {
        Observable<JSONData> observableJSONDate = new Backend(this).get("entry")
                .param("postDate", postDate.getString())
                .toObservable();

        setEntry(observableJSONDate);
    }
    /**
     * <p>指定された日記IDから日記情報を取得し、タイトル、日記内容をViewに配置する</p>
     * @param diaryId
     */
    private void getEntry(DiaryId diaryId) {
        Observable<JSONData> observableJSONDate = new Backend(this).get("entry")
                .param("entryId", Long.toString(diaryId.value))
                .toObservable();

        setEntry(observableJSONDate);
    }

    /**
     * <p>日記一覧リクエストのレスポンスから描画を担当する。</p>
     * @param observableJSONDate
     */
    private void setEntry(Observable<JSONData> observableJSONDate) {

        observableJSONDate
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
                        DiaryItem item = diaryItems.get(0);
                        getSupportActionBar().setTitle(Utils.localDate(getParent(), item.postDate.value));
                        textView.setText(item.content);
                        if (item.optFileId.isPresent()) {
                            setUpImageId(item.optFileId.get());
                        }
                    }
                });
    }

    /**
     * <p>指定されたファイルIDの画像を取得し、Viewに配置する</p>
     * @param id
     */
    private void setUpImageId(final Long id) {
        int displaySize = getResources().getDimensionPixelSize(R.dimen.list_image_size);
        imageView.getLayoutParams().height = displaySize;
        int paddingSize = getResources().getDimensionPixelSize(R.dimen.list_image_padding_size);
        imageView.setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
        new Backend(DiaryDetailActivity.this).imageLoader()
                .load(id).into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageFullscreenDialog
                        .newInstance(id)
                        .show(DiaryDetailActivity.this.getFragmentManager(), "imageFullScreen");
            }
        });
    }


}
