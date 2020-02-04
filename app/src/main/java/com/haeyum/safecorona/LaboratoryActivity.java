package com.haeyum.safecorona;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class LaboratoryActivity extends AppCompatActivity {

    private TextView tvHeaderTitle;
    private CheckBox cbWaring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laboratory);

        cbWaring = findViewById(R.id.cb_laboratory_waring);
        cbWaring.setChecked(PreferenceManager.getBoolean(this, "isUseWaring"));

        tvHeaderTitle = findViewById(R.id.tv_header_title);
        SetHeaderTitle("실험실");
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_laboratory_developer:
                startActivity(new Intent(getApplicationContext(), DeveloperActivity.class));
                break;

            case R.id.ll_laboratory_license:
                startActivity(new Intent(getApplicationContext(), LicenseActivity.class));
                break;

            case R.id.ib_header_back:
                finish();
                break;

            case R.id.cb_laboratory_waring:
                PreferenceManager.setBoolean(this, "isUseWaring", cbWaring.isChecked());
//                Toast.makeText(this, PreferenceManager.getBoolean(this, "isUseWaring") + "<", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void SetHeaderTitle(String title) {
        tvHeaderTitle.setText(title);
    }
}
