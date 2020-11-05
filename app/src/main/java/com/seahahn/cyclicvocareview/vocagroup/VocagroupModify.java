package com.seahahn.cyclicvocareview.vocagroup;
import android.util.Log;
import android.widget.Toast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.seahahn.cyclicvocareview.R;
import com.seahahn.cyclicvocareview.VocaLearningCycle;
import com.seahahn.cyclicvocareview.VocaLearningCycleAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seahahn.cyclicvocareview.VocaShowItem;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static com.seahahn.cyclicvocareview.MainActivity.userID;

public class VocagroupModify extends AppCompatActivity {

    final String TAG = "VocagroupModify";

    ImageButton ImageButton_vocagroupModify_goback;
    Button Button_vocagroupModify_modify;
    TextView TextView_vocaModify_areaAdd;

    Spinner Spinner_vocagroupModify_vocagroupCycle;
    String vocaLearningCycleName;

    EditText EditText_vocagroupModify_vocagroupNameInput;
    EditText EditText_vocagroupModify_vocagroupArea1Input;
    EditText EditText_vocagroupModify_vocagroupArea2Input;
    Switch Switch_vocagroupModify_area1Switch;
    Switch Switch_vocagroupModify_area2Switch;

    private ArrayList<VocaLearningCycle> vocaLearningCycleList = new ArrayList<>();
    private ArrayList<VocagroupArea> vocagroupArea = new ArrayList<>();
    private VocagroupAreaAdapter vocagroupAreaAdapter;
    private RecyclerView ListView_vocagroupModify_listview;

