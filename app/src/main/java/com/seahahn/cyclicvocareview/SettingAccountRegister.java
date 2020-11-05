package com.seahahn.cyclicvocareview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingAccountRegister extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "SettingAccountRegister";
    ImageButton ImageButton_settingAccountRegister_goBack;
    Button Button_settingAccountRegister_register;

    EditText EditText_settingAccountRegister_emailInput;
    EditText EditText_settingAccountRegister_passwordInput;
    EditText EditText_settingAccountRegister_passwordCheckInput;

    CheckBox CheckBox_all;
    CheckBox CheckBox_serviceAgree;
    CheckBox CheckBox_personalInfoAgree;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account_register);

        // 이용약관 체크박스 초기화
//        CheckBox_all = findViewById(R.id.CheckBox_all);
        CheckBox_serviceAgree = findViewById(R.id.CheckBox_serviceAgree);
        CheckBox_personalInfoAgree = findViewById(R.id.CheckBox_personalInfoAgree);
//        CheckBox_all.setOnCheckedChangeListener(this);
        CheckBox_serviceAgree.setOnCheckedChangeListener(this);
        CheckBox_personalInfoAgree.setOnCheckedChangeListener(this);

        // 사용자에게 이메일 인증이 필수임을 알려주기
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("원활한 앱 이용을 위해\n계정 등록 후 이메일 인증을 꼭 해주세요!\n(데이터 저장, 비밀번호 찾기, 재설정에 필요)");
        builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        ImageButton_settingAccountRegister_goBack = findViewById(R.id.ImageButton_settingAccountRegister_goBack);
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
                if(EditText_settingAccountRegister_emailInput.getText().toString().equals("") || !EditText_settingAccountRegister_emailInput.getText().toString().contains("@")) {
                    Toast.makeText(SettingAccountRegister.this, R.string.registerEmailCheck, Toast.LENGTH_SHORT).show();
                } else if(!CheckBox_serviceAgree.isChecked() || !CheckBox_personalInfoAgree.isChecked()){
                    Toast.makeText(SettingAccountRegister.this, R.string.registerAgreeCheck, Toast.LENGTH_SHORT).show();
                } else if(EditText_settingAccountRegister_passwordInput.getText().toString().equals(EditText_settingAccountRegister_passwordCheckInput.getText().toString())){
//                    SettingAccount.accountList.put(EditText_settingAccountRegister_emailInput.getText().toString(), EditText_settingAccountRegister_passwordInput.getText().toString());
                    String email = EditText_settingAccountRegister_emailInput.getText().toString();
                    String password = EditText_settingAccountRegister_passwordInput.getText().toString();

                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SettingAccountRegister.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Log.d(TAG, ""+user);
                                        user.sendEmailVerification();

                                        Toast.makeText(SettingAccountRegister.this, R.string.registerPasswordCheckSucceed, Toast.LENGTH_SHORT).show();
                                        finish();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SettingAccountRegister.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                } else {
                    Toast.makeText(SettingAccountRegister.this, R.string.registerPasswordCheckFail, Toast.LENGTH_SHORT).show();
                }


            }
        });



    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
//            case R.id.CheckBox_all:
//                if(CheckBox_all.isChecked()){
//                    CheckBox_all.setChecked(true);
//                    CheckBox_serviceAgree.setChecked(true);
//                    CheckBox_personalInfoAgree.setChecked(true);
//                } else {
//                    CheckBox_all.setChecked(false);
//                    CheckBox_serviceAgree.setChecked(false);
//                    CheckBox_personalInfoAgree.setChecked(false);
//                }
//                break;
            case R.id.CheckBox_serviceAgree:
                if(CheckBox_serviceAgree.isChecked()){
                    CheckBox_serviceAgree.setChecked(true);
//                    if(CheckBox_personalInfoAgree.isChecked()){
//                        CheckBox_all.setChecked(true);
//                    }
                } else {
                    CheckBox_serviceAgree.setChecked(false);
//                    if(CheckBox_all.isChecked()){
//                        CheckBox_all.setChecked(false);
//                    }
                }
                break;
            case R.id.CheckBox_personalInfoAgree:
                if(CheckBox_personalInfoAgree.isChecked()){
                    CheckBox_personalInfoAgree.setChecked(true);
//                    if(CheckBox_serviceAgree.isChecked()){
//                        CheckBox_all.setChecked(true);
//                    }
                } else {
                    CheckBox_personalInfoAgree.setChecked(false);
//                    if(CheckBox_all.isChecked()){
//                        CheckBox_all.setChecked(false);
//                    }
                }
                break;
            default:
                break;
        }
    }
}