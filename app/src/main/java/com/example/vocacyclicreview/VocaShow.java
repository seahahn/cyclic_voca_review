package com.example.vocacyclicreview;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.vocacyclicreview.vocagroup.Vocagroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VocaShow extends AppCompatActivity implements VocaShowItemAdapter.VocaShowItemClickInterface, View.OnClickListener {

    final String TAG = "VocaShow";
    ImageButton ImageButton_vocaFront_goBack;
    ImageButton ImageButton_vocaFront_add;
    ImageButton ImageButton_vocaFront_modify;
    ImageButton ImageButton_vocaFront_search;
    ImageButton ImageButton_vocaFront_info;

    TextView TextView_vocaFront_noWords;

    ArrayList<VocaShowItem> voca = new ArrayList<>();
    ArrayList<ArrayList<VocaShowItem>> vocaList = new ArrayList<>(); // 사용자가 선택한 단어장에 있는 모든 단어 목록
    ArrayList<VocaShowItem> vocaShowItemList = new ArrayList<>(); // 단어장에 있는 단어 하나하나의 데이터를 담은 리스트. 불러오기 및 저장을 위해서 생성.
    ArrayList<VocaShowItem> vocaShowing = new ArrayList<>();
    VocaShowItemAdapter vocaShowItemAdapter = new VocaShowItemAdapter(this, vocaShowing, this);
    RecyclerView ListView_vocaFront_listview;
    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this); // 레이아웃 매니저 초기화

    ArrayList<ArrayList<VocaShowItem>> todayLearningList = new ArrayList<>();
    ArrayList<Integer> todayLearningListPosition = new ArrayList<>();

    TextView TextView_timeCount;

    int vocaOrder = 0;

    int timeCount = 5;
    int vocaNumber;

//    ArrayList<ArrayList<VocaShowItem>> vocaList;

    TextView TextView_vocaFront_newCount;
    TextView TextView_vocaFront_wrongCount;
    TextView TextView_vocaFront_reviewCount;
    String newCount;
    String wrongCount = Integer.toString(0);
    String reviewCount;
    // 틀린 단어 수
    int newCountNumber = 0;
    int wrongCountNumber = 0;
    int reviewCountNumber = 0;

    Button Button_vocaBack_dontknow;
    Button Button_vocaBack_know;
    Button Button_vocaBack_completeknow;
    boolean sideCheck = true;
    boolean buttonBelowVisible;

    boolean learningDay = false;
    boolean learningStart = false;

    String vocagroupName;
    int vocagroupPosition;
    String vocaLearningCycleName;
    Vocagroup vocagroup;
    VocaLearningCycle vocaLearningCycle;
    ArrayList<Integer> vocaLearningCycleSequence = new ArrayList<>();
    int vocaLearningCycleSequenceNumber;

    TimeCountThread timeCountThread;
//    VocaCountThread vocaCountThread;
    boolean threadOn;
    boolean onCreateAdding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca_show);

        threadOn = true;
//        Log.d(TAG, "onCreate onCreateAdding : " + onCreateAdding);

        vocaShowItemAdapter.notifyDataSetChanged();

        // 메인에서 사용자가 선택한 단어장 정보(제목, 영역 구성) 받아오기 - 단어 추가, 수정 및 단어 학습 주기 연결을 위해서
//        Intent intent = getIntent();
//        vocagroupName = intent.getStringExtra("vocagroupName");
//        vocagroupPosition = intent.getIntExtra("단어장 포지션", -1);
        SharedPreferences sharedPreferencesM = getSharedPreferences("Vocagroup", MODE_PRIVATE);
        Gson gsonM = new Gson();
        vocagroupName = sharedPreferencesM.getString("vocagroupName", null);
        vocagroupPosition = sharedPreferencesM.getInt("단어장 포지션", 100);
        String vocagroupJson = sharedPreferencesM.getString(vocagroupName, null);
        vocagroup = gsonM.fromJson(vocagroupJson, Vocagroup.class);

        vocaLearningCycleName = vocagroup.getVocaLearningCycle(); // 단어 학습 주기 데이터 가져옴
//        Log.d(TAG, "vocagroupLearningCycleName : "+ vocaLearningCycleName);

        SharedPreferences sharedPreferencesCycle = getSharedPreferences("VocaLearningCycle", MODE_PRIVATE);
        Gson gsonCycle = new Gson();
        String vocaLearningCycleJson = sharedPreferencesCycle.getString(vocaLearningCycleName, "Basic_Cycle");
//        Log.d(TAG, "vocagroupVLC : "+vocaLearningCycleJson);
        vocaLearningCycle = gsonCycle.fromJson(vocaLearningCycleJson, VocaLearningCycle.class);
//        Log.d(TAG, "vocaLearningCycle : "+vocaLearningCycle);
        vocaLearningCycleSequence.add(Integer.parseInt(vocaLearningCycle.getVocaLearningCycleArea1()));
        vocaLearningCycleSequence.add(Integer.parseInt(vocaLearningCycle.getVocaLearningCycleArea2()));
        for(int i = 0; i < vocaLearningCycle.getVocaLearningCycleAreaList().size(); i++){
            vocaLearningCycleSequence.add(Integer.parseInt(vocaLearningCycle.getVocaLearningCycleAreaList().get(i).getEditText_vocaLearningCycle_vocaLearningCycleAreaInput()));
        }