    // 기존에 있는 단어장 제목과 새로운 단어장 제목의 중복 방지를 위해 생성
    // 단어장 목록 데이터를 이 리스트에 불러온 후, 데이터 내의 단어장 제목들과 새로운 단어장 제목의 중복을 체크함
    private ArrayList<Vocagroup> vocagroupList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocagroup_modify);

        // 뒤로 가기 버튼 기능 구현
        ImageButton_vocagroupModify_goback = findViewById(R.id.ImageButton_vocagroupModify_goback);
        ImageButton_vocagroupModify_goback.setClickable(true);
        ImageButton_vocagroupModify_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(VocagroupModify.this, MainActivity.class);
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

        Spinner_vocagroupModify_vocagroupCycle = findViewById(R.id.Spinner_vocagroupModify_vocagroupCycle);
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
        Spinner_vocagroupModify_vocagroupCycle.setAdapter(spinnerAdapter);
        // 스피너 선택 시 취해질 기능 구현 -> 단어장에 학습 주기 지정하기
        Spinner_vocagroupModify_vocagroupCycle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vocaLearningCycleName = vocaLearningCycleList.get(position).getVocaLearningCycleName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 추가 영역 출력하는 리사이클러뷰와 어댑터, 레이아웃 매니저 초기화
        ListView_vocagroupModify_listview = findViewById(R.id.ListView_vocagroupModify_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ListView_vocagroupModify_listview.setLayoutManager(linearLayoutManager);
        vocagroupAreaAdapter = new VocagroupAreaAdapter(this, vocagroupArea); // 받은 리스트 데이터를 어댑터에 꽂음
        ListView_vocagroupModify_listview.setAdapter(vocagroupAreaAdapter);

        // 하단에 영역 추가하기 텍스트뷰 눌렀을 때 기능 구현
        TextView_vocaModify_areaAdd = findViewById(R.id.TextView_vocaModify_areaAdd);
        TextView_vocaModify_areaAdd.setClickable(true);
        TextView_vocaModify_areaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vocagroupAreaAdapter.addItem(new VocagroupArea("추가 영역 ", "", false));
                vocagroupAreaAdapter.notifyDataSetChanged();
            }
        });

        // 단어장 제목, 영역 1,2 입력 받는 EditText들, 영역 1,2 스위치들
        EditText_vocagroupModify_vocagroupNameInput = findViewById(R.id.EditText_vocagroupModify_vocagroupNameInput);
        EditText_vocagroupModify_vocagroupArea1Input = findViewById(R.id.EditText_vocagroupModify_vocagroupArea1Input);
        EditText_vocagroupModify_vocagroupArea2Input = findViewById(R.id.EditText_vocagroupModify_vocagroupArea2Input);
        Switch_vocagroupModify_area1Switch = findViewById(R.id.Switch_vocagroupModify_area1Switch);
        Switch_vocagroupModify_area2Switch = findViewById(R.id.Switch_vocagroupModify_area2Switch);



        // 수정하기 전에 인텐트 값 받아서 기존 값들 세팅해놓기
        Intent intentModify = getIntent();
        final String vocagroupName = intentModify.getStringExtra("vocagroupName");
        final int vocagroupPosition = intentModify.getIntExtra("단어장 포지션", -1);
        SharedPreferences sharedPreferencesM = getSharedPreferences(userID, MODE_PRIVATE);
        Gson gsonM = new Gson();
        String vocagroupJsonLoad = sharedPreferencesM.getString(vocagroupName, null);
        Vocagroup vocagroup = gsonM.fromJson(vocagroupJsonLoad, Vocagroup.class);

        EditText_vocagroupModify_vocagroupNameInput.setText(vocagroup.getVocagroupName());
        Spinner_vocagroupModify_vocagroupCycle.setSelection(vocagroup.getVocaLearningCyclePosition());
        EditText_vocagroupModify_vocagroupArea1Input.setText(vocagroup.getVocagroupArea1());
        EditText_vocagroupModify_vocagroupArea2Input.setText(vocagroup.getVocagroupArea2());
        Switch_vocagroupModify_area1Switch.setChecked(vocagroup.isVocagroupAreaSwitch1());
        Switch_vocagroupModify_area2Switch.setChecked(vocagroup.isVocagroupAreaSwitch2());
        vocagroupArea.addAll(vocagroup.getVocagroupAreaList());
        System.out.println("getVocagroupAreaList 결과 : "+vocagroup.getVocagroupAreaList());
        System.out.println("size는 "+vocagroup.getVocagroupAreaList().size());
        for(int i=0; i < vocagroup.getVocagroupAreaList().size(); i++){
            vocagroupAreaAdapter.setItem(i, vocagroupArea, vocagroup.getVocagroupAreaList().get(i));
            vocagroupAreaAdapter.notifyItemChanged(i);
        }



        // 단어장 수정 전에 기존에 있는 단어장 제목과의 중복 방지를 위한 단어장 목록 데이터 불러오기
        SharedPreferences sharedPreferencesVocaName = getSharedPreferences(userID, MODE_PRIVATE);
        Gson gsonVocaName = new Gson();
        String vocagroupListJson = sharedPreferencesVocaName.getString("VocagroupList", null);
        Type vocagroupListType = new TypeToken<ArrayList<Vocagroup>>(){}.getType();
        if(gsonVocaName.fromJson(vocagroupListJson, vocagroupListType) != null && gsonVocaName.fromJson(vocagroupListJson, vocagroupListType).toString().length() >2){
            vocagroupList = gsonVocaName.fromJson(vocagroupListJson, vocagroupListType); // 기존에 저장된 단어장 있으면 데이터 가져옴
        }


        // 단어장 수정 기능 구현 -> 메인 액티비티에서 가져온 단어장의 정보를 수정함
        Button_vocagroupModify_modify = findViewById(R.id.Button_vocagroupModify_modify);
        Button_vocagroupModify_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 단어장 제목 중복 체크
                boolean vocagroupOverlapCheck = false;
                for(int i = 0; i < vocagroupList.size(); i++){
                    if(vocagroupList.get(i).getVocagroupName().equals(EditText_vocagroupModify_vocagroupNameInput.getText().toString())){
                        vocagroupOverlapCheck = true;
                    }
                }
                if(vocagroupName.equals(EditText_vocagroupModify_vocagroupNameInput.getText().toString()+" vocagroupName")){
                    vocagroupOverlapCheck = false;
                }

                // 단어장 영역 방향 체크
                int vocagroupSideCheck = 0;
                for(int i = 0; i < vocagroupAreaAdapter.getData().size(); i++){
                    if(vocagroupAreaAdapter.getData().get(i).isSwitch_vocagroupAdd_areaSwitch()){
                        vocagroupSideCheck++;
                    } else {
                        vocagroupSideCheck--;
                    }
                }

                if(EditText_vocagroupModify_vocagroupNameInput.getText().toString().isEmpty()) {
                    // 단어장 제목을 입력하지 않았을 경우
                    Toast.makeText(getApplicationContext(), "단어장 제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (EditText_vocagroupModify_vocagroupArea1Input.getText().toString().isEmpty() ||
                        EditText_vocagroupModify_vocagroupArea2Input.getText().toString().isEmpty() ||
                        vocagroupAreaAdapter.isVocagroupAreaEmpty()) {
                    // 단어장 영역 이름을 입력하지 않았을 경우
                    Toast.makeText(getApplicationContext(), "단어장 영역 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else if (vocagroupOverlapCheck) {
                    // 단어장 제목이 기존에 있는 단어장의 제목과 동일한 경우
                    Toast.makeText(getApplicationContext(), "동일한 제목의 단어장이 있습니다.\n다른 제목을 입력해주세요.", Toast.LENGTH_SHORT).show();

                } else if ((Switch_vocagroupModify_area1Switch.isChecked() == Switch_vocagroupModify_area2Switch.isChecked())
                        && (vocagroupSideCheck == vocagroupAreaAdapter.getData().size() || vocagroupSideCheck == -vocagroupAreaAdapter.getData().size())){
                    // 모든 영역의 스위치(앞/뒤 방향)이 동일한 경우
                    Toast.makeText(getApplicationContext(), "모든 영역의 방향이 같습니다.\n최소 1개의 영역은 다르게 설정해주세요.",Toast.LENGTH_SHORT).show();

                } else {
                    Vocagroup vocagroup = new Vocagroup(EditText_vocagroupModify_vocagroupNameInput.getText().toString(),
                            vocaLearningCycleName,
                            Spinner_vocagroupModify_vocagroupCycle.getSelectedItemPosition(),
                            EditText_vocagroupModify_vocagroupArea1Input.getText().toString(),
                            EditText_vocagroupModify_vocagroupArea2Input.getText().toString(),
                            Switch_vocagroupModify_area1Switch.isChecked(), Switch_vocagroupModify_area2Switch.isChecked(),
                            vocagroupAreaAdapter.getData());

//                    Date date = new Date();
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HH:mm");
                    // 단어장 제목이 바뀐 경우, 단어장 제목과 연결된 단어 데이터를 저장한 키값도 이에 맞춰 바꿔줘야 함
                    SharedPreferences sharedPreferences = getSharedPreferences(userID, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Gson gson = new Gson();
                    String vocaListKey = vocagroupName + " vocaList";
                    String vocaListJson = sharedPreferences.getString(vocaListKey, null);
                    Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
                    ArrayList<ArrayList<VocaShowItem>> vocaList = gson.fromJson(vocaListJson, vocaListType); // 단어장 단어 목록 불러옴

                    String vocagroupName = EditText_vocagroupModify_vocagroupNameInput.getText().toString() + " vocagroupName"; // 수정된 단어장 제목
                    String vocagroupJsonSave = gson.toJson(vocagroup); // 수정된 단어장 데이터
                    Log.d(TAG, vocagroupJsonSave);
                    editor.putString(vocagroupName, vocagroupJsonSave); // 수정된 단어장 제목을 Key로 하여 수정된 단어장 데이터 저장함

                    String vocaListJsonM = gson.toJson(vocaList); // 단어 목록을 gson String 형태로 변환
                    editor.putString(vocagroupName+" vocaList", vocaListJsonM); // 기존의 단어 목록을 수정된 단어장 제목을 Key로 하여 다시 저장
                    editor.remove(vocaListKey);
                    editor.commit();

                    Intent intent = new Intent();
                    intent.putExtra("vocagroupName", vocagroupName);
                    intent.putExtra("단어장 포지션", vocagroupPosition);

                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    public void modifySet(Context context, int position, Vocagroup vocagroup){
        String vocagroupName = vocagroup.getVocagroupName();
        String vocagroupArea1 = vocagroup.getVocagroupArea1();
        String vocagroupArea2 = vocagroup.getVocagroupArea2();
        ArrayList<VocagroupArea> vocagroupAreaList = vocagroup.getVocagroupAreaList();
    }
}