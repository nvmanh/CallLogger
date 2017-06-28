package com.android.callrecorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.android.callrecord.helper.PrefHelper;
import java.util.List;

/**
 * Created by root on 6/28/17.
 */

public class SettingActivity extends AppCompatActivity
        implements CompoundButton.OnCheckedChangeListener {
    private static final int REQUEST_UPDATE_BL = 101;
    private PrefHelper helper;
    private CheckBox chkCanSave, chkShowSeed, chkShowPhoneNumber, chkInComing, chkOutGoing, chkSync;
    private TextView tvBlackList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Setting");
        setContentView(R.layout.activity_setting);
        findViews();
        helper = PrefHelper.self(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setup();
    }

    private void findViews() {
        chkCanSave = (CheckBox) findViewById(R.id.chkCanSave);
        chkShowSeed = (CheckBox) findViewById(R.id.chkShowSeed);
        chkShowPhoneNumber = (CheckBox) findViewById(R.id.chkShowPhoneNumber);
        chkInComing = (CheckBox) findViewById(R.id.chkInComing);
        chkOutGoing = (CheckBox) findViewById(R.id.chkOutGoing);
        chkSync = (CheckBox) findViewById(R.id.chkSync);
        tvBlackList = (TextView) findViewById(R.id.edtBlackList);
        chkInComing.setOnCheckedChangeListener(this);
        chkCanSave.setOnCheckedChangeListener(this);
        chkOutGoing.setOnCheckedChangeListener(this);
        chkShowPhoneNumber.setOnCheckedChangeListener(this);
        chkShowSeed.setOnCheckedChangeListener(this);
        chkSync.setOnCheckedChangeListener(this);
    }

    private void setup() {
        chkSync.setChecked(helper.isSyncOnline());
        chkShowSeed.setChecked(helper.isShowSeed());
        chkShowPhoneNumber.setChecked(helper.isShowPhoneNumber());
        chkOutGoing.setChecked(!helper.isOutGoingOnly());
        chkSync.setChecked(helper.isSyncOnline());
        chkInComing.setChecked(!helper.isInComingOnly());
        tvBlackList.setText("");
        List<String> bls = helper.getBlackListNumber();
        for (String n : bls) {
            tvBlackList.append(n);
            tvBlackList.append(", ");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_OK);
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_blacklist) {
            showBlackList(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        helper.setSaveFile(chkInComing.isChecked() || chkOutGoing.isChecked());
        super.onDestroy();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.chkCanSave:
                helper.setSaveFile(isChecked);
                break;
            case R.id.chkInComing:
                helper.setInComingOnly(!isChecked);
                break;
            case R.id.chkOutGoing:
                helper.setOutGoingOnly(!isChecked);
                break;
            case R.id.chkShowPhoneNumber:
                helper.setShowPhoneNumber(isChecked);
                break;
            case R.id.chkShowSeed:
                helper.setShowSeed(isChecked);
                break;
            case R.id.chkSync:
                helper.setSyncOnline(isChecked);
                break;
            default:
                break;
        }
    }

    public void showBlackList(View v) {
        startActivityForResult(new Intent(this, BlackListActivity.class), REQUEST_UPDATE_BL);
    }
}
