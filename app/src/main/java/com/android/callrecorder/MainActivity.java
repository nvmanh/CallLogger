package com.android.callrecorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.android.callrecord.AudioRecorder;
import com.android.callrecord.helper.CallRecordHelper;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private static final String TIME_FORMAT = "%02d:%02d:%02d";
    private static final int REQUEST_SETTING = 100;
    private int sec = 0;
    private TextView timerText;
    Timer timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Record audio");
        setContentView(R.layout.activity_main);
        CallRecordHelper.self().onCreate(this);
        CheckBox btnRecord = (CheckBox) findViewById(R.id.btnRecord);
        timerText = (TextView) findViewById(R.id.timer);
        btnRecord.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (AudioRecorder.self().isRecording()) {
                    stopCallRecordClick();
                } else {
                    startCallRecordClick();
                }
            }
        });
        reset();
    }

    private void updateTimer() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int hours = sec / (60 * 60);
                int minutes = (sec % (60 * 60)) / 60;
                int seconds = (sec % (60 * 60)) % 60;
                timerText.setText(String.format(TIME_FORMAT, hours, minutes, seconds));
            }
        });
    }

    private void startTimer() {
        TimerTask t = new TimerTask() {
            @Override
            public void run() {
                sec++;
                updateTimer();
            }
        };
        timer.scheduleAtFixedRate(t, 0, 1000);
    }

    private void reset() {
        sec = 0;
        updateTimer();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        CallRecordHelper.self()
                .onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private void stopCallRecordClick() {
        timer.cancel();
        reset();
        AudioRecorder.self().stopRecord();
    }

    //IllegalStateException, IOException, NullPointerException
    private void startCallRecordClick() {
        try {
            AudioRecorder.self().startRecord(null);
            startTimer();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopCallRecordClick();
    }

    public void showSetting(View v) {
        startActivityForResult(new Intent(this, SettingActivity.class), REQUEST_SETTING);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_SETTING) return;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting) {
            showSetting(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}