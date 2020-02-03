package com.haeyum.safecorona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                startActivity(new Intent(getApplicationContext(), LicenseActivity.class));
                break;

            case R.id.ib_header_back:
                finish();
                break;
        }
    }

    private void SetHeaderTitle(String title) {
        tvHeaderTitle.setText(title);
    }
}
