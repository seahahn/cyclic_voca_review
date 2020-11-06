package com.seahahn.cyclicvocareview;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

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

public class VocaLearningCycleModify extends AppCompatActivity {

    ImageButton ImageButton_vocaLearningCycleModify_goback;
    Button Button_vocaLearningCycleModify_modify;
    TextView TextView_vocaLearningCycleModify_areaAdd;

    EditText EditText_vocaLearningCycleModify_vocaLearningCycleNameInput;
    EditText EditText_vocaLearningCycleModify_vocaLearningCycleArea1Input;
    EditText EditText_vocaLearningCycleModify_vocaLearningCycleArea2Input;


    private ArrayList<VocaLearningCycleArea> vocaLearningCycleArea = new ArrayList<>();
    private VocaLearningCycleAreaAdapter vocaLearningCycleAreaAdapter;
    private RecyclerView ListView_vocaLearningCycleModify_listview;

    private ArrayList<VocaLearningCycle> vocaLearningCycleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_learning_cycle_modify);

        // 화면 좌측 상단 좌향 화살표 이미지버튼 - 뒤로 가기 기능 구현
        ImageButton_vocaLearningCycleModify_goback = findViewById(R.id.ImageButton_vocaLearningCycleModify_goback);
        ImageButton_vocaLearningCycleModify_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 화면 하단에 + 주기 추가하기 텍스트뷰 눌렀을 때 기능 구현
        ListView_vocaLearningCycleModify_listview = findViewById(R.id.ListView_vocaLearningCycleModify_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ListView_vocaLearningCycleModify_listview.setLayoutManager(linearLayoutManager);
        vocaLearningCycleAreaAdapter = new VocaLearningCycleAreaAdapter(this, vocaLearningCycleArea);
        ListView_vocaLearningCycleModify_listview.setAdapter(vocaLearningCycleAreaAdapter);

        TextView_vocaLearningCycleModify_areaAdd = findViewById(R.id.TextView_vocaLearningCycleModify_areaAdd);
        TextView_vocaLearningCycleModify_areaAdd.setClickable(true);
        TextView_vocaLearningCycleModify_areaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vocaLearningCycleAreaAdapter.addItem(new VocaLearningCycleArea("추가 주기", ""));
                vocaLearningCycleAreaAdapter.notifyDataSetChanged();
            }
        });

        // 단어 학습 주기 이름, 주기 1,2 입력 받는 EditText들
        EditText_vocaLearningCycleModify_vocaLearningCycleNameInput = findViewById(R.id.EditText_vocaLearningCycleModify_vocaLearningCycleNameInput);
        EditText_vocaLearningCycleModify_vocaLearningCycleArea1Input = findViewById(R.id.EditText_vocaLearningCycleModify_vocaLearningCycleArea1Input);
        EditText_vocaLearningCycleModify_vocaLearningCycleArea2Input = findViewById(R.id.EditText_vocaLearningCycleModify_vocaLearningCycleArea2Input);

        // 수정하기 전에 인텐트 값 받아서 기존 값들 세팅해놓기
        Intent intentModify = getIntent();
        final String vocagroupVLC = intentModify.getStringExtra("vocagroupVLC");
        final int vocaLearningCyclePosition = intentModify.getIntExtra("단어학습주기 포지션", -1);
//        SharedPreferences sharedPreferences = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
        Gson gson = new Gson();
        String vocaLearningCycleJsonLoad = sharedPreferences.getString(vocagroupVLC, null);
        VocaLearningCycle vocaLearningCycle = gson.fromJson(vocaLearningCycleJsonLoad, VocaLearningCycle.class);

        EditText_vocaLearningCycleModify_vocaLearningCycleNameInput.setText(vocaLearningCycle.getVocaLearningCycleName());
        EditText_vocaLearningCycleModify_vocaLearningCycleArea1Input.setText(vocaLearningCycle.getVocaLearningCycleArea1());
        EditText_vocaLearningCycleModify_vocaLearningCycleArea2Input.setText(vocaLearningCycle.getVocaLearningCycleArea2());
        vocaLearningCycleArea.addAll(vocaLearningCycle.getVocaLearningCycleAreaList());
        for(int i=0; i < vocaLearningCycle.getVocaLearningCycleAreaList().size(); i++){
            vocaLearningCycleAreaAdapter.setItem(i, vocaLearningCycleArea, vocaLearningCycle.getVocaLearningCycleAreaList().get(i));
            vocaLearningCycleAreaAdapter.notifyItemChanged(i);
        }



        // 단어 학습 주기 추가 전에 기존에 있는 단어 학습 주기 제목과의 중복 방지를 위한 단어 학습 주기 목록 데이터 불러오기
