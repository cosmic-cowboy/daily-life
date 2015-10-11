package com.slgerkamp.daily.life.infra;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

public class DiaryDatePickerDialog extends DatePickerDialog {

    public static DiaryDatePickerDialog newInstance(DatePickerDialog.OnDateSetListener callBack, int year, int monthOfYear, int dayOfMonth) {
        DiaryDatePickerDialog ret = new DiaryDatePickerDialog();
        ret.initialize(callBack, year, monthOfYear, dayOfMonth);
        return ret;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // OK、CANCELボタンの非表示
        LinearLayout linearLayout = (LinearLayout) view.findViewById(com.wdullaer.materialdatetimepicker.R.id.done_background);
        linearLayout.getLayoutParams().height = 0;
        linearLayout.setVisibility(View.INVISIBLE);

        return view;
    }

}
