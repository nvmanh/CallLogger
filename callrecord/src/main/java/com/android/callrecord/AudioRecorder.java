package com.android.callrecord;

import android.media.MediaRecorder;
import com.android.callrecord.config.CallRecordConfig;
import com.android.callrecord.receiver.CallRecordReceiver;
import java.io.File;
import java.io.IOException;

/**
 * Created by root on 6/28/17.
 */

public class AudioRecorder {
    private static AudioRecorder _self;
    private boolean recording;
    private MediaRecorder recorder;

    public static AudioRecorder self() {
        return _self == null ? _self = new AudioRecorder() : _self;
    }

    private AudioRecorder() {
        super();
    }

    public void startRecord(MediaRecorder recorder)
            throws IllegalStateException, IOException, NullPointerException {
        if (recording) stopRecord();
        recording = true;
        if (recorder == null) {
            CallRecordConfig defaultConfig = new CallRecordConfig();
            File sampleDir = new File(defaultConfig.getStorePath());
            if (!sampleDir.exists()) sampleDir.mkdirs();
            String suffix = ".amr";
            String fileName = CallRecordReceiver.getCurrentTime() + "_audio";
            File audioFile = File.createTempFile(fileName, suffix, sampleDir);
            recorder = new MediaRecorder();
            recorder.setAudioSource(defaultConfig.getAudioSource());
            recorder.setOutputFormat(defaultConfig.getOutputFormat());
            recorder.setAudioEncoder(defaultConfig.getAudioEncoder());
            recorder.setOutputFile(audioFile.getAbsolutePath());
        }
        this.recorder = recorder;
        this.recorder.prepare();
        this.recorder.start();
    }

    public void stopRecord() {
        recording = false;
        if (recorder == null) return;
        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;
    }

    public boolean isRecording() {
        return recording;
    }
}
