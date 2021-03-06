package com.mai.dibbler;

import android.app.Activity;
import android.text.TextUtils;

import com.mai.dibbler.base.BaseActivity;
import com.mai.dibbler.bean.Course;
import com.mai.dibbler.bean.WaitCourse;
import com.mai.dibbler.http.CatService;
import com.mai.dibbler.utils.Key;
import com.mai.xmai_fast_lib.base.BaseApplication;
import com.mai.xmai_fast_lib.basehttp.MParams;
import com.mai.xmai_fast_lib.utils.SharedPreferencesHelper;
import com.mai.xmai_fast_lib.utils.XAppManager;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action1;

public class MyApplication extends BaseApplication {

    public Course mSelCourse;

    public int selIndex;

    public WaitCourse mWaitCourse;

    public static MyApplication INSTANCE;

    private Subscription sbWaitCourse;

    private final long PERIOD = 60 * 1000;

    @Override

    public String getBuglyAppid() {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        sbWaitCourse = Observable.interval(0, PERIOD, TimeUnit.MILLISECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                getWaitCourseList();
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

        getWaitCourseList();
    }

    public void getWaitCourseList() {
        String lifeId = SharedPreferencesHelper.getInstance(getApplicationContext()).getStringValue(Key.LIFE_ID);
        if (!TextUtils.isEmpty(lifeId)) {
            CatService.getInstance().videoWaitForPlayList(new MParams().add("lifeNo", lifeId), getApplicationContext())
                    .subscribe(new Action1<WaitCourse>() {
                        @Override
                        public void call(WaitCourse waitCourse) {
                            mWaitCourse = waitCourse;

                            XAppManager.getInstance().doInAllActivity(new XAppManager.DoAllActivityListener() {
                                @Override
                                public void doAll(Activity act) {
                                    ((BaseActivity) act).videoWaitForPlayListSuccess();
                                }
                            });
                        }
                    });
        }
    }

    public void setSelCourse(WaitCourse waitCourse) {

//        if (this.mWaitCourse == null || waitCourse == null) {
        this.mWaitCourse = waitCourse;
        /*} else {
            for (int i = 0, size = waitCourse.getVideoWaitList().size(); i < size; i++) {

                if (i == mWaitCourse.getVideoWaitList().size()) { //判定最后一个是新增的
                    selIndex = i;
                    mSelCourse = waitCourse.getVideoWaitList().get(i);
                    break;
                }

                String oldStartTime = mWaitCourse.getVideoWaitList().get(i).getVideoStartTime();
                String newStartTime = waitCourse.getVideoWaitList().get(i).getVideoStartTime();

                if (!oldStartTime.equals(newStartTime)) { //开始时间不一致，判定是新增的
                    selIndex = i;
                    mSelCourse = waitCourse.getVideoWaitList().get(i);
                }
            }
        }*/
        XAppManager.getInstance().doInAllActivity(new XAppManager.DoAllActivityListener() {
            @Override
            public void doAll(Activity act) {
                ((BaseActivity) act).videoWaitForPlayListSuccess();
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        if (sbWaitCourse != null)
            sbWaitCourse.unsubscribe();
    }
}
