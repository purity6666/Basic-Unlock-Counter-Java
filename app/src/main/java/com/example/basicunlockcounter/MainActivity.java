package com.example.basicunlockcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "StatsFragment";
    private TextView textView;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private int count = 0;

    public class PhoneUnlockedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
            preferences = getSharedPreferences("label", 0);
            editor = preferences.edit();

            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                if (!keyguardManager.inKeyguardRestrictedInputMode()) {
                    count++;
                    editor.putInt("tag", count).apply();
                    textView.setText(String.valueOf(count));
                    Log.d(TAG, "unlock registered");
                }
            } else {
                if (!keyguardManager.isDeviceLocked()) {
                    count++;
                    editor.putInt("tag", count).apply();
                    textView.setText(String.valueOf(count));
                    Log.d(TAG, "unlock registered");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.unlock_count);
        registerReceiver(new PhoneUnlockedReceiver(), new IntentFilter("android.intent.action.USER_PRESENT"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        preferences = getSharedPreferences("label", 0);
        count = preferences.getInt("tag", 0);
        textView.setText(String.valueOf(count));
    }
}