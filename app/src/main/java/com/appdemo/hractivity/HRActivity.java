package com.appdemo.hractivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.appdemo.R;
import com.appdemo.hractivity.rank.RealTimeActivity;

public class HRActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_rank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr);
        initView();
        addListener();
    }

    private void addListener() {
        btn_rank.setOnClickListener(HRActivity.this);

    }

    private void initView() {
        btn_rank = (Button) findViewById(R.id.btn_rank);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_rank:
                startActivity(new Intent(HRActivity.this, RealTimeActivity.class));
            break;
        }

    }



}
