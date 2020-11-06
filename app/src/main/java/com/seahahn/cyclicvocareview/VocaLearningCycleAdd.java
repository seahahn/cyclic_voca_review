package com.seahahn.cyclicvocareview;
import android.util.Log;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;
import android.widget.Toast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class VocaLearningCycleAdd extends AppCompatActivity {

    ImageButton ImageButton_vocaLearningCycleAdd_goback;
    Button Button_vocaLearningCycleAdd_add;
    TextView TextView_vocaLearningCycleAdd_areaAdd;

    EditText EditText_vocaLearningCycleAdd_vocaLearningCycleNameInput;
    EditText EditText_vocaLearningCycleAdd_vocaLearningCycleArea1Input;
    EditText EditText_vocaLearningCycleAdd_vocaLearningCycleArea2Input;


    private ArrayList<VocaLearningCycleArea> vocaLearningCycleArea = new ArrayList<>();
    private VocaLearningCycleAreaAdapter vocaLearningCycleAreaAdapter;
    private RecyclerView ListView_vocaLearningCycleAdd_listview;

    private ArrayList<VocaLearningCycle> vocaLearningCycleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_learning_cycle_add);

        // 화면 좌측 상단 좌향 화살표 이미지버튼 - 뒤로 가기 기능 구현
        ImageButton_vocaLearningCycleAdd_goback = findViewById(R.id.ImageButton_vocaLearningCycleAdd_goback);
        ImageButton_vocaLearningCycleAdd_goback.setClickable(true);
        ImageButton_vocaLearningCycleAdd_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(VocaLearningCycleAdd.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        // 화면 하단에 + 주기 추가하기 텍스트뷰 눌렀을 때 기능 구현
        ListView_vocaLearningCycleAdd_listview = findViewById(R.id.ListView_vocaLearningCycleAdd_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ListView_vocaLearningCycleAdd_listview.setLayoutManager(linearLayoutManager);
        vocaLearningCycleAreaAdapter = new VocaLearningCycleAreaAdapter(this, vocaLearningCycleArea);
        ListView_vocaLearningCycleAdd_listview.setAdapter(vocaLearningCycleAreaAdapter);

        TextView_vocaLearningCycleAdd_areaAdd = findViewById(R.id.TextView_vocaLearningCycleAdd_areaAdd);
        TextView_vocaLearningCycleAdd_areaAdd.setClickable(true);
        TextView_vocaLearningCycleAdd_areaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vocaLearningCycleAreaAdapter.addItem(new VocaLearningCycleArea("추가 주기", ""));
                vocaLearningCycleAreaAdapter.notifyDataSetChanged();
            }
        });

        // 단어 학습 주기 이름, 주기 1,2 입력 받는 EditText들
        EditText_vocaLearningCycleAdd_vocaLearningCycleNameInput = findViewById(R.id.EditText_vocaLearningCycleAdd_vocaLearningCycleNameInput);
        EditText_vocaLearningCycleAdd_vocaLearningCycleArea1Input = findViewById(R.id.EditText_vocaLearningCycleAdd_vocaLearningCycleArea1Input);
        EditText_vocaLearningCycleAdd_vocaLearningCycleArea2Input = findViewById(R.id.EditText_vocaLearningCycleAdd_vocaLearningCycleArea2Input);

        // 단어 학습 주기 추가 전에 기존에 있는 단어 학습 주기 제목과의 중복 방지를 위한 단어 학습 주기 목록 데이터 불러오기
//        SharedPreferences sharedPreferencesVocaName = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
        SharedPreferences sharedPreferencesVocaName = getSharedPreferences(userID, MODE_PRIVATE);
        Gson gsonVocaName = new Gson();
        String vocaLearningCycleListJson = sharedPreferencesVocaName.getString("VocaLearningCycleList", null);
        Type vocaLearningCycleListType = new TypeToken<ArrayList<VocaLearningCycle>>(){}.getType();
        if(gsonVocaName.fromJson(vocaLearningCycleListJson, vocaLearningCycleListType) != null && gsonVocaName.fromJson(vocaLearningCycleListJson, vocaLearningCycleListType).toString().length() >2){
            vocaLearningCycleList = gsonVocaName.fromJson(vocaLearningCycleListJson, vocaLearningCycleListType); // 기존에 저장된 단어 학습 주기 있으면 데이터 가져옴
        }

        // 단어 학습 주기 추가 기능 구현 -> 단어장 추가/수정 시 단어 학습 주기 선택 목록에 추가됨
        Button_vocaLearningCycleAdd_add = findViewById(R.id.Button_vocaLearningCycleAdd_add);
        Button_vocaLearningCycleAdd_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 단어 학습 주기 제목 중복 체크
                boolean vocaLearningCycleOverlapCheck = false;
                for(int i = 0; i < vocaLearningCycleList.size(); i++){
                    if(vocaLearningCycleList.get(i).getVocaLearningCycleName().equals(EditText_vocaLearningCycleAdd_vocaLearningCycleNameInput.getText().toString())){
                        vocaLearningCycleOverlapCheck = true;
                    }
                }

                if(EditText_vocaLearningCycleAdd_vocaLearningCycleNameInput.getText().toString().isEmpty()) {
                    // 학습 주기 이름을 입력하지 않았을 경우
                    Toast.makeText(getApplicationContext(), "학습 주기 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else if (EditText_vocaLearningCycleAdd_vocaLearningCycleArea1Input.getText().toString().isEmpty() ||
                            EditText_vocaLearningCycleAdd_vocaLearningCycleArea2Input.getText().toString().isEmpty() ||
                            vocaLearningCycleAreaAdapter.isSAreaEmpty()) {
                    // 학습 주기 숫자를 입력하지 않았을 경우
                    Toast.makeText(getApplicationContext(), "학습 주기를 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else if (vocaLearningCycleOverlapCheck) {
                    // 단어 학습 주기 제목이 기존에 있는 단어 학습 주기의 제목과 동일한 경우
                    Toast.makeText(getApplicationContext(), "동일한 제목의 단어 학습 주기가 있습니다.\n다른 제목을 입력해주세요.",Toast.LENGTH_SHORT).show();

                } else {
                    VocaLearningCycle vocaLearningCycle = new VocaLearningCycle(EditText_vocaLearningCycleAdd_vocaLearningCycleNameInput.getText().toString(),
                            EditText_vocaLearningCycleAdd_vocaLearningCycleArea1Input.getText().toString(),
                            EditText_vocaLearningCycleAdd_vocaLearningCycleArea2Input.getText().toString(),
                            vocaLearningCycleAreaAdapter.getData());

                    String vocagroupVLC = EditText_vocaLearningCycleAdd_vocaLearningCycleNameInput.getText().toString() + " vocaLearningCycle";
//                    SharedPreferences sharedPreferences = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
                    SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String vocaLearningCycleJsonSave = gson.toJson(vocaLearningCycle);
                    editor.putString(vocagroupVLC, vocaLearningCycleJsonSave); // 저장할 값 입력하기
                    editor.commit();

                    Intent intent = new Intent();
                    intent.putExtra("vocagroupVLC", vocagroupVLC);

                    setResult(RESULT_OK, intent);
                    finish();
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