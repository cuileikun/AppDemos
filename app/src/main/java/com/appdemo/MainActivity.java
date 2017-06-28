package com.appdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.appdemo.api.HouseResourceAPIAsyncTask;
import com.appdemo.api.HouseResourceResponseResult;
import com.appdemo.api.Protocol;
import com.appdemo.hractivity.HRActivity;
import com.qk.applibrary.listener.ResponseResultListener;
import com.qk.applibrary.util.CommonUtil;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_hr;
    private Button btn_hk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addListener();
    }

    private void addListener() {
        btn_hr.setOnClickListener(MainActivity.this);
        btn_hk.setOnClickListener(MainActivity.this);
    }

    private void initView() {

        btn_hr = (Button) findViewById(R.id.btn_hr);
        btn_hk = (Button) findViewById(R.id.btn_hk);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_hr:
                startActivity(new Intent(MainActivity.this, HRActivity.class));
                break;
            case R.id.btn_hk:
                break;
        }
    }

}
