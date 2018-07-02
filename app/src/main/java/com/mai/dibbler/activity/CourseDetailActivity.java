package com.mai.dibbler.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;

import com.mai.dibbler.MyApplication;
import com.mai.dibbler.R;
import com.mai.dibbler.base.BaseActivity;
import com.mai.dibbler.bean.Course;
import com.mai.dibbler.bean.WaitCourse;
import com.mai.dibbler.delegate.CourseDetailDelegate;
import com.mai.dibbler.http.CatService;
import com.mai.dibbler.utils.Key;
import com.mai.xmai_fast_lib.basehttp.MParams;
import com.mai.xmai_fast_lib.utils.SharedPreferencesHelper;

import butterknife.OnClick;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by maijuntian on 2018/6/28.
 */
public class CourseDetailActivity extends BaseActivity<CourseDetailDelegate> {

    Course mCourse;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCourse = (Course) getIntent().getSerializableExtra("course");
        viewDelegate.showCourse(mCourse);
    }

    @OnClick(R.id.iv_return)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_play)
    public void play() {

        String lifeId = SharedPreferencesHelper.getInstance(this).getStringValue(Key.LIFE_ID);
        if (TextUtils.isEmpty(lifeId)) {
            showToast("请找管理员，输入场馆号...");
            return;
        }

        CatService.getInstance().videoOnDemand(new MParams()
                .add("lifeId", lifeId)
                .add("videoId", mCourse.getVideoId()), this)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        dialog = ProgressDialog.show(CourseDetailActivity.this, "", "正在努力点播课程，请稍等...");
                    }
                })
                .subscribe(new Action1<WaitCourse>() {
                    @Override
                    public void call(WaitCourse waitCourse) {
                        if (dialog != null)
                            dialog.dismiss();
                        MyApplication.INSTANCE.setSelCourse(waitCourse);
                        startActivity(WaitListActivity.class, true);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (dialog != null)
                            dialog.dismiss();
                        throwable.printStackTrace();
                    }
                });
    }
}