//        SharedPreferences sharedPreferencesVocaName = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
        SharedPreferences sharedPreferencesVocaName = getSharedPreferences(userID, MODE_PRIVATE);
        Gson gsonVocaName = new Gson();
        String vocaLearningCycleListJson = sharedPreferencesVocaName.getString("VocaLearningCycleList", null);
        Type vocaLearningCycleListType = new TypeToken<ArrayList<VocaLearningCycle>>(){}.getType();
        if(gsonVocaName.fromJson(vocaLearningCycleListJson, vocaLearningCycleListType) != null && gsonVocaName.fromJson(vocaLearningCycleListJson, vocaLearningCycleListType).toString().length() >2){
            vocaLearningCycleList = gsonVocaName.fromJson(vocaLearningCycleListJson, vocaLearningCycleListType); // 기존에 저장된 단어 학습 주기 있으면 데이터 가져옴
        }

        // 단어 학습 주기 수정 기능 구현 -> 학습 주기 관리 액티비티에서 가져온 학습 주기의 정보를 수정함
        Button_vocaLearningCycleModify_modify = findViewById(R.id.Button_vocaLearningCycleModify_modify);
        Button_vocaLearningCycleModify_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 단어 학습 주기 제목 중복 체크
                boolean vocaLearningCycleOverlapCheck = false;
                for(int i = 0; i < vocaLearningCycleList.size(); i++){
                    if(vocaLearningCycleList.get(i).getVocaLearningCycleName().equals(EditText_vocaLearningCycleModify_vocaLearningCycleNameInput.getText().toString())){
                        vocaLearningCycleOverlapCheck = true;
                        if(vocagroupVLC.equals(EditText_vocaLearningCycleModify_vocaLearningCycleNameInput.getText().toString() + " vocaLearningCycle")){
                            vocaLearningCycleOverlapCheck = false;
                        }
                    }
                }

                if(EditText_vocaLearningCycleModify_vocaLearningCycleNameInput.getText().toString().isEmpty()) {
                    // 학습 주기 이름을 입력하지 않았을 경우
                    Toast.makeText(getApplicationContext(), "학습 주기 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else if (EditText_vocaLearningCycleModify_vocaLearningCycleArea1Input.getText().toString().isEmpty() ||
                            EditText_vocaLearningCycleModify_vocaLearningCycleArea2Input.getText().toString().isEmpty() ||
                            vocaLearningCycleAreaAdapter.isSAreaEmpty()) {
                        // 학습 주기 숫자를 입력하지 않았을 경우
                        Toast.makeText(getApplicationContext(), "학습 주기를 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else if (vocaLearningCycleOverlapCheck) {
                    // 단어 학습 주기 제목이 기존에 있는 단어 학습 주기의 제목과 동일한 경우
                    Toast.makeText(getApplicationContext(), "동일한 제목의 단어 학습 주기가 있습니다.\n다른 제목을 입력해주세요.",Toast.LENGTH_SHORT).show();

                } else {
                    VocaLearningCycle vocaLearningCycle = new VocaLearningCycle(EditText_vocaLearningCycleModify_vocaLearningCycleNameInput.getText().toString(),
                            EditText_vocaLearningCycleModify_vocaLearningCycleArea1Input.getText().toString(),
                            EditText_vocaLearningCycleModify_vocaLearningCycleArea2Input.getText().toString(),
                            vocaLearningCycleAreaAdapter.getData());

                    SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String vocaLearningCycleJsonSave = gson.toJson(vocaLearningCycle);
                    editor.putString(vocagroupVLC, vocaLearningCycleJsonSave); // 저장할 값 입력하기
                    editor.commit();

                    Intent intent = new Intent();
                    intent.putExtra("vocagroupVLC", vocagroupVLC);
                    intent.putExtra("단어학습주기 포지션", vocaLearningCyclePosition);

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