//        Log.d(TAG, "vocaLearningCycleSequence : "+ vocaLearningCycleSequence);

        // 상단 툴바 좌측 첫번째 이미지버튼 - 뒤로 가기(메인 액티비티로 돌아감)
        ImageButton_vocaFront_goBack = findViewById(R.id.ImageButton_vocaFront_goBack);
        ImageButton_vocaFront_goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VocaShow.this, MainActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        // 상단 툴바 좌측 두번째 이미지버튼 - 단어 추가(현재 사용자가 들어가 있는 단어장에)
        ImageButton_vocaFront_add = findViewById(R.id.ImageButton_vocaFront_add);
        ImageButton_vocaFront_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 메인에서 사용자가 선택한 단어장 정보(제목, 영역 구성) 단어 추가 액티비티에 보내기
                Intent intent = new Intent(VocaShow.this, VocaAdd.class);

                SharedPreferences sharedPreferences = getSharedPreferences("Vocagroup", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String vocagroupJson = gson.toJson(vocagroup);
                editor.putString(vocagroupName, vocagroupJson); // 저장할 값 입력하기
                editor.commit();

//                Log.d(TAG, "vocagroupName : "+vocagroupName);
                intent.putExtra("vocagroupName", vocagroupName);
                intent.putExtra("단어장 포지션", vocagroupPosition);
                startActivity(intent);
            }
        });



        // 상단 툴바 우측 두번째 이미지버튼 - 단어 검색(단어 검색(VocaSearch) 액티비티로 이동)
        ImageButton_vocaFront_search = findViewById(R.id.ImageButton_vocaFront_search);
        ImageButton_vocaFront_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VocaShow.this, VocaSearch.class);
                startActivity(intent);
            }
        });

        // 상단 툴바 우측 첫번째 이미지버튼 - 단어 정보(단어가 포함된 단어장, 단어가 현재 위치한 학습 주기, 단어장의 각 영역별 입력값 확인 가능)
        ImageButton_vocaFront_info = findViewById(R.id.ImageButton_vocaFront_info);
        ImageButton_vocaFront_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VocaShow.this, VocaInfo.class);
                startActivity(intent);
            }
        });

        // 하단에 New, Wrong, Review 각각 옆에 해당하는 단어 수 표시할 텍스트뷰 초기화
        TextView_vocaFront_newCount = findViewById(R.id.TextView_vocaFront_newCount);
        TextView_vocaFront_wrongCount = findViewById(R.id.TextView_vocaFront_wrongCount);
        TextView_vocaFront_reviewCount = findViewById(R.id.TextView_vocaFront_reviewCount);

        // 하단에 '모름', '앎', '완전 앎' 선택 가능한 버튼 3개 초기화
        Button_vocaBack_dontknow = findViewById(R.id.Button_vocaBack_dontknow);
        Button_vocaBack_know = findViewById(R.id.Button_vocaBack_know);
        Button_vocaBack_completeknow = findViewById(R.id.Button_vocaBack_completeknow); // 시간제한 스레드 추가 예정. 시간 지나면 이 버튼은 비활성화됨.
        Button_vocaBack_dontknow.setOnClickListener(this);
        Button_vocaBack_know.setOnClickListener(this);
        Button_vocaBack_completeknow.setOnClickListener(this);

        // 하단 '완전 앎' 버튼 위에 표시될 시간제한 초 출력하는 텍스트뷰 초기화
        TextView_timeCount = findViewById(R.id.TextView_timeCount);



        // 사용자가 선택한 단어장에 담긴 단어 목록 불러오기 - 단어 학습을 위해 필요한 데이터
        String vocaListKey = vocagroupName + " vocaList";
        SharedPreferences sharedPreferences = getSharedPreferences("Vocagroup", MODE_PRIVATE);
        Gson gson = new Gson();
        String vocaListJson = sharedPreferences.getString(vocaListKey, null);
        Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
        vocaList = gson.fromJson(vocaListJson, vocaListType);
//        Log.d(TAG, "onCreate vocaList : "+vocaList);

        // 단어 목록에서 학습할 단어 정보 불러오기
        // 전체 단어 목록에서 오늘 학습해야 하는 단어 목록을 필터링하여 todayLearningList에 추가
        // vocaShowing에 사용자에게 보여줘야 하는 텍스트, 이미지를 리스트 순서에 따라 차례대로 추가함
        // 각 영역별 앞면/뒷면(스위치 false/true) 설정값도 함께 받은 후, 그 값에 따라 VISIBLE과 GONE도 세팅됨
        long now = System.currentTimeMillis(); // 단어 학습 주기에 따른 단어 출력 날짜를 지정하기 위해 단어를 생성한 시간을 단어 데이터에 포함시킴
        Date date = new Date(now);
        Date cycleDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm", new Locale("ko", "KR"));
        String getTime = format.format(date);

