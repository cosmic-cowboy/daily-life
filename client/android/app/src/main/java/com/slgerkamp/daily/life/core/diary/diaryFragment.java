package com.slgerkamp.daily.life.core.diary;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.google.common.collect.ImmutableList;
import com.slgerkamp.daily.life.R;
import com.slgerkamp.daily.life.generic.Backend;
import com.slgerkamp.daily.life.infra.JSONData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * <p>日記の一覧画面を管理するクラスです。
 */
public class DiaryFragment extends Fragment implements AbsListView.OnItemClickListener {

    @InjectView(android.R.id.list) AbsListView listView;

    private DiaryAdapter diaryAdapter;

    // TODO リストが他にできたら汎用的なEntityListをつくる
    private Map<Integer, DiaryItem> entityMap;

    public DiaryFragment() {
        entityMap = new HashMap<>();
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getEntry();
        diaryAdapter = new DiaryAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.diary_fragment, container, false);

        ButterKnife.inject(this, view);

        listView.setAdapter(diaryAdapter);

        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

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
            cell.setItem(entityMap.get(position));

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

                        for(int i = 0; i <  diaryItems.size(); i++){
                            Log.d("JSON:", diaryItems.get(i).toString());
                            entityMap.put(i, diaryItems.get(i));
                        }
                        diaryAdapter.notifyDataSetChanged();
                    }
                });
    }
}
