package com.slgerkamp.daily.life.core.diary;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.slgerkamp.daily.life.R;
import com.slgerkamp.daily.life.generic.Backend;
import com.slgerkamp.daily.life.infra.JSONData;

import java.util.Date;

import butterknife.InjectView;
import rx.functions.Action1;
import rx.functions.Func1;

public class DiaryEditActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        editText = (EditText) findViewById(R.id.message_input);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(Long.toString(new Date().getTime()));
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
        int id = item.getItemId();

        if (id == R.id.post_newDiary) {
            postEntry(editText.getText().toString());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void postEntry(String content) {

        new Backend(this).post("entry")
                .param("userId", "1")
                .param("messageType","日記")
                .param("content", content)
                .toObservable()
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
}