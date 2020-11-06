package com.seahahn.cyclicvocareview;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import org.jetbrains.annotations.NotNull;

import java.util.Hashtable;

import static com.seahahn.cyclicvocareview.MainActivity.userEmail;
import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class SettingAccount extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SettingAccount";
    ImageButton ImageButton_settingAccount_goBack;
    TextView TextView_settingAccount_register;
    TextView TextView_settingAccount_findPassword;
    EditText EditText_settingAccount_emailInput;
    EditText EditText_settingAccount_passwordInput;
    Button Button_settingAccount_Login;

    static boolean accountLogin;

    static Hashtable<String, String> accountList = new Hashtable<>();

    private SignInButton btn_google; // 구글 로그인 버튼
    private FirebaseAuth mAuth; // 파이어베이스 인증 객체
    private GoogleApiClient googleApiClient; // 구글 API 클라이언트 객체
    private static final int REQ_SIGN_GOOGLE = 100; // 구글 로그인 결과 코드


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_account);


        // 구글 로그인 기능 구현
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        mAuth = FirebaseAuth.getInstance(); // 파이어베이스 인증 객체 초기화

        btn_google = findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() { // 구글 로그인 버튼 클릭 시 행동
            @Override
            public void onClick(View v) {
                Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);
            }
        });

        // 뒤로가기 버튼
        ImageButton_settingAccount_goBack = findViewById(R.id.ImageButton_settingAccount_goBack);
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

                String email = EditText_settingAccount_emailInput.getText().toString();
                String password = EditText_settingAccount_passwordInput.getText().toString();

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SettingAccount.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(user.isEmailVerified()){
                                        Toast.makeText(getApplicationContext(), "로그인 성공",Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "이메일 인증을 해주세요.",Toast.LENGTH_SHORT).show();
                                    }

                                    if(FirebaseAuth.getInstance().getCurrentUser() != null){
                                        userID = FirebaseAuth.getInstance().getUid();
                                        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                                    }

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
//                                    Toast.makeText(SettingAccount.this, getString(R.string.loginFail), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(SettingAccount.this, "올바르지 않은 이메일 또는 비밀번호입니다.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // 계정 만들기 버튼
        TextView_settingAccount_register = findViewById(R.id.TextView_settingAccount_register);
        TextView_settingAccount_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingAccount.this, SettingAccountRegister.class);
                startActivity(intent);
            }
        });

        // 비밀번호 찾기 버튼
        TextView_settingAccount_findPassword = findViewById(R.id.TextView_settingAccount_findPassword);
        TextView_settingAccount_findPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingAccount.this, SettingAccountFindpassword.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 구글 로그인 결과 받는 곳
        if(requestCode == REQ_SIGN_GOOGLE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount(); // account 안에 구글 닉네임, 프로필 사진 URL, 이메일 등 데이터 다 들어있음
                resultLogin(account); // 로그인 결과값 출력 수행하는 메소드
                accountLogin = true;
            }
        }
    }

    private void resultLogin(final GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            // 로그인 성공한 경우
                            Toast.makeText(getApplicationContext(), "구글 로그인 성공",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Setting.class);

                            if(FirebaseAuth.getInstance() != null){
                                userID = FirebaseAuth.getInstance().getUid();
                                userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                            }

                            startActivity(intent);
                        } else {
                            // 로그인 실패한 경우
                            Toast.makeText(getApplicationContext(), "구글 로그인 실패",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull @NotNull ConnectionResult connectionResult) {

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