//        Calendar calendar = Calendar.getInstance();

        if(vocaList != null && !vocaList.isEmpty()){
//            Log.d(TAG, "onCreate 단어 세팅 여부 확인");
            for(int i = 0; i < vocaList.size(); i++){
                try {
                    cycleDate = format.parse(vocaList.get(i).get(0).getAddedDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                learningDay = vocaList.get(i).get(0).getAddedDate().equals(getTime) | cycleDate.before(date);
//                Log.d(TAG, "learningDay 테스트 : "+vocaList.get(i).get(0).getAddedDate().equals(getTime)+" / "+cycleDate.before(date));

                if(learningDay){
                    todayLearningList.add(vocaList.get(i));
                    todayLearningListPosition.add(i);
//                    Log.d(TAG, "vocaList.get(i) : "+vocaList.get(i));
//                    for(int j = 0; j < vocaList.get(i).size(); j++){
//                        vocaShowItemList.add(vocaList.get(i).get(j));
//                    }
                }
            }
//            learningDay = vocaList.get(vocaOrder).get(0).getAddedDate().equals(getTime);
//            for(int i = 0; i < vocaList.get(vocaOrder).size(); i++){
//                if(learningDay){
//                    vocaShowItemList.add(vocaList.get(vocaOrder).get(i));
//                }
//            }
            if(!learningStart){
//                Log.d(TAG, "todayLearningList : "+todayLearningList);
                if(todayLearningList != null && !todayLearningList.isEmpty()){
                    for(int a = 0; a < todayLearningList.get(0).size(); a++){
                        vocaShowing.add(todayLearningList.get(0).get(a));
//                        Log.d(TAG, "todayLearningList.get(0).get(a) : "+todayLearningList.get(0).get(a));
                        learningStart = true;
                    }
                }
            }

            // 틀린 단어 수, 새 단어 수, 복습할 단어 수 세기 세기
            for(int i = 0; i < todayLearningList.size(); i++){
                if(Integer.parseInt(todayLearningList.get(i).get(1).getAddedDate()) == -1){
//                    wrongCountNumber = 0;
                    wrongCountNumber++;
                } else if(Integer.parseInt(todayLearningList.get(i).get(1).getAddedDate()) == 0){
//                    wrongCountNumber = 0;
                    newCountNumber++;
                } else {
//                    reviewCountNumber = 0;
                    reviewCountNumber++;
                }
            }
            newCount = Integer.toString(newCountNumber);
            TextView_vocaFront_newCount.setText(newCount);
            wrongCount = Integer.toString(wrongCountNumber);
            TextView_vocaFront_wrongCount.setText(wrongCount);
            reviewCount = Integer.toString(reviewCountNumber);
            TextView_vocaFront_reviewCount.setText(reviewCount);

            // onCreate에서 단어 추가하면 true가 됨
            // 그러면 onResume에서 다시 한번 똑같은 단어를 중복해서 추가하지 않음
            onCreateAdding = true;
        }

        // 시간제한 초 출력할 횟수를 지정해주기 위해 학습할 단어 갯수를 가져옴
        vocaNumber = todayLearningList.size();

        // 사용자가 선택한 단어장에 단어가 없으면 "학습할 단어가 없습니다. 단어를 추가해주세요." 문구가 출력됨
        TextView_vocaFront_noWords = findViewById(R.id.TextView_vocaFront_noWords);
        if(vocaShowing == null || vocaShowing.isEmpty()){
            // 단어가 없는 경우
            TextView_vocaFront_noWords.setVisibility(View.VISIBLE);
        } else {
            // 단어가 있는 경우
            TextView_vocaFront_noWords.setVisibility(View.GONE);
        }

        // 상단 툴바 가운데 이미지버튼 - 단어 수정(현재 사용자가 보고 있는 단어)
        ImageButton_vocaFront_modify = findViewById(R.id.ImageButton_vocaFront_modify);
        ImageButton_vocaFront_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VocaShow.this, VocaModify.class);

                // 사용자가 선택한 단어장 정보(제목, 영역 구성) 보내기
                SharedPreferences sharedPreferences = getSharedPreferences("Vocagroup", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                Gson gson = new Gson();
                String vocagroupJson = gson.toJson(vocagroup);
                editor.putString(vocagroupName, vocagroupJson); // 저장할 값 입력하기

                // 사용자가 선택한 단어장의 단어 목록 보내기
                String vocaListKey = vocagroupName + " vocaList";
                String vocaListJson = gson.toJson(vocaList);
                editor.putString(vocaListKey, vocaListJson);

                // 사용자가 현재 보고 있는 단어 데이터 보내기
                String vocaJson = gson.toJson(todayLearningList.get(vocaOrder));
                editor.putString("vocaModify", vocaJson);
                editor.putInt("vocaModifyPosition", vocaOrder);

                editor.commit();

                // '쉐어드이름' 과 동일한 xml 파일을 호출함(없으면 새로 생성함)
//                SharedPreferences sharedPreferencesM = getSharedPreferences(vocagroupName, MODE_PRIVATE);
//                SharedPreferences.Editor editorM = sharedPreferencesM.edit();
//                Gson gsonM = new Gson();
                // '저장할_객체'를 '저장할_객체_클래스명'의 형식에 맞추어 String으로 변환함
//                String voca = gsonM.toJson(저장할_객체, 저장할_객체_클래스명.class );
                // '쉐어드이름' xml 파일 안에 'String_식별값'이라는 이름으로 '스트링_변수명'의 내용을 저장함
//                editor.putString("String_식별값", voca);
//                editor.commit();

                intent.putExtra("vocagroupName", vocagroupName);
//                intent.putExtra("단어장 포지션", vocagroupPosition);
                startActivity(intent);
            }
        });

        // 학습할 단어 내용 출력할 리사이클러뷰와 레이아웃 매니저 초기화, 그리고 어댑터 연결
        ListView_vocaFront_listview = findViewById(R.id.ListView_vocaFront_listview);
        ListView_vocaFront_listview.setLayoutManager(linearLayoutManager); // 리사이클러뷰에 레이아웃 매니저 연결
        ListView_vocaFront_listview.setAdapter(vocaShowItemAdapter);

        timeCountThread = new TimeCountThread();
        timeCountThread.start();
