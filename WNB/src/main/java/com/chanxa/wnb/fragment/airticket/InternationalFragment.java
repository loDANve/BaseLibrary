package com.chanxa.wnb.fragment.airticket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chanxa.wnb.R;
import com.chanxa.wnb.activity.MemberDataActivity;
import com.chanxa.wnb.view.ProgressDialogBuilder;
import com.wtm.library.base.BaseFragment;

/**
 * Created by wtm on 2014/12/12.
 */
public class InternationalFragment extends BaseFragment implements View.OnClickListener {

    private ProgressDialog progressDialog;

    @Override
    protected View inflaterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        progressDialog = ProgressDialogBuilder.builderDialog(getActivity());
        return inflater.inflate(R.layout.fragment_home_content, null, false);
    }

    @Override
    public void initView() {
        super.initView();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.container_memberData:
                intent.setClass(getActivity(), MemberDataActivity.class);
                break;
        }
        startActivity(intent);
    }
}