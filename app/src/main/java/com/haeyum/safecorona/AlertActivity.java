package com.haeyum.safecorona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AlertActivity extends AppCompatActivity {

    public static AlertActivity instance = new AlertActivity();

    private TextView tvTitle;
    private TextView tvContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        tvTitle = findViewById(R.id.tv_alert_title);
        tvContext = findViewById(R.id.tv_alert_context);

        Intent intent = getIntent();
        tvTitle.setText(intent.getStringExtra("title"));
        tvContext.setText(intent.getStringExtra("context"));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_alert_close:
                finish();
                break;

        }
    }
}