//        vocaCountThread = new VocaCountThread();
//        vocaCountThread.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(), "VocaShow onResume",Toast.LENGTH_SHORT).show();
//        Log.d(TAG, "onResume learningStart : "+learningStart);

        threadOn = true;

        Intent intentRefreshAction = getIntent();
        int refreshAction = intentRefreshAction.getIntExtra("intentRefreshAction", 100);
//        vocagroupName = intentRefreshAction.getStringExtra("vocagroupName");
//        Log.d(TAG, "refreshAction : "+refreshAction);

        // 사용자가 선택한 단어장에 담긴 단어 목록 불러오기 - 단어 학습을 위해 필요한 데이터
        String vocaListKey = vocagroupName + " vocaList";
        SharedPreferences sharedPreferences = getSharedPreferences("Vocagroup", MODE_PRIVATE);
        Gson gson = new Gson();
        String vocaListJson = sharedPreferences.getString(vocaListKey, null);
        Type vocaListType = new TypeToken<ArrayList<ArrayList<VocaShowItem>>>(){}.getType();
        vocaList = gson.fromJson(vocaListJson, vocaListType);

        // 단어 목록에서 학습할 단어 정보 불러오기
        // vocaShowItemList에 사용자에게 보여줘야 하는 텍스트, 이미지를 배열 순서에 따라 차례대로 추가함
        // 각 영역별 앞면/뒷면(스위치 false/true) 설정값도 함께 받은 후, 그 값에 따라 VISIBLE과 GONE도 세팅됨
        long now = System.currentTimeMillis(); // 단어 학습 주기에 따른 단어 출력 날짜를 지정하기 위해 단어를 생성한 시간을 단어 데이터에 포함시킴
        Date date = new Date(now);
        Date cycleDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm", new Locale("ko", "KR"));
        String getTime = format.format(date);

//        Log.d(TAG, "onResume onCreateAdding : " + onCreateAdding);
        if(!onCreateAdding){
            if(vocaList != null && !vocaList.isEmpty()){
                for(int i = 0; i < vocaList.size(); i++){
                    try {
                        cycleDate = format.parse(vocaList.get(i).get(0).getAddedDate());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    learningDay = vocaList.get(i).get(0).getAddedDate().equals(getTime) | cycleDate.before(date);
//                Log.d(TAG, "learningDay 테스트 : "+vocaList.get(i).get(0).getAddedDate().equals(getTime)+" / "+cycleDate.before(date));

                    if(learningDay){
                        todayLearningList.add(vocaList.get(i));
                        todayLearningListPosition.add(i);
                    }
                }


//            learningDay = vocaList.get(vocaOrder).get(0).getAddedDate().equals(getTime);
//            for(int i = 0; i < vocaList.get(vocaOrder).size(); i++){
//                if(learningDay){
//                    vocaShowItemList.add(vocaList.get(vocaOrder).get(i));
//                }
//            }

                switch (refreshAction) {
                    case 10: // 단어 추가한 경우
                        if(!learningStart){
                            if(vocaShowing.isEmpty()){
                                for(int a = 0; a < todayLearningList.get(0).size(); a++){
                                    vocaShowing.add(todayLearningList.get(0).get(a));
                                    learningStart = true;
//                                Log.d(TAG, "onResume vocaShowing : "+vocaShowing);
                                }
                            }
                        }
                        break;
                    case 11: // 단어 수정, 삭제한 경우
                        // 삭제 후 다음 단어 출력되게 고쳐야 함
//                    if(!learningStart){
                        for(int a = 0; a < todayLearningList.get(0).size(); a++){
                            vocaShowing.set(a, todayLearningList.get(0).get(a));
                            learningStart = true;
                        }
//                    }
                        break;
                    default:
                        break;
                }

                // 틀린 단어 수, 새 단어 수, 복습할 단어 수 세기 세기
                wrongCountNumber = 0;
                wrongCountNumber = 0;
                reviewCountNumber = 0;
                for(int i = 0; i < todayLearningList.size(); i++){
                    if(Integer.parseInt(todayLearningList.get(i).get(1).getAddedDate()) == -1){
                        wrongCountNumber++;
                    } else if(Integer.parseInt(todayLearningList.get(i).get(1).getAddedDate()) == 0){
                        newCountNumber++;
                    } else {
                        reviewCountNumber++;
                    }
                }
                newCount = Integer.toString(newCountNumber);
                TextView_vocaFront_newCount.setText(newCount);
                wrongCount = Integer.toString(wrongCountNumber);
                TextView_vocaFront_wrongCount.setText(wrongCount);
                reviewCount = Integer.toString(reviewCountNumber);
                TextView_vocaFront_reviewCount.setText(reviewCount);

            }
        }


        // 시간제한 초 출력할 횟수를 지정해주기 위해 학습할 단어 갯수를 가져옴
        vocaNumber = vocaShowing.size();
//        Log.d(TAG, "vocaNumber : "+vocaNumber);

        // 사용자가 선택한 단어장에 단어가 없으면 "학습할 단어가 없습니다. 단어를 추가해주세요." 문구가 출력됨
        TextView_vocaFront_noWords = findViewById(R.id.TextView_vocaFront_noWords);
        if(vocaShowing == null || vocaShowing.isEmpty()){
            // 단어가 없는 경우
            TextView_vocaFront_noWords.setVisibility(View.VISIBLE);
        } else {
            // 단어가 있는 경우
            TextView_vocaFront_noWords.setVisibility(View.GONE);
        }

        vocaShowItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "onPause",Toast.LENGTH_LONG).show();
        timeCountThread.interrupt();
