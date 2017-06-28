package com.android.callrecord.config;

import android.media.MediaRecorder;
import android.os.Environment;

/**
 * Created by root on 6/28/17.
 */

public class CallRecordConfig {
    private boolean canSave = true;
    private String storePath = Environment.getExternalStorageDirectory().getPath() + "/CallRecord";
    private boolean showSeed = true;
    private boolean showPhoneNumber = true;
    private int audioSource = MediaRecorder.AudioSource.VOICE_COMMUNICATION;
    private int audioEncoder = MediaRecorder.AudioEncoder.AMR_NB;
    private int outputFormat = MediaRecorder.OutputFormat.AMR_NB;

    public CallRecordConfig() {
        super();
    }

    public CallRecordConfig setEnableSaveRecord(boolean canSave) {
        this.canSave = canSave;
        return this;
    }

    public CallRecordConfig setStorePath(String path) {
        this.storePath = path;
        return this;
    }

    public CallRecordConfig setShowSeed(boolean show) {
        this.showSeed = show;
        return this;
    }

    public CallRecordConfig setShowPhoneNumber(boolean show) {
        this.showPhoneNumber = show;
        return this;
    }

    public CallRecordConfig setAudioSource(int source) {
        this.audioSource = source;
        return this;
    }

    public CallRecordConfig setAudioEncoder(int encoder) {
        this.audioEncoder = encoder;
        return this;
    }

    public CallRecordConfig setOutputFormat(int format) {
        this.outputFormat = format;
        return this;
    }

    public boolean isCanSave() {
        return canSave;
    }

    public boolean isShowPhoneNumber() {
        return showPhoneNumber;
    }

    public boolean isShowSeed() {
        return showSeed;
    }

    public int getAudioEncoder() {
        return audioEncoder;
    }

    public int getAudioSource() {
        return audioSource;
    }

    public int getOutputFormat() {
        return outputFormat;
    }

    public String getStorePath() {
        return storePath;
    }
}