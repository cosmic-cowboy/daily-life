package com.slgerkamp.daily.life.core.diary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.slgerkamp.daily.life.R;
import com.slgerkamp.daily.life.generic.Backend;
import com.slgerkamp.daily.life.infra.DiaryDatePickerDialog;
import com.slgerkamp.daily.life.infra.JSONData;
import com.slgerkamp.daily.life.infra.OnDiaryDatePickerClickListener;
import com.slgerkamp.daily.life.infra.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Bind;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * <p>日記の一覧画面を管理するクラスです。
 */
public class DiaryFragment extends Fragment implements AbsListView.OnItemClickListener{

    @Bind(android.R.id.list) AbsListView listView;
    @Bind(R.id.first_fab) FloatingActionButton fab;

    private DiaryAdapter diaryAdapter;

    private Map<Integer, DiaryItem> entityMap;
    private Optional<List<Calendar>> optPostDateCalendar = Optional.absent();

    public DiaryFragment() {
        entityMap = new HashMap<>();
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEntry();
        getPostDate();
        diaryAdapter = new DiaryAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diary_fragment, container, false);
        setHasOptionsMenu(true);

        ButterKnife.bind(this, view);

        listView.setAdapter(diaryAdapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_diary, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.open_calendar_dialog:

                // カレンダーを表示します。
                // ハイライトされた日付がタップされた場合は、詳細画面
                // ハイライトされていない日付がタップされた場合は、新規作成画面
                // に遷移します。
                Calendar now = Calendar.getInstance();
                DiaryDatePickerDialog dpd = DiaryDatePickerDialog.newInstance(
                        new OnDiaryDatePickerClickListener() {
                            @Override
                            public void onHighlightedDayOfMonthSelected(PostDate postDate) {
                                DiaryDetailActivity.openFromFragment(getActivity(), DiaryFragment.this, postDate);
                            }
                            @Override
                            public void onNotHighlightedDayOfMonthSelected(PostDate postDate) {
                                DiaryEditActivity.openFromFragmentUsingPostDate(getActivity(), DiaryFragment.this, postDate);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH),
                        optPostDateCalendar
                );
                dpd.show(getFragmentManager(), "DiaryDatePickerDialog");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (optPostDateCalendar.isPresent()) {
                    boolean hasDate = Utils.hasDate(optPostDateCalendar.get(), PostDate.of(new Date()));
                    if (hasDate) {
                        new AlertDialog.Builder(getActivity())
                                .setMessage(getString(R.string.notify_already_post))
                                .setPositiveButton(getString(R.string.ok), null)
                                .show();
                        return;
                    }
                }
                PostDate postDate = PostDate.of(new Date());
                DiaryEditActivity.openFromFragmentUsingPostDate(getActivity(), DiaryFragment.this, postDate);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case DiaryEditActivity.CALL_DIARY_EDIT_ACTIVITY_REQUEST_CODE:
                getEntry();
                getPostDate();
                break;
            case DiaryDetailActivity.CALL_DIARY_DETAIL_ACTIVITY_REQUEST_CODE:
                getEntry();
                getPostDate();
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Activity activity = getActivity();
        Object item = entityMap.get(position);
        if (item instanceof DiaryItem) {
            DiaryDetailActivity.openFromFragment(getActivity(), this, ((DiaryItem) item).diaryId, ((DiaryItem) item).postDate );
        }
    }


    private class DiaryAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return entityMap.size();
        }

        @Override
        public Object getItem(int position) {
            return entityMap.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi = LayoutInflater.from(getActivity());
                v = vi.inflate(R.layout.diary_cell, parent, false);
            }

            DiaryCell cell = new DiaryCell(v);
            cell.setItem(entityMap.get(position), getActivity(), new Backend(getActivity()).imageLoader());

            return v;
        }
    }

    private void getEntry() {
        new Backend(getActivity()).get("entry")
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
                        entityMap.clear();
                        for(int i = 0; i <  diaryItems.size(); i++){
                            Log.d("JSON:", diaryItems.get(i).toString());
                            entityMap.put(i, diaryItems.get(i));
                        }
                        diaryAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * <p>日記の投稿日を取得する</p>
     */
    private void getPostDate() {
        new Backend(getActivity()).get("entry/postdate")
                .toObservable()
                .flatMap(new Func1<JSONData, Observable<List<JSONData>>>() {
                    @Override
                    public Observable<List<JSONData>> call(JSONData json) {
                        return json.getList("entryList");
                    }
                })
                .map(new Func1<List<JSONData>, List<PostDate>>() {
                    @Override
                    public List<PostDate> call(List<JSONData> jsonData) {

                        ImmutableList.Builder<PostDate> builder = new ImmutableList.Builder<>();
                        for (JSONData d : jsonData) {
                            PostDate postDate = d.getLong("postDate").flatMap(new Func1<Long, Observable<PostDate>>() {
                                @Override
                                public Observable<PostDate> call(Long l) {
                                    return PostDate.from(l);
                                }
                            }).toBlocking().lastOrDefault(null);
                            if (postDate != null) {
                                builder.add(postDate);
                            } else {
                                Log.e(DiaryFragment.class.getSimpleName(), "invalid data: " + d);
                            }
                        }
                        return builder.build();
                    }
                })
                .subscribe(new Action1<List<PostDate>>() {
                    @Override
                    public void call(List<PostDate> postDates) {
                        List<Calendar> postDateCalendar = Observable.from(postDates)
                                .map(new Func1<PostDate, Calendar>() {
                                    @Override
                                    public Calendar call(PostDate postDate) {
                                        return postDate.toCalendar();
                                    }
                                })
                                .toList().toBlocking().single();
                        optPostDateCalendar = Optional.of(postDateCalendar);
                    }
                });
    }

}
