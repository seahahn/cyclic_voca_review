package com.seahahn.cyclicvocareview.vocagroup;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.Toast;

import android.content.SharedPreferences;
import android.widget.Spinner;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.seahahn.cyclicvocareview.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class VocagroupAdd extends AppCompatActivity {
    final String TAG = "VocagroupAdd";

    // 단어장 추가 액티비티를 구성하는 클래스
    // 이 액티비티에서 입력한 데이터는 우측 상단 '추가' 버튼 클릭 후 메인 액티비티의 단어장 목록으로 전송되어 저장됨

    ImageButton ImageButton_vocagroupAdd_goback;
    Button Button_vocagroupAdd_add;
    TextView TextView_vocaAdd_areaAdd;

    Spinner Spinner_vocagroupAdd_vocagroupCycle;
    String vocaLearningCycleName;

    EditText EditText_vocagroupAdd_vocagroupNameInput;
    String EditText_vocagroupAdd_vocagroupNameInputString;
    EditText EditText_vocagroupAdd_vocagroupArea1Input;
    EditText EditText_vocagroupAdd_vocagroupArea2Input;
    Switch Switch_vocagroupAdd_area1Switch;
    Switch Switch_vocagroupAdd_area2Switch;

    private ArrayList<VocaLearningCycle> vocaLearningCycleList = new ArrayList<>();
    private ArrayList<VocagroupArea> vocagroupArea = new ArrayList<>();
    private VocagroupAreaAdapter vocagroupAreaAdapter;
    private RecyclerView ListView_vocagroupAdd_listview;

    // 기존에 있는 단어장 제목과 새로운 단어장 제목의 중복 방지를 위해 생성
    // 단어장 목록 데이터를 이 리스트에 불러온 후, 데이터 내의 단어장 제목들과 새로운 단어장 제목의 중복을 체크함
    private ArrayList<Vocagroup> vocagroupList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocagroup_add);

        // 뒤로 가기 버튼 기능 구현
        ImageButton_vocagroupAdd_goback = findViewById(R.id.ImageButton_vocagroupAdd_goback);
        ImageButton_vocagroupAdd_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(VocagroupAdd.this, MainActivity.class);
//                startActivity(intent);
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        // 단어 학습 주기 선택 스피너 기능 구현
        Spinner_vocagroupAdd_vocagroupCycle = findViewById(R.id.Spinner_vocagroupAdd_vocagroupCycle);
         //문자열 배열 및 기본 스피너 레이아웃을 사용하여 ArrayAdapter 생성
         VocaLearningCycleAdapter spinnerAdapter = new VocaLearningCycleAdapter(this, vocaLearningCycleList);
        // 단어 학습 주기 목록을 담은 데이터 불러오기
//        SharedPreferences sharedPreferences = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
        SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
        Gson gson = new Gson();
        String vocaLearningCycleListJson = sharedPreferences.getString("VocaLearningCycleList", null);
        Type vocaLearningCycleListType = new TypeToken<ArrayList<VocaLearningCycle>>(){}.getType();
        vocaLearningCycleList = gson.fromJson(vocaLearningCycleListJson, vocaLearningCycleListType); // 기존에 저장된 단어 학습 주기 있으면 데이터 가져옴
        spinnerAdapter.setData(vocaLearningCycleList);
         // 스피너에 어댑터 적용
        Spinner_vocagroupAdd_vocagroupCycle.setAdapter(spinnerAdapter);
        // 스피너 선택 시 취해질 기능 구현 -> 단어장에 학습 주기 지정하기
        Spinner_vocagroupAdd_vocagroupCycle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vocaLearningCycleName = vocaLearningCycleList.get(position).getVocaLearningCycleName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });


        // 하단에 + 영역 추가하기 텍스트뷰 눌렀을 때 기능 구현
        ListView_vocagroupAdd_listview = findViewById(R.id.ListView_vocagroupAdd_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ListView_vocagroupAdd_listview.setLayoutManager(linearLayoutManager);
        vocagroupAreaAdapter = new VocagroupAreaAdapter(this, vocagroupArea);
        ListView_vocagroupAdd_listview.setAdapter(vocagroupAreaAdapter);

        TextView_vocaAdd_areaAdd = findViewById(R.id.TextView_vocaAdd_areaAdd);
        TextView_vocaAdd_areaAdd.setClickable(true);
        TextView_vocaAdd_areaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vocagroupAreaAdapter.addItem(new VocagroupArea("추가 영역", "", false));
                vocagroupAreaAdapter.notifyDataSetChanged();
//                setListViewHeightBasedOnChildren(ListView_vocagroupAdd_listview);
            }
        });

        // 단어장 제목, 영역 1,2 입력 받는 EditText들
        EditText_vocagroupAdd_vocagroupNameInput = findViewById(R.id.EditText_vocagroupAdd_vocagroupNameInput);
        EditText_vocagroupAdd_vocagroupArea1Input = findViewById(R.id.EditText_vocagroupAdd_vocagroupArea1Input);
        EditText_vocagroupAdd_vocagroupArea2Input = findViewById(R.id.EditText_vocagroupAdd_vocagroupArea2Input);
        // 영역 1,2 우측 단어 앞/뒤 결정하는 스위치들
        Switch_vocagroupAdd_area1Switch = findViewById(R.id.Switch_vocagroupAdd_area1Switch);
        Switch_vocagroupAdd_area2Switch = findViewById(R.id.Switch_vocagroupAdd_area2Switch);



        // 단어장 추가 전에 기존에 있는 단어장 제목과의 중복 방지를 위한 단어장 목록 데이터 불러오기
        SharedPreferences sharedPreferencesVocaName = getSharedPreferences(userID, MODE_PRIVATE);
        Gson gsonVocaName = new Gson();
        String vocagroupListJson = sharedPreferencesVocaName.getString("VocagroupList", null);
        Type vocagroupListType = new TypeToken<ArrayList<Vocagroup>>(){}.getType();
        if(gsonVocaName.fromJson(vocagroupListJson, vocagroupListType) != null && gsonVocaName.fromJson(vocagroupListJson, vocagroupListType).toString().length() >2){
            vocagroupList = gsonVocaName.fromJson(vocagroupListJson, vocagroupListType); // 기존에 저장된 단어장 있으면 데이터 가져옴
        }
