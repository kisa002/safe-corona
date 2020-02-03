package com.haeyum.safecorona;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DeveloperActivity extends AppCompatActivity {

    private TextView tvHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);

        tvHeaderTitle = findViewById(R.id.tv_header_title);
        SetHeaderTitle("개발자 정보");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_header_back:
                finish();
                break;
        }
    }

    private void SetHeaderTitle(String title) {
        tvHeaderTitle.setText(title);
    }

}
