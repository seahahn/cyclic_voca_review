package com.example.vocacyclicreview;
import android.app.Activity;
import android.content.*;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.IntentCompat;
import com.google.gson.Gson;

import java.io.File;

public class Setting extends AppCompatActivity {

    ImageButton ImageButton_setting_goback;
    Button Button_setting_account;
    Button Button_setting_vocalearningcycle;
    Button Button_setting_theme;
    Button Button_setting_backup;
    Button Button_setting_about;
    TextView TextView_setting_reset;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        // 좌측 상단 좌향 화살표 이미지버튼 - 뒤로 가기 기능 구현
        ImageButton_setting_goback = findViewById(R.id.ImageButton_setting_goback);
        ImageButton_setting_goback.setClickable(true);
        ImageButton_setting_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Setting.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        // 계정 로그인/로그아웃 버튼
        Button_setting_account = findViewById(R.id.Button_setting_account);
        Button_setting_account.setClickable(true);
        Button_setting_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SettingAccount.accountLogin != true){
                    Intent intent = new Intent(Setting.this, SettingAccount.class);
                    startActivity(intent);
                } else {
                    SettingAccount.accountLogin = false;
                    Intent intent = new Intent(Setting.this, Setting.class);
                    startActivity(intent);
                }
            }
        });

        // 테마 설정 버튼
        Button_setting_theme = findViewById(R.id.Button_setting_theme);
        Button_setting_theme.setClickable(true);
        Button_setting_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Setting.this);
                builder.setTitle("테마 선택");
                builder.setItems(R.array.themeColor, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String LIGHT_MODE = "밝은 테마";
                        final String DARK_MODE = "어두운 테마";
                        // The 'which' argument contains the index position
                        // of the selected item
                        String[] items = getResources().getStringArray(R.array.themeColor);

                        switch (which) {
                            case 0:
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                                break;

                            case 1:
                                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                                break;

                            default:
                                // 안드로이드 10 이상
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                                }
                                // 안드로이드 10 미만
                                else {
                                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                                }
                                break;
                        }
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        // About 버튼
        Button_setting_about = findViewById(R.id.Button_setting_about);
        Button_setting_about.setClickable(true);
        Button_setting_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Setting.this, SettingAbout.class);
                startActivity(intent);
            }
        });

        // 맨 아래 초기화하기 텍스트뷰 - 누르면 앱 내 데이터 초기화시키는 기능
        TextView_setting_reset = findViewById(R.id.TextView_setting_reset);
        TextView_setting_reset.setClickable(true);
        TextView_setting_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBulder = new AlertDialog.Builder(Setting.this);
                dialogBulder.setMessage("앱에 저장된 모든 데이터를 삭제합니다.");
                dialogBulder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File sharedPreferenceFile = new File("/data/data/"+ getPackageName()+ "/shared_prefs/");
                        File[] listFiles = sharedPreferenceFile.listFiles();
                        for (File file : listFiles) {
                            file.delete();
                            System.out.println("file 삭제 여부 "+file);
                        }

//                        File imageFile = new File("Android/data/"+getPackageName()+"/files/Pictures");
//                        File[] photoListFiles = imageFile.listFiles();
//                        for (File file : photoListFiles) {
//                            file.delete();
//                            System.out.println("file 삭제 여부 "+file);
//                        }

//                        ActivityCompat.finishAffinity(Setting.this);
                        finishAffinity();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        System.exit(0);
                    }
                });
                dialogBulder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "초기화 취소",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog dialog = dialogBulder.create();
                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(SettingAccount.accountLogin == true){
            Button_setting_account.setText("계정 로그아웃");
        } else {
            Button_setting_account.setText("계정 로그인");
        }

        // 로그인/로그아웃 상태에 따라서 첫번째 버튼(계정 관련)에 '계정 로그인' 또는 '계정 로그아웃' 으로 출력되게 할 것
        // if문 사용해보기


    }

}