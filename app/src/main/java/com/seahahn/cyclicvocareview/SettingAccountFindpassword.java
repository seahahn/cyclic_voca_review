package com.seahahn.cyclicvocareview;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import org.jetbrains.annotations.NotNull;

public class SettingAccountFindpassword extends AppCompatActivity {

    private static final String TAG = "SettingAccountFindpassword";
    ImageButton ImageButton_settingAccountFindpassword_goBack;
    EditText EditText_settingAccountFindpassword_emailInput;
    Button Button_settingAccountFindpassword_sendPassword;

    String email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account_findpassword);

        ImageButton_settingAccountFindpassword_goBack = findViewById(R.id.ImageButton_settingAccountFindpassword_goBack);
        ImageButton_settingAccountFindpassword_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(SettingAccountFindpassword.this, SettingAccount.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
                finish();
            }
        });

        EditText_settingAccountFindpassword_emailInput = findViewById(R.id.EditText_settingAccountFindpassword_emailInput);
        Button_settingAccountFindpassword_sendPassword = findViewById(R.id.Button_settingAccountFindpassword_sendPassword);
        Button_settingAccountFindpassword_sendPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = EditText_settingAccountFindpassword_emailInput.getText().toString();

                mAuth = FirebaseAuth.getInstance();
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(SettingAccountFindpassword.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "비밀번호 재설정 메일 전송 완료",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

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