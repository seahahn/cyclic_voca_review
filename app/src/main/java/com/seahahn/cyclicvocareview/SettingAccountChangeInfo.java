package com.seahahn.cyclicvocareview;
import android.util.Log;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

import static com.seahahn.cyclicvocareview.MainActivity.userEmail;
import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class SettingAccountChangeInfo extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SettingAccountChangeInfo";
    ImageButton ImageButton_settingAccountChangeInfo_goback;
    Button Button_settingAccountChangeInfo_logout;
    Button Button_settingAccountChangeInfo_password;
//    Button Button_settingAccountChangeInfo_email;
    TextView TextView_settingAccountChangeInfo_delete;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account_change_info);

        mAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화
        FirebaseUser user = mAuth.getCurrentUser();

        // 좌측 상단 좌향 화살표 이미지버튼 - 뒤로 가기 기능 구현
        ImageButton_settingAccountChangeInfo_goback = findViewById(R.id.ImageButton_settingAccountChangeInfo_goback);
        ImageButton_settingAccountChangeInfo_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingAccountChangeInfo.this, Setting.class);
                startActivity(intent);
//                finish();
            }
        });

        // 계정 로그아웃 버튼
        Button_settingAccountChangeInfo_logout = findViewById(R.id.Button_settingAccountChangeInfo_logout);
        Button_settingAccountChangeInfo_logout.setOnClickListener(this);

        Button_settingAccountChangeInfo_password = findViewById(R.id.Button_settingAccountChangeInfo_password);
        Button_settingAccountChangeInfo_password.setOnClickListener(this);
        // 비밀번호 변경 버튼
        if(user.getProviderData().get(1).getProviderId().equals("google.com")){
            Button_settingAccountChangeInfo_password.setVisibility(View.GONE);
        } else {
            Button_settingAccountChangeInfo_password.setVisibility(View.VISIBLE);
        }

//        // 이메일 변경 버튼
//        Button_settingAccountChangeInfo_email = findViewById(R.id.Button_settingAccountChangeInfo_email);
//        Button_settingAccountChangeInfo_email.setOnClickListener(this);

        // 회원 탈퇴 텍스트뷰
        TextView_settingAccountChangeInfo_delete = findViewById(R.id.TextView_settingAccountChangeInfo_delete);
        TextView_settingAccountChangeInfo_delete.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Button_settingAccountChangeInfo_logout:
                FirebaseAuth.getInstance().signOut();
                userID = null;
                userEmail = null;
                Intent mIntent = new Intent(getApplicationContext(), Setting.class);
                startActivity(mIntent);
                break;
            case R.id.Button_settingAccountChangeInfo_password:
                mIntent = new Intent(getApplicationContext(), SettingAccountChangePassword.class);
                startActivity(mIntent);
                break;
//            case R.id.Button_settingAccountChangeInfo_email:
//                mIntent = new Intent(getApplicationContext(), SettingAccountChangeEmail.class);
//                startActivity(mIntent);
//                break;
            case R.id.TextView_settingAccountChangeInfo_delete:
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("회원 탈퇴");
                builder.setMessage("회원 탈퇴 시 회원 정보와 저장된 데이터가 삭제됩니다.\n탈퇴하시겠어요?");
                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().getCurrentUser().delete();
                        Toast.makeText(getApplicationContext(), "회원 탈퇴 완료",Toast.LENGTH_SHORT).show();

                        File sharedPreferenceFile = new File("/data/data/"+ getPackageName()+ "/shared_prefs/");
                        File[] listFiles = sharedPreferenceFile.listFiles();
                        for (File file : listFiles) {
                            if(file.getName().equals(userID)){
                                file.delete();
                            }
                        }

                        Intent mIntent = new Intent(getApplicationContext(), MainActivity.class);
                        mIntent.putExtra("accountDelete", true);
                        startActivity(mIntent);
                    }
                });
                builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            default:
                break;

        }
    }
}