//        vocaCountThread.interrupt();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "onDestroy",Toast.LENGTH_LONG).show();
        timeCountThread.interrupt();
//        vocaCountThread.interrupt();
    }

    @Override
    public void sideChange() {
        boolean frontShow;
        for(int i = 0; i < vocaShowItemAdapter.getItemCount(); i++){
            frontShow = vocaShowItemAdapter.getData().get(i).isShowingSide();
            if(!frontShow){
                vocaShowItemAdapter.getData().get(i).setShowingSide(true);
//                frontShow = true;
            } else {
                vocaShowItemAdapter.getData().get(i).setShowingSide(false);
//                frontShow = false;
            }
        }
        sideCheck = !sideCheck;
        if(!sideCheck){
            Button_vocaBack_dontknow.setVisibility(View.VISIBLE);
            Button_vocaBack_know.setVisibility(View.VISIBLE);
            Button_vocaBack_completeknow.setVisibility(View.VISIBLE);
        }else{
            Button_vocaBack_dontknow.setVisibility(View.GONE);
            Button_vocaBack_know.setVisibility(View.GONE);
            Button_vocaBack_completeknow.setVisibility(View.GONE);
        }
        vocaShowItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        // 뒷면 버튼 누르는 것에 따라 단어 학습 주기 재지정해줌(모름:처음으로(0) / 앎:다음 학습주기로 / 완전 앎:마지막 학습 주기로)
        long now = System.currentTimeMillis(); // 단어 학습 주기에 따른 단어 출력 날짜를 지정하기 위해 단어를 생성한 시간을 단어 데이터에 포함시킴
        Date date = new Date(now);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm", new Locale("ko", "KR"));
        String getTime = format.format(date); // 현재 시각

        Calendar calendar = Calendar.getInstance(new Locale("ko", "KR"));
        calendar.setTime(date);

        String nextTime; // 단어의 다음 차례 학습할 시각
        int seq = 0; // 단어의 다음 학습 주기까지 걸릴 시간
        int nextSeq = 0; // 단어의 다음 학습 주기 순서

        Log.d(TAG, "vocaLearningCycleSequence : "+ vocaLearningCycleSequence);

        switch (v.getId()) {
            case R.id.Button_vocaBack_dontknow: // 모름
                Log.d(TAG, "vocaShowing 변경 전 : "+vocaShowing);
                calendar.add(Calendar.MINUTE, 1);
                nextTime = format.format(calendar.getTime());
                vocaShowing.get(0).setAddedDate(nextTime);
                vocaShowing.get(1).setAddedDate(Integer.toString(-1)); // 틀린 것은 -1으로 지정해서 틀린 갯수 따로 세기
                System.out.println(vocaShowing.get(0).getVocaShowText()+" / "+vocaShowing.get(0).getAddedDate());

                for(int i = 0; i < vocaShowing.size(); i++){
                    vocaShowing.get(i).setShowingSide(!vocaShowing.get(i).isShowingSide());
                }

//                Log.d(TAG, "vocaOrder 변경 전 : "+vocaList.get(vocaOrder));
//                Log.d(TAG, "onClick vocaList 변경 전 : "+vocaList);

                vocaList.set(todayLearningListPosition.get(vocaOrder), vocaShowing);
                Log.d(TAG, "vocaShowing 변경 후 : "+vocaShowing);
//                Log.d(TAG, "vocaOrder 변경 후 : "+vocaList.get(vocaOrder));
//                Log.d(TAG, "onClick vocaList 변경 후 : "+vocaList);

//                // 사용자가 선택한 단어장 정보(제목, 영역 구성) 보내기
//                SharedPreferences sharedPreferences = getSharedPreferences("Vocagroup", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                Gson gson = new Gson();
////                String vocagroupJson = gson.toJson(vocagroup);
////                editor.putString(vocagroupName, vocagroupJson); // 저장할 값 입력하기
//
//                // 사용자가 선택한 단어장의 단어 목록 보내기
////                String vocaListKey = vocagroupName + " vocaList";
////                String vocaListJson = gson.toJson(vocaList);
////                editor.putString(vocaListKey, vocaListJson);
//
//                // 사용자가 현재 보고 있는 단어 데이터 보내기
////                String vocaJson = gson.toJson(todayLearningList.get(vocaOrder));
////                editor.putString("vocaModify", vocaJson);
////                editor.putInt("vocaModifyPosition", vocaOrder);
//                String vocaListJsonSave = gson.toJson(vocaList);
//                String vocaListKey = vocagroupName + " vocaList";
//                editor.putString(vocaListKey, vocaListJsonSave);
//
//                editor.commit();

                break;
            case R.id.Button_vocaBack_know: // 앎
                Log.d(TAG, "vocaShowing 변경 전 : "+vocaShowing);
                // vocaShowing.get(1).getAddedDate()로 단어 학습 주기의 순서를 가져옴
                if(Integer.parseInt(vocaShowing.get(1).getAddedDate()) < vocaLearningCycleSequence.size()){
                    // 가져온 순서의 값이 실제 단어 학습 주기의 주기 갯수 이하인 경우
                    if(Integer.parseInt(vocaShowing.get(1).getAddedDate()) == -1){
                        // '모름' 먼저 눌렀다가 '앎' 누른 경우
//                        seq = vocaLearningCycleSequence.get(Integer.parseInt(vocaShowing.get(1).getAddedDate())+1);
                        calendar.add(Calendar.MINUTE, 5); // 현재 시각 기준으로 다음에 학습할 시각 지정하기
                    } else {
                        // 그 외 경우
                        seq = vocaLearningCycleSequence.get(Integer.parseInt(vocaShowing.get(1).getAddedDate()));
                        calendar.add(Calendar.MINUTE, seq); // 현재 시각 기준으로 다음에 학습할 시각 지정하기
                    }
                } else {
                    // 가져온 순서의 값이 실제 단어 학습 주기의 주기 갯수 초과인 경우
                    // 마지막 학습 주기 이후 반복한 횟수를 마지막 학습 주기 값에 곱한 시간을 다음 학습 주기로 지정
                    seq = vocaLearningCycleSequence.get(vocaLearningCycleSequence.size()-1)*(Integer.parseInt(vocaShowing.get(1).getAddedDate())-vocaLearningCycleSequence.size());
                    calendar.add(Calendar.MINUTE, seq); // 현재 시각 기준으로 다음에 학습할 시각 지정하기
                }

                // 다음 학습 주기를 지정하기 위해 현재 위치한 학습 주기에 1을 더함
                nextSeq = Integer.parseInt(vocaShowing.get(1).getAddedDate())+2;

//                calendar.add(Calendar.MINUTE, seq); // 현재 시각 기준으로 다음에 학습할 시각 지정하기
                nextTime = format.format(calendar.getTime());
                vocaShowing.get(0).setAddedDate(nextTime); // 지정한 시각의 값을 영역 1번에 저장
                vocaShowing.get(1).setAddedDate(Integer.toString(nextSeq)); // 다음 학습 주기 순서를 영역 2번에 저장
                System.out.println(vocaShowing.get(0).getVocaShowText()+" / "+vocaShowing.get(0).getAddedDate()+" / "+vocaShowing.get(1).getAddedDate());

                for(int i = 0; i < vocaShowing.size(); i++){
                    vocaShowing.get(i).setShowingSide(!vocaShowing.get(i).isShowingSide());
                }
//                Log.d(TAG, "단어 주기 변경 전 : "+vocaList.get(todayLearningListPosition.get(vocaOrder)));
                vocaList.set(todayLearningListPosition.get(vocaOrder), vocaShowing);
                Log.d(TAG, "vocaShowing 변경 후 : "+vocaShowing);
//                Log.d(TAG, "단어 주기 변경 후 : "+vocaList.get(todayLearningListPosition.get(vocaOrder)));

                break;
            case R.id.Button_vocaBack_completeknow: // 완전 앎
                Log.d(TAG, "vocaShowing 변경 전 : "+vocaShowing);
                // vocaShowing.get(1).getAddedDate()로 단어 학습 주기의 순서를 가져옴
                if(Integer.parseInt(vocaShowing.get(1).getAddedDate()) < vocaLearningCycleSequence.size()){
                    // 가져온 순서의 값이 실제 단어 학습 주기의 주기 갯수 이하인 경우
                    // '완전 앎' 이므로 마지막 학습 주기 값을 가져옴
                    seq = vocaLearningCycleSequence.get(vocaLearningCycleSequence.size()-1);
                } else {
                    // 가져온 순서의 값이 실제 단어 학습 주기의 주기 갯수 초과인 경우
                    // 마지막 학습 주기 이후 반복한 횟수를 마지막 학습 주기 값에 곱한 시간을 다음 학습 주기로 지정
                    seq = vocaLearningCycleSequence.get(vocaLearningCycleSequence.size()-1)*(Integer.parseInt(vocaShowing.get(1).getAddedDate())-vocaLearningCycleSequence.size());
                }

                // 마지막 학습 주기로 지정해줌
                nextSeq = vocaLearningCycleSequence.size()-1;

                calendar.add(Calendar.MINUTE, seq); // 현재 시각 기준으로 다음에 학습할 시각 지정하기
                nextTime = format.format(calendar.getTime());
                vocaShowing.get(0).setAddedDate(nextTime); // 지정한 시각의 값을 영역 1번에 저장
                vocaShowing.get(1).setAddedDate(Integer.toString(nextSeq)); // 다음 학습 주기 순서를 영역 2번에 저장

                for(int i = 0; i < vocaShowing.size(); i++){
                    vocaShowing.get(i).setShowingSide(!vocaShowing.get(i).isShowingSide());
                }
//                Log.d(TAG, "단어 주기 변경 전 : "+vocaList.get(todayLearningListPosition.get(vocaOrder)));
                vocaList.set(todayLearningListPosition.get(vocaOrder), vocaShowing);
                Log.d(TAG, "vocaShowing 변경 후 : "+vocaShowing);
//                Log.d(TAG, "단어 주기 변경 후 : "+vocaList.get(todayLearningListPosition.get(vocaOrder)));

                break;
            default:
                break;
        }

        // 틀린 단어 수, 새 단어 수, 복습할 단어 수 세기
        wrongCountNumber = 0;
        newCountNumber = 0;
        reviewCountNumber = 0;
        for(int i = 0; i < todayLearningList.size(); i++){
            if(Integer.parseInt(todayLearningList.get(i).get(1).getAddedDate()) == -1){
                wrongCountNumber++;
            } else if(Integer.parseInt(todayLearningList.get(i).get(1).getAddedDate()) == 0){
                newCountNumber++;
            } else {
                reviewCountNumber++;
            }
        }
        newCount = Integer.toString(newCountNumber);
        TextView_vocaFront_newCount.setText(newCount);
        wrongCount = Integer.toString(wrongCountNumber);
        TextView_vocaFront_wrongCount.setText(wrongCount);
        reviewCount = Integer.toString(reviewCountNumber);
        TextView_vocaFront_reviewCount.setText(reviewCount);

        // 사용자가 선택한 단어장 정보(제목, 영역 구성) 보내기
        SharedPreferences sharedPreferences = getSharedPreferences("Vocagroup", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
//                String vocagroupJson = gson.toJson(vocagroup);
//                editor.putString(vocagroupName, vocagroupJson); // 저장할 값 입력하기

        // 사용자가 선택한 단어장의 단어 목록 보내기
//                String vocaListKey = vocagroupName + " vocaList";
//                String vocaListJson = gson.toJson(vocaList);
//                editor.putString(vocaListKey, vocaListJson);

        // 사용자가 현재 보고 있는 단어 데이터 보내기
//                String vocaJson = gson.toJson(todayLearningList.get(vocaOrder));
//                editor.putString("vocaModify", vocaJson);
//                editor.putInt("vocaModifyPosition", vocaOrder);
        String vocaListJsonSave = gson.toJson(vocaList);
        String vocaListKey = vocagroupName + " vocaList";
        editor.putString(vocaListKey, vocaListJsonSave);

        editor.commit();

//        Log.d(TAG, "vocaOrder 숫자 변경 전 : "+vocaOrder);
        vocaOrder++; // 다음 단어 출력하기 위해 단어의 순서 숫자를 증가시킴
//        Log.d(TAG, "vocaOrder 숫자 변경 후 : "+vocaOrder);
//        Log.d(TAG, "vocaOrder < todayLearningList.size() : "+vocaOrder +"<"+ todayLearningList.size());
        if(vocaOrder < todayLearningList.size()){
            // 다음 단어 학습할 경우
            sideChange();
            for(int i = 0 ; i < todayLearningList.get(vocaOrder).size(); i++){
//                Log.d(TAG, "onClick vocaShowing.set 변경 전 : "+vocaList);
                vocaShowing.set(i, todayLearningList.get(vocaOrder).get(i));
//                Log.d(TAG, "onClick vocaShowing.set 변경 후 : "+vocaList);
//                Log.d(TAG, i+" / "+vocaOrder+" / "+todayLearningList.get(vocaOrder).get(i));
//                Log.d(TAG, "vocaShowing.size : "+vocaShowing.size());
//                Log.d(TAG, "vocaShowing : "+vocaShowing);
                vocaShowItemAdapter.notifyItemChanged(i);
            }

        } else {
            // 모든 단어 학습 완료된 경우
            sideChange();
            vocaShowing.removeAll(vocaShowing);
            vocaShowItemAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), "단어 학습 완료",Toast.LENGTH_SHORT).show();
            TextView_vocaFront_noWords.setVisibility(View.VISIBLE);
        }
        vocaNumber--; // 학습해야 하는 단어 숫자를 감소시킴
        timeCount = 5; // 우측 하단에 '완전 앎' 버튼 비활성화까지 남은 시각 초기화
        TextView_timeCount.setText(Integer.toString(timeCount));
        TextView_timeCount.setVisibility(View.VISIBLE);
