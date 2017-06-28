package com.android.callrecord.receiver;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.util.Log;
import com.android.callrecord.AudioRecorder;
import com.android.callrecord.CallRecord;
import com.android.callrecord.helper.PrefHelper;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aykutasil on 19.10.2016.
 */
public class CallRecordReceiver extends PhoneCallReceiver {

    private static final String TAG = CallRecordReceiver.class.getSimpleName();

    public static final String ACTION_IN = "android.intent.action.PHONE_STATE";
    public static final String ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL";
    public static final String EXTRA_PHONE_NUMBER = "android.intent.extra.PHONE_NUMBER";

    private static MediaRecorder recorder;
    private File audioFile;
    private boolean isRecordStarted = false;
    private PrefHelper helper;

    @Override
    public void onReceive(Context context, Intent intent) {
        helper = PrefHelper.self(context);
        super.onReceive(context, intent);
    }

    @Override
    protected void onIncomingCallReceived(Context context, String number, Date start) {

    }

    @Override
    protected void onIncomingCallAnswered(Context context, String number, Date start) {
        if (helper.isOutGoingOnly() || helper.isInBlackList(number) || !helper.canSaveFile()) {
            return;
        }
        startRecord("incoming", number);
    }

    @Override
    protected void onIncomingCallEnded(Context context, String number, Date start, Date end) {
        stopRecord(context);
    }

    @Override
    protected void onOutgoingCallStarted(Context context, String number, Date start) {
        if (helper.isInComingOnly() || helper.isInBlackList(number) || !helper.canSaveFile()) {
            return;
        }
        startRecord("outgoing", number);
    }

    @Override
    protected void onOutgoingCallEnded(Context context, String number, Date start, Date end) {
        stopRecord(context);
    }

    @Override
    protected void onMissedCall(Context context, String number, Date start) {

    }

    private void startRecord(String seed, String phoneNumber) {
        try {
            String dirPath = helper.getSavePath();
            boolean showSeed = helper.isShowSeed();
            boolean showPhoneNumber = helper.isShowPhoneNumber();
            int outputFormat = helper.get(CallRecord.PREF_OUTPUT_FORMAT, Integer.class);
            int audioSource = helper.get(CallRecord.PREF_AUDIO_SOURCE, Integer.class);
            int audioEncoder = helper.get(CallRecord.PREF_AUDIO_ENCODER, Integer.class);

            File sampleDir = new File(dirPath);
            if (!sampleDir.exists()) {
                sampleDir.mkdirs();
            }
            StringBuilder fileNameBuilder = new StringBuilder();
            fileNameBuilder.append(getCurrentTime());
            fileNameBuilder.append("_");

            if (showSeed) {
                fileNameBuilder.append(seed);
                fileNameBuilder.append("_");
            }
            if (showPhoneNumber) {
                fileNameBuilder.append(phoneNumber);
                fileNameBuilder.append("_");
            }

            String fileName = fileNameBuilder.toString();

            String suffix = "";
            switch (outputFormat) {
                case MediaRecorder.OutputFormat.AMR_NB: {
                    suffix = ".amr";
                    break;
                }
                case MediaRecorder.OutputFormat.AMR_WB: {
                    suffix = ".amr";
                    break;
                }
                case MediaRecorder.OutputFormat.MPEG_4: {
                    suffix = ".mp4";
                    break;
                }
                case MediaRecorder.OutputFormat.THREE_GPP: {
                    suffix = ".3gp";
                    break;
                }
                default: {
                    suffix = ".amr";
                    break;
                }
            }

            audioFile = File.createTempFile(fileName, suffix, sampleDir);

            recorder = new MediaRecorder();
            recorder.setAudioSource(audioSource);
            recorder.setOutputFormat(outputFormat);
            recorder.setAudioEncoder(audioEncoder);
            recorder.setOutputFile(audioFile.getAbsolutePath());

            AudioRecorder.self().startRecord(recorder);

            isRecordStarted = true;
            Log.i(TAG, "record start");
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd.HH-mm-ss.SSS");
        return sdf.format(new Date());
    }

    private void stopRecord(Context context) {
        if (recorder != null && isRecordStarted) {
            AudioRecorder.self().stopRecord();
        }
    }
}