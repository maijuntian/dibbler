package com.mai.dibbler.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;

import com.healthmall.sail.cat_doctor.MyApplication;
import com.healthmall.sail.cat_doctor.bean.BloodOxygenReport;
import com.healthmall.sail.cat_doctor.bean.BloodPressureReport;
import com.healthmall.sail.cat_doctor.bean.BodyReport;
import com.healthmall.sail.cat_doctor.bean.BodyRespone;
import com.healthmall.sail.cat_doctor.bean.CDRespone;
import com.healthmall.sail.cat_doctor.bean.Question;
import com.healthmall.sail.cat_doctor.bean.QuestionReport;
import com.healthmall.sail.cat_doctor.bean.Symptom;
import com.healthmall.sail.cat_doctor.bean.TemperatureReport;
import com.healthmall.sail.cat_doctor.bean.Version;
import com.healthmall.sail.cat_doctor.utils.BitmapUtils;
import com.healthmall.sail.cat_doctor.utils.CommonUtils;
import com.healthmall.sail.cat_doctor.utils.Keys;
import com.healthmall.sail.cat_doctor.widget.LoadingDialog;
import com.mai.xmai_fast_lib.basehttp.BaseRetrofitService;
import com.mai.xmai_fast_lib.basehttp.MParams;
import com.mai.xmai_fast_lib.basehttp.UploadListener;
import com.mai.xmai_fast_lib.exception.ServerException;
import com.mai.xmai_fast_lib.utils.MLog;
import com.mai.xmai_fast_lib.utils.SharedPreferencesHelper;

import java.io.File;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * Created by mai on 2017/11/20.
 */
public class CatService extends BaseRetrofitService<CatDoctorService> {

    static CatDoctorApi instance;
    private LoadingDialog dialog;

    ProgressDialog pdLoading;

    public static CatDoctorApi getInstance() {
        if (instance == null) {
            synchronized (CatDoctorApi.class) {
                if (instance == null) {
                    instance = new CatDoctorApi();
                }
            }
        }
        return instance;
    }

    @Override
    protected String getBaseUrl() {
//        return "http://apisail.healthmall.cn/api/";
        return "http://dev-apisail.healthmall.cn/api/";
    }


