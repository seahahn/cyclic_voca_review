package com.seahahn.cyclicvocareview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import static com.seahahn.cyclicvocareview.MainActivity.userEmail;
import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class SettingAccountChangePassword extends AppCompatActivity {

    private static final String TAG = "SettingAccountChangePassword";
    ImageButton ImageButton_settingAccountChangePassword_goBack;
    Button Button_settingAccountChangePassword_change;

    EditText EditText_settingAccountChangePassword_currentPasswordInput;
    EditText EditText_settingAccountChangePassword_newPasswordInput;
    EditText EditText_settingAccountChangePassword_newPasswordCheckInput;

    String currentPassword;
    String newPassword;
    String newPasswordCheck;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account_change_password);

        // 좌측 상단 좌향 화살표 이미지버튼 - 뒤로 가기 기능 구현
        ImageButton_settingAccountChangePassword_goBack = findViewById(R.id.ImageButton_settingAccountChangePassword_goBack);
        ImageButton_settingAccountChangePassword_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SettingAccountChangePassword.this, SettingAccountChangeInfo.class);
//                startActivity(intent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화

        EditText_settingAccountChangePassword_currentPasswordInput = findViewById(R.id.EditText_settingAccountChangePassword_currentPasswordInput);
        EditText_settingAccountChangePassword_newPasswordInput = findViewById(R.id.EditText_settingAccountChangePassword_newPasswordInput);
        EditText_settingAccountChangePassword_newPasswordCheckInput = findViewById(R.id.EditText_settingAccountChangePassword_newPasswordCheckInput);

        Button_settingAccountChangePassword_change = findViewById(R.id.Button_settingAccountChangePassword_change);
        Button_settingAccountChangePassword_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPassword = EditText_settingAccountChangePassword_currentPasswordInput.getText().toString();
                newPassword = EditText_settingAccountChangePassword_newPasswordInput.getText().toString();
                newPasswordCheck = EditText_settingAccountChangePassword_newPasswordCheckInput.getText().toString();

                if(!newPassword.equals(newPasswordCheck)){
                    Toast.makeText(getApplicationContext(), "새 비밀번호 입력이 일치하지 않습니다.",Toast.LENGTH_SHORT).show();

                } else if(currentPassword == null || newPassword == null || newPasswordCheck == null) {
                    Toast.makeText(getApplicationContext(), "빈 입력칸을 채워주세요.",Toast.LENGTH_SHORT).show();

                }else if(!Pattern.matches("^(?=.*\\d)(?=.*[~`!@#$%\\^&*()-])(?=.*[a-z])(?=.*[A-Z]).{8,12}$", newPassword)) {
                    Toast.makeText(getApplicationContext(),"비밀번호 형식을 지켜주세요.",Toast.LENGTH_SHORT).show();

                } else {
                    String email = userEmail;
                    String password = currentPassword;

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SettingAccountChangePassword.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        user.updatePassword(newPassword);
                                        Toast.makeText(getApplicationContext(), "비밀번호 변경 완료",Toast.LENGTH_SHORT).show();

                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "현재 비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();

            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);

                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }
}