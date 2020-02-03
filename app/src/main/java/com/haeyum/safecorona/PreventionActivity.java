package com.haeyum.safecorona;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PreventionActivity extends AppCompatActivity {

    private TextView tvHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevention);

        tvHeaderTitle = findViewById(R.id.tv_header_title);
        SetHeaderTitle("예방 방법");
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
