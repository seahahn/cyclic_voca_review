package com.seahahn.cyclicvocareview;

import android.util.Log;
import android.view.View;
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
//                Toast.makeText(SettingAccountFindpassword.this, R.string.sendtemppasswordMessage, Toast.LENGTH_SHORT).show();

//                try {
//                    GMailSender gMailSender = new GMailSender("seah.ahn.nt@gmail.com", "password1234");
//                    //GMailSender.sendMail(제목, 본문내용, 받는사람);
//                    gMailSender.sendMail(getString(R.string.sendtemppasswordMailTitle),
//                            getString(R.string.sendtemppasswordMailText1) + accountList.get(EditText_settingAccountFindpassword_emailInput.getText().toString()) + getString(R.string.sendtemppasswordMailText2),
//                            EditText_settingAccountFindpassword_emailInput.getText().toString());
//                    Toast.makeText(getApplicationContext(), "이메일을 성공적으로 보냈습니다.", Toast.LENGTH_SHORT).show();
//                } catch (SendFailedException e) {
//                    Toast.makeText(getApplicationContext(), "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
//                } catch (MessagingException e) {
//                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                email = EditText_settingAccountFindpassword_emailInput.getText().toString();
                Log.d(TAG, email);

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
}