package com.android.callrecorder;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import com.android.callrecord.helper.PrefHelper;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 6/28/17.
 */

public class BlackListActivity extends AppCompatActivity {
    private PrefHelper helper;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Black list");
        setContentView(R.layout.activity_blacklist);
        helper = PrefHelper.self(this);
        editText = (EditText) findViewById(R.id.edtBlackList);
        List<String> numbers = helper.getBlackListNumber();
        for (String n : numbers) {
            editText.append(n);
            editText.append(", ");
        }
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        String text = editText.getText().toString().trim();
        List<String> numbers = new ArrayList<>();
        if (!TextUtils.isEmpty(text)) {
            String[] apd = text.split(",");
            for (String s : apd) {
                if (!PhoneNumberUtils.isGlobalPhoneNumber(s)) continue;
                numbers.add(s.trim());
            }
        }
        helper.setBlackList(new Gson().toJson(numbers));
    }
}
