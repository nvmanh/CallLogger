package com.android.callrecord.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import com.android.callrecord.CallRecord;
import com.android.callrecord.config.CallRecordConfig;
import com.android.callrecord.service.CallRecordService;
import java.util.Arrays;

/**
 * Created by root on 6/28/17.
 */

public class CallRecordHelper {
    private static final int REJECT_CODE = 1;
    String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    int PERMISSION_ALL = 1;
    private static CallRecordHelper _self;

    public static CallRecordHelper self() {
        return _self == null ? _self = new CallRecordHelper() : _self;
    }

    private CallRecordHelper() {
        super();
    }

    public void onCreate(Activity activity) {
        init(activity);
        if (!hasPermissions(activity)) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, PERMISSION_ALL);
            return;
        }
        activity.startService(new Intent(activity, CallRecordService.class));
    }

    public void onRequestPermissionsResult(Activity activity, int requestCode,
            @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != PERMISSION_ALL) return;
        if (Arrays.asList(grantResults).contains(REJECT_CODE)) {
            return;
        } else {
            activity.startService(new Intent(activity, CallRecordService.class));
        }
    }

    boolean hasPermissions(Activity activity) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(activity, permission)
                        != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void init(Context context) {
        if (!TextUtils.isEmpty(
                PrefHelper.self(context).get(CallRecord.PREF_DIR_PATH, String.class))) {
            return;
        }
        setConfig(context, new CallRecordConfig());
    }

    public void setConfig(Context context, CallRecordConfig config) {
        PrefHelper helper = PrefHelper.self(context);
        helper.put(CallRecord.PREF_DIR_PATH, config.getStorePath());
        helper.put(CallRecord.PREF_AUDIO_SOURCE, config.getAudioSource());
        helper.put(CallRecord.PREF_AUDIO_ENCODER, config.getAudioEncoder());
        helper.put(CallRecord.PREF_OUTPUT_FORMAT, config.getOutputFormat());
        helper.put(CallRecord.PREF_SHOW_SEED, config.isShowSeed());
        helper.put(CallRecord.PREF_SHOW_PHONE_NUMBER, config.isShowPhoneNumber());
        helper.put(CallRecord.PREF_SAVE_FILE, config.isCanSave());
        restartService(context);
    }

    void restartService(Context context) {
        try {
            Intent intent = new Intent(context, CallRecordService.class);
            context.startService(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
