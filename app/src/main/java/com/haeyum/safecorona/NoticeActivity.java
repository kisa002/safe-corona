package com.haeyum.safecorona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class NoticeActivity extends AppCompatActivity {

    public static NoticeActivity instance = new NoticeActivity();

    private TextView tvTitle;
    private TextView tvContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);

        tvTitle = findViewById(R.id.tv_notice_title);
        tvContext = findViewById(R.id.tv_notice_context);

        Intent intent = getIntent();
        tvTitle.setText(intent.getStringExtra("title"));
        tvContext.setText(intent.getStringExtra("context"));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_notice_close:
                finish();
                break;

        }
    }
}
