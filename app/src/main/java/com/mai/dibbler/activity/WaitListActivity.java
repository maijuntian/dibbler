package com.mai.dibbler.activity;

import android.os.Bundle;

import com.mai.dibbler.MyApplication;
import com.mai.dibbler.R;
import com.mai.dibbler.base.BaseActivity;
import com.mai.dibbler.delegate.WaitListDelegate;

import butterknife.OnClick;

/**
 * Created by maijuntian on 2018/6/28.
 */
public class WaitListActivity extends BaseActivity<WaitListDelegate> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDelegate.initWaitCourse();

    }

    @Override
    public void videoWaitForPlayListSuccess() {
        viewDelegate.initWaitCourse();
    }

    @OnClick(R.id.iv_return)
    public void back() {
        finish();
    }
}
