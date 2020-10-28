package com.example.vocacyclicreview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vocacyclicreview.vocagroup.Vocagroup;
import com.example.vocacyclicreview.vocagroup.VocagroupAdapter;
import com.example.vocacyclicreview.vocagroup.VocagroupAdd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class VocaLearningCycleManage extends AppCompatActivity {

    private static final int REQUEST_VOCALEARNINGCYCLE_ADD = 3;
    private static final int REQUEST_VOCALEARNINGCYCLE_MODIFY = 4;
    ImageButton ImageButton_vocaLearningCycleManage_goback;
    FloatingActionButton floatingActionButton;

    // 단어장 학습 주기 목록 출력을 위한 요소들 초기화 시작
    ArrayList<VocaLearningCycle> vocaLearningCycleList = new ArrayList<>(); // 메인에서 보여줄 단어장 리스트
    VocaLearningCycleManageAdapter vocaLearningCycleManageAdapter = new VocaLearningCycleManageAdapter(this, vocaLearningCycleList); // 리스트를 보여주기 위한 어댑터
    RecyclerView ListView_vocaLearningCycleManage_listview; // 메인의 리사이클러뷰 초기화
    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelperCallback(vocaLearningCycleManageAdapter)); // 좌우 스와이프 모션을 위한 아이템터치헬퍼 초기화 후 어댑터에 연결
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this); // 레이아웃 매니저 초기화

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_learning_cycle_manage);

        // 화면 좌측 상단 좌향 화살표 이미지버튼 - 뒤로 가기 기능 구현 (단어 학습 주기 관리 액티비티로 돌아감)
        ImageButton_vocaLearningCycleManage_goback = findViewById(R.id.ImageButton_vocaLearningCycleManage_goback);
        ImageButton_vocaLearningCycleManage_goback.setClickable(true);
        ImageButton_vocaLearningCycleManage_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(VocaLearningCycleManage.this, MainActivity.class);
//                startActivity(intent);
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        // 화면 우측 하단 플로팅버튼 - 단어 학습 주기 추가 기능 구현
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdd = new Intent(VocaLearningCycleManage.this, VocaLearningCycleAdd.class);
                startActivityForResult(intentAdd, REQUEST_VOCALEARNINGCYCLE_ADD);
            }
        });

        // 단어 학습 주기 목록 출력하기
        ListView_vocaLearningCycleManage_listview = findViewById(R.id.ListView_vocaLearningCycleManage_listview);
        ListView_vocaLearningCycleManage_listview.setLayoutManager(linearLayoutManager); // 리사이클러뷰에 레이아웃 매니저 연결
        helper.attachToRecyclerView(ListView_vocaLearningCycleManage_listview); // 리사이클러뷰에 아이템터치헬퍼 연결
        ListView_vocaLearningCycleManage_listview.setAdapter(vocaLearningCycleManageAdapter); // 리사이클러뷰에 어댑터 연결
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "VLC Manage onResume",Toast.LENGTH_LONG).show();
        vocaLearningCycleManageAdapter.notifyDataSetChanged();

        // 단어 학습 주기 목록을 담은 데이터 불러오기
        SharedPreferences sharedPreferences = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
        Gson gson = new Gson();
        String vocaLearningCycleListJson = sharedPreferences.getString("VocaLearningCycleList", null);
        Type vocaLearningCycleListType = new TypeToken<ArrayList<VocaLearningCycle>>(){}.getType();
        if(gson.fromJson(vocaLearningCycleListJson, vocaLearningCycleListType) != null && gson.fromJson(vocaLearningCycleListJson, vocaLearningCycleListType).toString().length() >2){
            vocaLearningCycleList = gson.fromJson(vocaLearningCycleListJson, vocaLearningCycleListType); // 기존에 저장된 단어 학습 주기 있으면 데이터 가져옴
        } else {
            vocaLearningCycleManageAdapter.removeAll(vocaLearningCycleList);
        }
        if(vocaLearningCycleList != null) {
            if (vocaLearningCycleList.size() > 0) {
                vocaLearningCycleManageAdapter.setData(vocaLearningCycleList);
            } else {
                vocaLearningCycleManageAdapter.removeAll(vocaLearningCycleList);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "VLCManage onPause",Toast.LENGTH_LONG).show();

        SharedPreferences sharedPreferences = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String vocaLearningCycleListJson = gson.toJson(vocaLearningCycleManageAdapter.getData());
        editor.putString("VocaLearningCycleList", vocaLearningCycleListJson); // 저장할 값 입력하기
        editor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 단어 학습 주기 추가 요청 시 결과 받는 부분
        if(requestCode == REQUEST_VOCALEARNINGCYCLE_ADD && resultCode == RESULT_OK){

            String vocagroupVLC = data.getStringExtra("vocagroupVLC");

            SharedPreferences sharedPreferences = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
            Gson gson = new Gson();
            String vocaLearningCycleJsonLoad = sharedPreferences.getString(vocagroupVLC, null);
            VocaLearningCycle vocaLearningCycle = gson.fromJson(vocaLearningCycleJsonLoad, VocaLearningCycle.class);
            vocaLearningCycleManageAdapter.addItem(vocaLearningCycle);
            vocaLearningCycleManageAdapter.notifyDataSetChanged();

        } else if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "단어 학습 주기 추가 취소", Toast.LENGTH_SHORT).show();
        }

        // 단어 학습 주기 수정 요청 시 결과 받는 부분
        if(requestCode == REQUEST_VOCALEARNINGCYCLE_MODIFY && resultCode == RESULT_OK){

            String vocagroupVLC = data.getStringExtra("vocagroupVLC");
            int vocagroupPosition = data.getIntExtra("단어학습주기 포지션", -1);

            SharedPreferences sharedPreferences = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
            Gson gson = new Gson();
            String vocaLearningCycleJsonLoad = sharedPreferences.getString(vocagroupVLC, null);
            VocaLearningCycle vocaLearningCycle = gson.fromJson(vocaLearningCycleJsonLoad, VocaLearningCycle.class);
            vocaLearningCycleList.set(vocagroupPosition, vocaLearningCycle);
            vocaLearningCycleManageAdapter.notifyDataSetChanged();

        } else if(resultCode == RESULT_CANCELED){
            Toast.makeText(this, "단어 학습 주기 수정 취소", Toast.LENGTH_SHORT).show();
        }

        SharedPreferences sharedPreferences = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String vocaLearningCycleListJson = gson.toJson(vocaLearningCycleManageAdapter.getData());
        editor.putString("VocaLearningCycleList", vocaLearningCycleListJson); // 저장할 값 입력하기
        editor.commit();
    }
}