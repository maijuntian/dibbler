package com.mai.dibbler.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;

import com.mai.dibbler.R;
import com.mai.dibbler.base.BaseActivity;
import com.mai.dibbler.base.BaseDelegate;
import com.mai.dibbler.bean.Course;
import com.mai.dibbler.delegate.MainDelegate;
import com.mai.dibbler.http.CatService;
import com.mai.xmai_fast_lib.basehttp.MParams;
import com.mai.xmai_fast_lib.utils.MLog;

import java.util.List;

import rx.functions.Action1;

public class MainActivity extends BaseActivity<MainDelegate> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display mDisplay = getWindowManager().getDefaultDisplay();
        int width = mDisplay.getWidth();
        int height = mDisplay.getHeight();
        showToast("Screen Default Ratio: [" + width + "x" + height + "]");

      /*  viewDelegate.initWaitCourse();

        courseList();*/
    }


    @Override
    public void videoWaitForPlayListSuccess() {

        viewDelegate.initWaitCourse();
    }

    private void courseList() {

        final String videoCourseName = viewDelegate.etInput.getText().toString();
        int videoSortType = 0;

        int checkId = viewDelegate.rgOrder.getCheckedRadioButtonId();
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
                .add("videoSortType", videoSortType), this).subscribe(new Action1<List<Course>>() {
            @Override
            public void call(List<Course> courses) {
                viewDelegate.initCourses(courses);

                if (TextUtils.isEmpty(videoCourseName)) {

                }
            }
        });
    }
}
