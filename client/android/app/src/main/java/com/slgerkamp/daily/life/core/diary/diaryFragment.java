package com.slgerkamp.daily.life.core.diary;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import com.slgerkamp.daily.life.R;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
        entityMap.put(0, new DiaryItem(new DiaryId(123456), "日記56", 123456));
        entityMap.put(1, new DiaryItem(new DiaryId(123457), "日記57", 123457));
        entityMap.put(2, new DiaryItem(new DiaryId(123458), "日記58", 123458));
        entityMap.put(3, new DiaryItem(new DiaryId(123459), "日記59", 123459));
        entityMap.put(4, new DiaryItem(new DiaryId(123460), "日記60", 123460));
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
}