//        if(vocagroupList != null) {
//            if (vocagroupList.size() > 0) {
//                vocagroupAdapter.setData(vocagroupList);
//            }
//        }


        // 단어장 추가 기능 구현 -> 메인 액티비티의 단어장 목록에 추가됨
        Button_vocagroupAdd_add = findViewById(R.id.Button_vocagroupAdd_add);
        Button_vocagroupAdd_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 단어장 제목 중복 체크
                boolean vocagroupOverlapCheck = false;
                for(int i = 0; i < vocagroupList.size(); i++){
                    if(vocagroupList.get(i).getVocagroupName().equals(EditText_vocagroupAdd_vocagroupNameInput.getText().toString()+" vocagroupName")){
                        vocagroupOverlapCheck = true;
                    }
                }

                if(EditText_vocagroupAdd_vocagroupNameInput.getText().toString().isEmpty()) {
                    // 단어장 제목을 입력하지 않았을 경우
                    Toast.makeText(getApplicationContext(), "단어장 제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (EditText_vocagroupAdd_vocagroupArea1Input.getText().toString().isEmpty() ||
                        EditText_vocagroupAdd_vocagroupArea2Input.getText().toString().isEmpty() ||
                        vocagroupAreaAdapter.isVocagroupAreaEmpty()) {
                    // 단어장 영역 이름을 입력하지 않았을 경우
                    Toast.makeText(getApplicationContext(), "단어장 영역 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else if (vocagroupOverlapCheck) {
                    // 단어장 제목이 기존에 있는 단어장의 제목과 동일한 경우
                    Toast.makeText(getApplicationContext(), "동일한 제목의 단어장이 있습니다.\n다른 제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(Switch_vocagroupAdd_area1Switch.isChecked() == Switch_vocagroupAdd_area2Switch.isChecked()){
                    Toast.makeText(getApplicationContext(), "영역 1과 2의 앞/뒤 방향을 다르게 설정해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Vocagroup vocagroup = new Vocagroup(EditText_vocagroupAdd_vocagroupNameInput.getText().toString(),
                            vocaLearningCycleName,
                            Spinner_vocagroupAdd_vocagroupCycle.getSelectedItemPosition(),
                            EditText_vocagroupAdd_vocagroupArea1Input.getText().toString(),
                            EditText_vocagroupAdd_vocagroupArea2Input.getText().toString(),
                            Switch_vocagroupAdd_area1Switch.isChecked(), Switch_vocagroupAdd_area2Switch.isChecked(),
                            vocagroupAreaAdapter.getData());
                    // Spinner_vocagroupAdd_vocagroupCycle.getSelectedItem().toString(),

//                    Date date = new Date();
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm");

                    String vocagroupName = EditText_vocagroupAdd_vocagroupNameInput.getText().toString() + " vocagroupName"; // spKey 각각의 저장된 객체마다 고유한 이름 가질 수 있도록 수정 필요
                    SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String vocagroupJsonSave = gson.toJson(vocagroup);
                    editor.putString(vocagroupName, vocagroupJsonSave); // 저장할 값 입력하기
                    editor.commit();

                    Intent intent = new Intent();
                    intent.putExtra("vocagroupName", vocagroupName);

                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("groupname", EditText_vocagroupAdd_vocagroupNameInput.getText().toString()); // 단어장 제목 텍스트 임시 저장

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        EditText_vocagroupAdd_vocagroupNameInputString = savedInstanceState.getString("groupname"); // 단어장 제목 텍스트 임시 저장한 것 불러오기

        EditText_vocagroupAdd_vocagroupNameInput.setText(EditText_vocagroupAdd_vocagroupNameInputString);
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