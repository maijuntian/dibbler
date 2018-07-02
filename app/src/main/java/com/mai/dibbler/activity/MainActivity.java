package com.mai.dibbler.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.mai.dibbler.MyApplication;
import com.mai.dibbler.R;
import com.mai.dibbler.base.BaseActivity;
import com.mai.dibbler.base.BaseDelegate;
import com.mai.dibbler.bean.Course;
import com.mai.dibbler.delegate.MainDelegate;
import com.mai.dibbler.download.DownloadUtils;
import com.mai.dibbler.download.DownloadVideosUtils;
import com.mai.dibbler.http.CatService;
import com.mai.dibbler.utils.DialogUtils;
import com.mai.dibbler.utils.Key;
import com.mai.xmai_fast_lib.baseadapter.listener.ROnItemClickListener;
import com.mai.xmai_fast_lib.basehttp.MParams;
import com.mai.xmai_fast_lib.utils.MLog;
import com.mai.xmai_fast_lib.utils.SharedPreferencesHelper;

import java.util.List;

import butterknife.OnClick;
import butterknife.OnLongClick;
import rx.functions.Action0;
import rx.functions.Action1;

public class MainActivity extends BaseActivity<MainDelegate> {

    List<Course> mCourses;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewDelegate.initWaitCourse();


        viewDelegate.etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    courseList();
                    return true;
                }
                return false;
            }
        });


        courseList();
    }


    @Override
    public void videoWaitForPlayListSuccess() {

        viewDelegate.initWaitCourse();
    }

    private void courseList() {

        final String videoCourseName = viewDelegate.etInput.getText().toString();
        int videoSortType = 0;

        final int checkId = viewDelegate.rgOrder.getCheckedRadioButtonId();
        switch (checkId) {
            case R.id.rb_course_name:
                videoSortType = 0;
                break;
            case R.id.rb_course_time:
                videoSortType = 1;
                break;
            case R.id.rb_course_hot:
                videoSortType = 2;
                break;
        }

        CatService.getInstance().allVideoCourseList(new MParams().add("videoCourseName", videoCourseName)
                .add("videoSortType", videoSortType), this)
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        dialog = ProgressDialog.show(MainActivity.this, "", "正在努力加载课程，请稍等...");
                    }
                })
                .subscribe(new Action1<List<Course>>() {
            @Override
            public void call(final List<Course> courses) {
                if(dialog != null)
                    dialog.dismiss();
                mCourses = courses;

                viewDelegate.initCourses(courses, new ROnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Bundle bundle = new Bundle();
                        bundle.putSerializable("course", mCourses.get(position));
                        startActivity(CourseDetailActivity.class, bundle, false);
                    }
                });

                if (TextUtils.isEmpty(videoCourseName))
                    DownloadVideosUtils.getInstance().addVideoUrls(courses);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) { if(dialog != null)
                dialog.dismiss();
                throwable.printStackTrace();
            }
        });
    }

    @OnClick({R.id.rb_course_name, R.id.rb_course_time, R.id.rb_course_hot,})
    public void chooseOrder() {
        courseList();
    }


    @OnClick(R.id.tv_wait_list)
    public void waitList() {
        startActivity(WaitListActivity.class, false);
    }

    @OnLongClick(R.id.tv_main_tip)
    public boolean showSetting() {
        DialogUtils.showSettingDialog(this, new Action0() {
            @Override
            public void call() {
                MyApplication.INSTANCE.getWaitCourseList();
            }
        });
        return true;
    }

    @OnClick(R.id.iv_qrcode)
    public void showQrcode() {
        String deviceId = SharedPreferencesHelper.getInstance(getApplicationContext()).getStringValue(Key.DEVICE_ID);

        if (TextUtils.isEmpty(deviceId)) {
            showToast("联系管理员，输入设备编号...");
            return;
        }
        DialogUtils.showQrcodeDialog(this, deviceId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewDelegate.initWaitCourse();
    }
}
