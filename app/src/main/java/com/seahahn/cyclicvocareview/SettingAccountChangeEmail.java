package com.seahahn.cyclicvocareview;

import android.content.Context;
import android.graphics.Rect;
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

public class SettingAccountChangeEmail extends AppCompatActivity {

    private static final String TAG = "SettingAccountChangeEmail";
    ImageButton ImageButton_settingAccountChangeEmail_goBack;
    Button Button_settingAccountChangeEmail_change;

    EditText EditText_settingAccountChangeEmail_currentPasswordInput;
    EditText EditText_settingAccountChangeEmail_newEmailInput;
    EditText EditText_settingAccountChangeEmail_newEmailCheckInput;

    String currentPassword;
    String newEmail;
    String newEmailCheck;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account_change_email);

        // 좌측 상단 좌향 화살표 이미지버튼 - 뒤로 가기 기능 구현
        ImageButton_settingAccountChangeEmail_goBack = findViewById(R.id.ImageButton_settingAccountChangeEmail_goBack);
        ImageButton_settingAccountChangeEmail_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SettingAccountChangePassword.this, SettingAccountChangeInfo.class);
//                startActivity(intent);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화

        EditText_settingAccountChangeEmail_currentPasswordInput = findViewById(R.id.EditText_settingAccountChangeEmail_currentPasswordInput);
        EditText_settingAccountChangeEmail_newEmailInput = findViewById(R.id.EditText_settingAccountChangeEmail_newEmailInput);
        EditText_settingAccountChangeEmail_newEmailCheckInput =findViewById(R.id.EditText_settingAccountChangeEmail_newEmailCheckInput);

        Button_settingAccountChangeEmail_change = findViewById(R.id.Button_settingAccountChangeEmail_change);
        Button_settingAccountChangeEmail_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPassword = EditText_settingAccountChangeEmail_currentPasswordInput.getText().toString();
                newEmail = EditText_settingAccountChangeEmail_newEmailInput.getText().toString();
                newEmailCheck = EditText_settingAccountChangeEmail_newEmailCheckInput.getText().toString();

                if(!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                    Toast.makeText(getApplicationContext(), R.string.registerEmailCheck, Toast.LENGTH_SHORT).show();

                } else if(!newEmail.equals(newEmailCheck)){
                    Toast.makeText(getApplicationContext(), "새 이메일 입력이 일치하지 않습니다.",Toast.LENGTH_SHORT).show();

                } else if(currentPassword == null || newEmail == null || newEmailCheck == null) {
                    Toast.makeText(getApplicationContext(), "빈 입력칸을 채워주세요.",Toast.LENGTH_SHORT).show();

                } else {
                    String email = userEmail;
                    String password = currentPassword;

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SettingAccountChangeEmail.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        user.updateEmail(newEmail);
                                        user.sendEmailVerification();
                                        Toast.makeText(getApplicationContext(), "이메일 변경 완료",Toast.LENGTH_SHORT).show();

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