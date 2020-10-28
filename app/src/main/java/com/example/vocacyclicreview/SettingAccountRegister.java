package com.example.vocacyclicreview;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class SettingAccountRegister extends AppCompatActivity {

    ImageButton ImageButton_settingAccountRegister_goBack;
    Button Button_settingAccountRegister_register;

    EditText EditText_settingAccountRegister_emailInput;
    EditText EditText_settingAccountRegister_passwordInput;
    EditText EditText_settingAccountRegister_passwordCheckInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account_register);

        ImageButton_settingAccountRegister_goBack = findViewById(R.id.ImageButton_settingAccountRegister_goBack);
        ImageButton_settingAccountRegister_goBack.setClickable(true);
        ImageButton_settingAccountRegister_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingAccountRegister.this, SettingAccount.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        EditText_settingAccountRegister_emailInput = findViewById(R.id.EditText_settingAccountRegister_emailInput);
        EditText_settingAccountRegister_passwordInput = findViewById(R.id.EditText_settingAccountRegister_passwordInput);
        EditText_settingAccountRegister_passwordCheckInput = findViewById(R.id.EditText_settingAccountRegister_passwordCheckInput);
        Button_settingAccountRegister_register = findViewById(R.id.Button_settingAccountRegister_register);
        Button_settingAccountRegister_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EditText_settingAccountRegister_passwordInput.getText().toString().equals(EditText_settingAccountRegister_passwordCheckInput.getText().toString())){
                    SettingAccount.accountList.put(EditText_settingAccountRegister_emailInput.getText().toString(), EditText_settingAccountRegister_passwordInput.getText().toString());
                    Toast.makeText(SettingAccountRegister.this, R.string.registerPasswordCheckSucceed, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SettingAccountRegister.this, R.string.registerPasswordCheckFail, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}