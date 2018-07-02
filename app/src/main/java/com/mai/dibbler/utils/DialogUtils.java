package com.mai.dibbler.utils;

import android.app.Activity;
import android.app.Dialog;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mai.dibbler.R;
import com.mai.xmai_fast_lib.utils.SharedPreferencesHelper;

import rx.functions.Action0;
import rx.functions.Action1;

public class DialogUtils {

    public static void showSettingDialog(final Activity act, final Action0 callback) {
        final Dialog dialog = new Dialog(act, R.style.LoadingDialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_setting);

        final EditText etDeviceId = dialog.findViewById(R.id.et_device_id);
        final EditText etLifeId = dialog.findViewById(R.id.et_life_id);

        String deviceId = SharedPreferencesHelper.getInstance(act).getStringValue(Key.DEVICE_ID);
        String lifeId = SharedPreferencesHelper.getInstance(act).getStringValue(Key.LIFE_ID);

        if (!TextUtils.isEmpty(deviceId)) {
            etDeviceId.setText(deviceId);
        }

        if (!TextUtils.isEmpty(lifeId)) {
            etDeviceId.setText(lifeId);
        }


        dialog.findViewById(R.id.bt_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String deviceId = etDeviceId.getText().toString().trim();

                String lifeId = etLifeId.getText().toString().trim();

                if (TextUtils.isEmpty(deviceId) || TextUtils.isEmpty(lifeId)) {
                    Toast.makeText(act, "不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferencesHelper.getInstance(act).putStringValue(Key.DEVICE_ID, deviceId);
                SharedPreferencesHelper.getInstance(act).putStringValue(Key.LIFE_ID, lifeId);

                dialog.dismiss();
                callback.call();
            }
        });
        dialog.show();
    }

    public static void showQrcodeDialog(Activity act, String qrcode) {
        final Dialog dialog = new Dialog(act, R.style.LoadingDialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.dialog_qrcode);
        dialog.getWindow().getAttributes().width = act.getWindowManager().getDefaultDisplay().getWidth();
        dialog.getWindow().getAttributes().height = act.getWindowManager().getDefaultDisplay().getHeight();

        ImageView ivQrcode = dialog.findViewById(R.id.iv_qrcode);

        ivQrcode.setImageBitmap(QrCodeUtils.createImage(qrcode, 500, 500));
        dialog.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
