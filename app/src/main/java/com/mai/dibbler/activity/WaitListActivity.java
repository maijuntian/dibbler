package com.mai.dibbler.activity;

import android.os.Bundle;
import android.view.View;

import com.mai.dibbler.MyApplication;
import com.mai.dibbler.R;
import com.mai.dibbler.base.BaseActivity;
import com.mai.dibbler.delegate.WaitListDelegate;
import com.mai.xmai_fast_lib.baseadapter.listener.ROnItemClickListener;

import butterknife.OnClick;

/**
 * Created by maijuntian on 2018/6/28.
 */
public class WaitListActivity extends BaseActivity<WaitListDelegate> {

    ROnItemClickListener itemClickListener = new ROnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("course", MyApplication.INSTANCE.mWaitCourse.getVideoWaitList().get(position));
            startActivity(CourseDetailActivity.class, bundle, false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        boolean isShowInsert = getIntent().getBooleanExtra("isShow", false);

        if(isShowInsert){
            viewDelegate.showInsert(itemClickListener);
        } else {

            viewDelegate.initWaitCourse(itemClickListener);
        }

    }

    @Override
    public void videoWaitForPlayListSuccess() {
        viewDelegate.initWaitCourse(itemClickListener);
    }

    @OnClick(R.id.iv_return)
    public void back() {
        finish();
    }
}
