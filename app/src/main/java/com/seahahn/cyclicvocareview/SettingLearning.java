package com.seahahn.cyclicvocareview;
import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static com.seahahn.cyclicvocareview.MainActivity.userID;
import static java.lang.String.valueOf;

public class SettingLearning extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "SettingLearning";
    ImageButton ImageButton_settingLearning_goback;
    Button Button_settingLearning_vocaOrder;
    Button Button_settingLearning_vocaLimit;
    Button Button_settingLearning_countLimit;

    String newFirst = "true";
    int newVocaLimit = 30;
    int reviewVocaLimit = 30;
    int timeCount = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_learning);

        ImageButton_settingLearning_goback = findViewById(R.id.ImageButton_settingLearning_goback);
        Button_settingLearning_vocaOrder = findViewById(R.id.Button_settingLearning_vocaOrder);
        Button_settingLearning_vocaLimit = findViewById(R.id.Button_settingLearning_vocaLimit);
        Button_settingLearning_countLimit = findViewById(R.id.Button_settingLearning_countLimit);

        ImageButton_settingLearning_goback.setOnClickListener(this);
        Button_settingLearning_vocaOrder.setOnClickListener(this);
        Button_settingLearning_vocaLimit.setOnClickListener(this);
        Button_settingLearning_countLimit.setOnClickListener(this);

        // 설정 전에 기존에 저장되어 있던 값들을 불러옴
        // 사용자가 설정하고자 하는 항목에 대하여 그에 맞는 값을 넣어주기 위해서
        SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
        newFirst = sharedPreferences.getString("newFirst", "true");
        try{
            newVocaLimit = Integer.parseInt(sharedPreferences.getString("newVocaLimit", String.valueOf(30)));
            reviewVocaLimit = Integer.parseInt(sharedPreferences.getString("reviewVocaLimit", String.valueOf(30)));
            timeCount = Integer.parseInt(sharedPreferences.getString("timeCount", String.valueOf(5)));
        }catch(ClassCastException e){
            newVocaLimit = sharedPreferences.getInt("newVocaLimit", 30);
            reviewVocaLimit = sharedPreferences.getInt("reviewVocaLimit", 30);
            timeCount = sharedPreferences.getInt("timeCount", 5);
        }catch(NumberFormatException e){
            newVocaLimit = (int)Double.parseDouble(sharedPreferences.getString("newVocaLimit", String.valueOf(30)));
            reviewVocaLimit = (int)Double.parseDouble(sharedPreferences.getString("reviewVocaLimit", String.valueOf(30)));
            timeCount = (int)Double.parseDouble(sharedPreferences.getString("timeCount", String.valueOf(5)));
        }
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        switch (v.getId()) {
            case R.id.ImageButton_settingLearning_goback:
                // 액티비티 좌측 상단 좌향 화살표 이미지버튼 - 뒤로가기 기능
                finish();
                break;
            case R.id.Button_settingLearning_vocaOrder:
                // 새 단어/복습 단어 순서 설정 버튼
                // 사용자가 단어 학습 시 새 단어부터 학습할지, 이전에 학습하고 이제 복습해야 할 단어 먼저 학습할지 선택 가능
                AlertDialog.Builder builder_vocaOrder = new AlertDialog.Builder(v.getContext());
                builder_vocaOrder.setTitle("먼저 학습할 단어 유형 선택");
                int checkedItem;
                if(newFirst.equals("true")){
                    checkedItem = 0;
                } else {
                    checkedItem = 1;
                }
                builder_vocaOrder.setSingleChoiceItems(R.array.vocaOrder, checkedItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // 새 단어 먼저
                                newFirst = "true";
                                editor.putString("newFirst", "true");
                                editor.commit();
                                break;
                            case 1:
                                // 복습할 단어 먼저
                                newFirst = "false";
                                editor.putString("newFirst", "false");
                                editor.commit();
                                break;
                            default:
                                break;
                        }
                    }
                });
                builder_vocaOrder.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(newFirst.equals("true")){
                            Toast.makeText(getApplicationContext(), "새 단어 먼저 학습",Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "복습할 단어 먼저 학습",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder_vocaOrder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "먼저 학습할 단어 설정 취소",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog_vocaOrder = builder_vocaOrder.create();
                alertDialog_vocaOrder.show();
                break;

            case R.id.Button_settingLearning_vocaLimit:
                // 단어 학습 갯수 설정 버튼
                // 사용자가 단어 학습 시 새 단어와 복습할 단어를 각각 최대 몇 개씩 학습할지 설정 가능능
//               final EditText newVocaLimitInput = new EditText(v.getContext());
//                newVocaLimitInput.setText(valueOf(newVocaLimit));
//                newVocaLimitInput.setAutofillHints("새 단어 갯수 설정");
//                newVocaLimitInput.setInputType(TYPE_CLASS_NUMBER);
//                final EditText reviewVocaLimitInput = new EditText(v.getContext());
//                reviewVocaLimitInput.setText(valueOf(reviewVocaLimit));
//                reviewVocaLimitInput.setAutofillHints("복습할 단어 갯수 설정");
//                reviewVocaLimitInput.setInputType(TYPE_CLASS_NUMBER);

                View dialogView = getLayoutInflater().inflate(R.layout.activity_setting_learning_voca_limit_dialog, null);
                final EditText newVocaLimitInput = dialogView.findViewById(R.id.newVocaLimitInput);
                final EditText reviewVocaLimitInput = dialogView.findViewById(R.id.reviewVocaLimitInput);
                newVocaLimitInput.setText(valueOf(newVocaLimit));
                reviewVocaLimitInput.setText(valueOf(reviewVocaLimit));

                AlertDialog.Builder builder_vocaLimit = new AlertDialog.Builder(v.getContext());

//                builder_vocaLimit.setTitle("학습할 단어 갯수 설정");
//                builder_vocaLimit.setMessage("설정한 숫자만큼 새 단어와 복습할 단어가\n단어장 학습 목록에 추가됩니다.");
//                builder_vocaLimit.setView(newVocaLimitInput);
//                builder_vocaLimit.setView(reviewVocaLimitInput);
                builder_vocaLimit.setView(dialogView);
                builder_vocaLimit.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newVocaLimit = Integer.parseInt(newVocaLimitInput.getText().toString());
                        reviewVocaLimit = Integer.parseInt(reviewVocaLimitInput.getText().toString());
                        Log.d(TAG, "newVocaLimit : "+newVocaLimit);
                        Log.d(TAG, "reviewVocaLimit : "+reviewVocaLimit);
                        editor.putString("newVocaLimit", newVocaLimitInput.getText().toString());
                        editor.putString("reviewVocaLimit", reviewVocaLimitInput.getText().toString());
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "단어 갯수 설정되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
                builder_vocaLimit.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "단어 갯수 설정 취소",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog_vocaLimit = builder_vocaLimit.create();
                alertDialog_vocaLimit.show();

                break;
            case R.id.Button_settingLearning_countLimit:
                // 단어 학습 카운트다운 설정
                // 설정된 시간이 지나면 단어 학습 시 우측 하단의 '완전 앎' 버튼 비활성화 시킴
                final EditText countLimit = new EditText(v.getContext());
                countLimit.setText(valueOf(timeCount));
                countLimit.setAutofillHints("시간(초) 설정");
                countLimit.setInputType(TYPE_CLASS_NUMBER);

                AlertDialog.Builder builder_countLimit = new AlertDialog.Builder(v.getContext());
                builder_countLimit.setTitle("단어 학습 시 카운트다운 시간 설정");
                builder_countLimit.setView(countLimit);
                builder_countLimit.setPositiveButton(R.string.okay, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        timeCount = Integer.parseInt(countLimit.getText().toString());
                        editor.putInt("timeCount", Integer.parseInt(countLimit.getText().toString()));
                        editor.commit();
                        Toast.makeText(getApplicationContext(), "카운트다운 시간 설정되었습니다.",Toast.LENGTH_SHORT).show();
                    }
                });
                builder_countLimit.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "카운트다운 시간 설정 취소",Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = builder_countLimit.create();
                alertDialog.show();
                break;
            default:
                break;
        }
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