package com.example.vocacyclicreview;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Hashtable;

public class SettingAccount extends AppCompatActivity {

    ImageButton ImageButton_settingAccount_goBack;
    TextView TextView_settingAccount_register;
    TextView TextView_settingAccount_findPassword;
    EditText EditText_settingAccount_emailInput;
    EditText EditText_settingAccount_passwordInput;
    Button Button_settingAccount_Login;

    static boolean accountLogin;

    static Hashtable<String, String> accountList = new Hashtable<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);

        // 뒤로가기 버튼
        ImageButton_settingAccount_goBack = findViewById(R.id.ImageButton_settingAccount_goBack);
        ImageButton_settingAccount_goBack.setClickable(true);
        ImageButton_settingAccount_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingAccount.this, Setting.class);
                startActivity(intent);
            }
        });

        // 이메일, 비번 입력 후 로그인
        EditText_settingAccount_emailInput = findViewById(R.id.EditText_settingAccount_emailInput);
        EditText_settingAccount_passwordInput = findViewById(R.id.EditText_settingAccount_passwordInput);
        Button_settingAccount_Login = findViewById(R.id.Button_settingAccount_Login);
        Button_settingAccount_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accountList.containsKey(EditText_settingAccount_emailInput.getText().toString()) && EditText_settingAccount_passwordInput.getText().toString().equals(accountList.get(EditText_settingAccount_emailInput.getText().toString()))){
                    accountLogin = true;
                    Intent intent = new Intent(SettingAccount.this, Setting.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SettingAccount.this, getString(R.string.loginFail), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 계정 만들기 버튼
        TextView_settingAccount_register = findViewById(R.id.TextView_settingAccount_register);
        TextView_settingAccount_register.setClickable(true);
        TextView_settingAccount_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingAccount.this, SettingAccountRegister.class);
                startActivity(intent);
            }
        });

        // 비밀번호 찾기 버튼
        TextView_settingAccount_findPassword = findViewById(R.id.TextView_settingAccount_findPassword);
        TextView_settingAccount_findPassword.setClickable(true);
        TextView_settingAccount_findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingAccount.this, SettingAccountFindpassword.class);
                startActivity(intent);
            }
        });
    }
}