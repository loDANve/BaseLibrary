package com.chanxa.wnb.fragment.airticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.airticket.CalendarActivity;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.chanxa.wnb.view.calendar.CalendarPickerView;
import com.wtm.library.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 *
 */
public class OneWayFragment extends BaseFragment implements View.OnClickListener {

    @InjectView(R.id.rl_date)
    protected RelativeLayout rl_date;
    private ProgressDialog progressDialog;
    private CalendarPickerView calendar;
    private static OneWayFragment oneWayFragment;

    public OneWayFragment() {

    }

    public static OneWayFragment getInstance() {
        if (oneWayFragment == null) {
            oneWayFragment = new OneWayFragment();
        }
        return oneWayFragment;
    }

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = ProgressDialogBuilder.builderDialog(getActivity());
        return inflater.inflate(R.layout.fragment_airticket_oneway, null, false);
    }

    @Override
    public void initView() {
        super.initView();
        ButterKnife.inject(this, rootView);
        rl_date.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.rl_date:
                intent.setClass(getActivity(), CalendarActivity.class);
                getActivity().startActivity(intent);
                break;
        }
        startActivity(intent);
    }
}