//        Button_vocaBack_completeknow.setTextColor(R.color.colorBlack);
        Button_vocaBack_completeknow.setEnabled(true);
        threadOn = true;
        if(timeCounting){
            // 제한 시간 가는 도중에 버튼 누르면 바로 시간 초기화시키기 위해
            // 시간 초 세는 스레드를 중지시켰다가 다시 돌림
            timeCountThread.interrupt();
            countRefresh = true;
        }
    }

    Handler timeCountHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 0){
                if(vocaShowing == null || vocaShowing.isEmpty()) {
                    TextView_timeCount.setVisibility(View.INVISIBLE);
                    TextView_timeCount.setText("");
                    timeCountThread.interrupt();
                }
                TextView_timeCount.setText(Integer.toString(timeCount));
                if(timeCount == 0){
                    Button_vocaBack_completeknow.setEnabled(false);
//                    Button_vocaBack_completeknow.setTextColor(R.color.colorPrimary);
                    TextView_timeCount.setVisibility(View.INVISIBLE);
                }
            }
        }
    };

    boolean timeCounting;
    boolean countRefresh;

    private class TimeCountThread extends Thread {

        @Override
        public void run() {
            while(vocaNumber != 0 && threadOn){
                if(timeCount > 0){
                    TimeCountRefresh:
                    for(int i=0; i<5; i++){
                        timeCounting = true;
                        timeCount--;
                        timeCountHandler.sendEmptyMessage(0);
                        try {
                            sleep(1000);
                        } catch (InterruptedException e) {
                            if(countRefresh){
                                countRefresh = false;
                                break TimeCountRefresh;
                            }
                            threadOn = false;
                        }
                    }
                    timeCounting = false;
                }
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    threadOn = false;
                }
            }
            Log.d(TAG, "TimeCountThread 중지됨");
        }
    }

    Handler vocaCountHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 1){
                TextView_vocaFront_newCount.setText(newCount);
                TextView_vocaFront_wrongCount.setText(wrongCount);
                TextView_vocaFront_reviewCount.setText(reviewCount);
            }
        }
    };

    private class VocaCountThread extends Thread {

        long now = System.currentTimeMillis(); // 단어 학습 주기에 따른 단어 출력 날짜를 지정하기 위해 단어를 생성한 시간을 단어 데이터에 포함시킴
        Date date = new Date(now);
        Date cycleDate = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH:mm", new Locale("ko", "KR"));
        String getTime = format.format(date);

        @Override
        public void run() {
            while(threadOn){
                if(vocaList != null && !vocaList.isEmpty()){
//            Log.d(TAG, "onCreate 단어 세팅 여부 확인");
//                    for(int i = 0; i < vocaList.size(); i++){
//                        try {
//                            cycleDate = format.parse(vocaList.get(i).get(0).getAddedDate());
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        learningDay = vocaList.get(i).get(0).getAddedDate().equals(getTime) | cycleDate.before(date);
//                Log.d(TAG, "learningDay 테스트 : "+vocaList.get(i).get(0).getAddedDate().equals(getTime)+" / "+cycleDate.before(date));

//                        if(learningDay){
//                            todayLearningList.add(vocaList.get(i));
//                            todayLearningListPosition.add(i);
//                        }
//                    }

//                    if(!learningStart){
////                Log.d(TAG, "todayLearningList : "+todayLearningList);
//                        if(todayLearningList != null && !todayLearningList.isEmpty()){
//                            for(int a = 0; a < todayLearningList.get(0).size(); a++){
//                                vocaShowing.add(todayLearningList.get(0).get(a));
////                        Log.d(TAG, "todayLearningList.get(0).get(a) : "+todayLearningList.get(0).get(a));
//                                learningStart = true;
//                            }
//                        }
//                    }

                    // 틀린 단어 수, 새 단어 수, 복습할 단어 수 세기 세기
                    wrongCountNumber = 0;
                    newCountNumber = 0;
                    reviewCountNumber = 0;
//                    Log.d(TAG, "단어 숫자 확인 : "+wrongCountNumber+" / "+newCountNumber+" / "+reviewCountNumber);
                    for(int i = 0; i < todayLearningList.size(); i++){
                        if(Integer.parseInt(todayLearningList.get(i).get(1).getAddedDate()) == -1){
                            wrongCountNumber++;
                        } else if(Integer.parseInt(todayLearningList.get(i).get(1).getAddedDate()) == 0){
                            newCountNumber++;
                        } else {
                            reviewCountNumber++;
                        }
                    }
//                    Log.d(TAG, "단어 숫자 확인 : "+wrongCountNumber+" / "+newCountNumber+" / "+reviewCountNumber);
                    newCount = Integer.toString(newCountNumber);
                    wrongCount = Integer.toString(wrongCountNumber);
                    reviewCount = Integer.toString(reviewCountNumber);
//                    TextView_vocaFront_newCount.setText(newCount);
//                    TextView_vocaFront_wrongCount.setText(wrongCount);
//                    TextView_vocaFront_reviewCount.setText(reviewCount);

                    // onCreate에서 단어 추가하면 true가 됨
                    // 그러면 onResume에서 다시 한번 똑같은 단어를 중복해서 추가하지 않음
//                    onCreateAdding = true;
                }
                vocaCountHandler.sendEmptyMessage(1);
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}