    @Override
    protected void showDialog(Context context) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        dialog = new LoadingDialog(context);
        dialog.show();
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
        if (dialog != null)
            dialog.dismiss();
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
    protected <M> Observable<M> checkResult2(Observable<CDRespone<M>> observable) {
        return observable.map(new Func1<CDRespone<M>, M>() {
            @Override
            public M call(CDRespone<M> response) {
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


    protected <M> Observable<M> checkAll2(Observable<CDRespone<M>> observable, Context context) {
        return checkError(showDialog(checkNetWork(checkResult2(observable), context), context), context);
    }

    protected <M> Observable<M> checkNoDialog(Observable<CDRespone<M>> observable, Context context) {
        return checkError(checkNetWork(checkResult2(observable), context), context);
    }

    protected <M> Observable<M> checkNoResult(Observable<M> observable, Context context) {
        return checkError(showDialog(checkNetWork(observable.subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()), context), context), context);
    }

    protected <M> Observable<M> checkNoDialog2(Observable<CDRespone<M>> observable, Context context) {
        return checkNetWork(checkResult2(observable), context);
    }

    //请求接口

    public Observable<Object> scanCode(MParams params, Context ctx) {
        return checkNoDialog(mService.scanCode(params.getJsonRequestBody()), ctx);
    }


    public Observable<CDRespone> bindDevice(String deviceId, Context ctx) {
        return checkNoResult(mService.bindDevice(new MParams().add("deviceId", deviceId).getJsonRequestBody()), ctx);
    }

    public Observable<BodyRespone> bodyReport(BodyReport bodyReport, Context ctx) {
        bodyReport.setSex(MyApplication.get().getCurrUser().getMemberSex());
        bodyReport.setAge(MyApplication.get().getCurrUser().getMemberAge());
        return checkNoDialog(mService.bodyReport(new MParams().getJsonRequestBody(bodyReport)), ctx);
    }

    public Observable<BodyRespone> temperatureReport(TemperatureReport temperatureReport, Context ctx) {
        return checkNoDialog(mService.temperatureReport(new MParams().getJsonRequestBody(temperatureReport)), ctx);
    }

    public Observable<Object> bloodOxygenReport(BloodOxygenReport bloodOxygenReport, Context ctx) {
        return checkNoDialog(mService.bloodOxygenReport(new MParams().getJsonRequestBody(bloodOxygenReport)), ctx);
    }

    public Observable<Object> bloodPressureReport(BloodPressureReport bloodPressureReport, Context ctx) {
        return checkNoDialog(mService.bloodPressureReport(new MParams().getJsonRequestBody(bloodPressureReport)), ctx);
    }

    public Observable<Object> faceTonUpload(final Bitmap bmp, final int testType, final UploadListener listener, final Context ctx) {
        final String deviceId = SharedPreferencesHelper.getInstance(MyApplication.get().getApplicationContext()).getStringValue(Keys.KEY_DEVICE_ID);
        return checkNoDialog(Observable.just(bmp).map(new Func1<Bitmap, File>() {
            @Override
            public File call(Bitmap bitmap) {
                String path;
                if (testType == 1) {
                    path = CommonUtils.getFaceFilePath();
                } else {
                    path = CommonUtils.getTonFilePath();
                }

                BitmapUtils.compressJpgBitmap(bmp, path);
                return new File(path);
            }
        }).flatMap(new Func1<File, Observable<CDRespone<Object>>>() {
            @Override
            public Observable<CDRespone<Object>> call(File file) {

                return mService.faceTonUpload(new MParams().add("img", file).add("pwd", "9daef6de79902510dbd1f7702b179d0d").add("testType", testType).add("source", 0).add("deviceId", deviceId).add("mallId", MyApplication.get().getCurrUser().getMallId()).setLoadListener(listener).getFileListenerParams());
            }
        }), ctx);
    }

    public Observable<Question> questionAnswer(MParams params, Context ctx) {
        String deviceId = SharedPreferencesHelper.getInstance(MyApplication.get().getApplicationContext()).getStringValue(Keys.KEY_DEVICE_ID);
        params.add("deviceId", deviceId);
        return checkNoDialog(mService.questionAnswer(MyApplication.get().getCurrUser().getAccessToken(), params.getJsonRequestBody()), ctx);
    }

    public Observable<Question> preQuestion(MParams params, Context ctx) {
        return checkNoDialog(mService.preQuestion(MyApplication.get().getCurrUser().getAccessToken(), params.getJsonRequestBody()), ctx);
    }

    public Observable<QuestionReport> questionResult(MParams params, Context ctx) {
        return checkNoDialog(mService.questionResult(MyApplication.get().getCurrUser().getAccessToken(), params.getJsonRequestBody()), ctx);
    }

    public Observable<QuestionReport> commitQuestion(MParams params, final Context ctx) {
        String deviceId = SharedPreferencesHelper.getInstance(MyApplication.get().getApplicationContext()).getStringValue(Keys.KEY_DEVICE_ID);
        params.add("deviceId", deviceId);
        return checkResult2(mService.questionAnswer(MyApplication.get().getCurrUser().getAccessToken(), params.getJsonRequestBody()))
                .flatMap(new Func1<Question, Observable<QuestionReport>>() {
                    @Override
                    public Observable<QuestionReport> call(final Question question) {
                        return questionResult(new MParams().add("questionAnswerId", question.getQuestionAnswerId()), ctx).map(new Func1<QuestionReport, QuestionReport>() {
                            @Override
                            public QuestionReport call(QuestionReport questionReport) {
                                questionReport.setQuestionAnswerId(question.getQuestionAnswerId());
                                return questionReport;
                            }
                        });
                    }
                });
    }

    public Observable<Object> saveUserInfo(MParams params, Context ctx) {
        return checkAll2(mService.saveUserInfo(MyApplication.get().getCurrUser().getAccessToken(), params.getJsonRequestBody()), ctx);
    }

    public Observable<Object> quit(MParams params, Context ctx) {
        MLog.log("" + (MyApplication.get().getCurrUser() == null));
        return checkNoDialog(mService.quit(MyApplication.get().getCurrUser().getAccessToken(), params.getJsonRequestBody()), ctx);
    }

    public Observable<Version> getNewVersion(MParams params, Context ctx) {
        return checkNoDialog2(mService.getNewVersion(params.getJsonRequestBody()), ctx);
    }

    public Observable<Object> upgrade(MParams params, Context ctx) {
        return checkNoDialog2(mService.upgrade(params.getJsonRequestBody()), ctx);
    }

    public Observable<List<Symptom>> getSymptom(Context ctx) {
        return checkNoDialog(mService.getSymptom(MyApplication.get().getCurrUser().getAccessToken()), ctx);
    }
}
