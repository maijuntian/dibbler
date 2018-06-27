package com.mai.dibbler.http;

import android.app.ProgressDialog;
import android.content.Context;

import com.mai.dibbler.bean.Course;
import com.mai.dibbler.bean.WaitCourse;
import com.mai.xmai_fast_lib.basehttp.BaseRetrofitService;
import com.mai.xmai_fast_lib.basehttp.MParams;
import com.mai.xmai_fast_lib.exception.ServerException;
import com.mai.xmai_fast_lib.utils.MLog;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class CatService extends BaseRetrofitService<CatApi> {

    static CatService instance;
//    private LoadingDialog dialog;

    ProgressDialog pdLoading;

    public static CatService getInstance() {
        if (instance == null) {
            synchronized (CatService.class) {
                if (instance == null) {
                    instance = new CatService();
                }
            }
        }
        return instance;
    }

    @Override
    protected String getBaseUrl() {
        return "http://betamanagelife2.healthmall.cn:80/gateway/sail/api/";
    }


    @Override
    protected void showDialog(Context context) {
     /*   if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        dialog = new LoadingDialog(context);
        dialog.show();*/
    }

    protected void showDialog(Context context, String msg) {
        if (pdLoading != null && pdLoading.isShowing())
            pdLoading.dismiss();
        pdLoading = new ProgressDialog(context);
        pdLoading.setCancelable(false);
        pdLoading.setMessage(msg);
        pdLoading.show();
    }

    @Override
    protected void dismissDialog() {
       /* if (dialog != null)
            dialog.dismiss();*/
    }

    /**
     * loading框
     *
     * @param observable
     * @param context
     */
    protected <M> Observable<M> showDialog(Observable<M> observable, final String msg, final Context context) {
        return observable.doOnSubscribe(new Action0() {
            @Override
            public void call() {
                showDialog(context, msg);
            }
        }).doOnCompleted(new Action0() {
            @Override
            public void call() {
                dismissDialog();
            }
        });
    }


    /**
     * 检查返回值
     *
     * @param observable
     * @param <M>
     */
    protected <M> Observable<M> checkResult2(Observable<CatRespone<M>> observable) {
        return observable.map(new Func1<CatRespone<M>, M>() {
            @Override
            public M call(CatRespone<M> response) {
                MLog.log("访问结果", response.toString());
                if (response.getCode() != 2000) {
                    ServerException serverException = new ServerException(response.getMsg());
                    serverException.setCode(response.getCode());
                    throw serverException;
                }
                return response.getData();
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
    }


    protected <M> Observable<M> checkAll2(Observable<CatRespone<M>> observable, Context context) {
        return checkError(showDialog(checkNetWork(checkResult2(observable), context), context), context);
    }

    protected <M> Observable<M> checkNoDialog(Observable<CatRespone<M>> observable, Context context) {
        return checkError(checkNetWork(checkResult2(observable), context), context);
    }

    protected <M> Observable<M> checkNoResult(Observable<M> observable, Context context) {
        return checkError(showDialog(checkNetWork(observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()), context), context), context);
    }

    protected <M> Observable<M> checkNoDialog2(Observable<CatRespone<M>> observable, Context context) {
        return checkNetWork(checkResult2(observable), context);
    }

    public Observable<List<Course>> allVideoCourseList(MParams params, Context ctx) {
        return checkNoDialog(mService.allVideoCourseList(params.getJsonRequestBody()), ctx);
    }

    public Observable<WaitCourse> videoWaitForPlayList(MParams params, Context ctx) {
        return checkNoDialog(mService.videoWaitForPlayList(params.getJsonRequestBody()), ctx);
    }
}
