package com.seahahn.cyclicvocareview;

import android.content.Intent;
import android.icu.util.VersionInfo;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SettingAbout extends AppCompatActivity {

    VersionInfo appVersion;

    ImageButton ImageButton_settingAbout_goback;
    TextView TextView_settingAbout_aboutDeveloperEmailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_about);

        ImageButton_settingAbout_goback = findViewById(R.id.ImageButton_settingAbout_goback);
        ImageButton_settingAbout_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingAbout.this, Setting.class);
                startActivity(intent);

            }
        });

        TextView_settingAbout_aboutDeveloperEmailAddress = findViewById(R.id.TextView_settingAbout_aboutDeveloperEmailAddress);
        TextView_settingAbout_aboutDeveloperEmailAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType("plain/Text");
                String[] emailAddress = {getString(R.string.aboutDeveloperEmailAddress)};
                email.putExtra(Intent.EXTRA_EMAIL, emailAddress);
                email.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name)+" "+getString(R.string.aboutEmailSubject));
                email.putExtra(Intent.EXTRA_TEXT, "앱 버전 (AppVersion):" + appVersion + "\n기기명 (Device):\n안드로이드 OS (Android OS):\n내용 (Content):\n");
                email.setType("message/rfc822");
                startActivity(email);
            }
        });
    }
}