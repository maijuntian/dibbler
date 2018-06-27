package com.mai.dibbler;

import android.app.Activity;
import android.text.TextUtils;

import com.mai.dibbler.base.BaseActivity;
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
        String deviceId = SharedPreferencesHelper.getInstance(getApplicationContext()).getStringValue(Key.DEVICE_ID);
        if (!TextUtils.isEmpty(deviceId)) {
            CatService.getInstance().videoWaitForPlayList(new MParams().add("deviceId", deviceId), getApplicationContext())
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

    @Override
    public void onTerminate() {
        super.onTerminate();

        if (sbWaitCourse != null)
            sbWaitCourse.unsubscribe();
    }
}
