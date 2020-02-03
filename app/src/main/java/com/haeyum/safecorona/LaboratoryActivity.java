package com.haeyum.safecorona;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LaboratoryActivity extends AppCompatActivity {

    private TextView tvHeaderTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboratory);

        tvHeaderTitle = findViewById(R.id.tv_header_title);
        SetHeaderTitle("실험실");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_laboratory_developer:
                break;

            case R.id.ll_laboratory_license:
                break;
        }
    }

    private void SetHeaderTitle(String title) {
        tvHeaderTitle.setText(title);
    }
}
